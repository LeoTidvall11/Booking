# Bokningssystem

Ett enkelt REST API för bokningar (Spring Boot, Java). Systemet körs i tre backend-instanser bakom Nginx med round robin. Alla instanser använder samma PostgreSQL-databas. Allt startas med Docker Compose.

```
Klient -> Nginx -> API 1 / API 2 / API 3 -> PostgreSQL
```

## Starta projektet

Du behöver Docker och Git. För att testa API:et behövs ett program som Bruno eller Postman.

1. Klona repositoryt:

   ```
   git clone https://github.com/LeoTidvall11/Booking.git
   ```

2. Skapa en `.env`-fil utifrån `.env.example` och byt gärna lösenord:

3. Bygg och starta allt:

   ```
   docker compose up --build -d
   ```

4. Kontrollera att alla containers är `healthy` (det kan ta en minut):

   ```
   docker compose ps
   ```

API:et nås via Nginx på **http://localhost** (port 80).

Stoppa allt med `docker compose down`. Lägg till `-v` om du även vill ta bort databasens data.


## Databasen

PostgreSQL startas som tjänsten `postgres-db` i `compose.yaml` och sparar sin data i en Docker-volym, så bokningar finns kvar efter omstart. Tabellen skapas automatiskt. Vill du bara starta databasen:

```
docker compose up -d postgres-db
```

## Tre backend-instanser

Samma applikation (byggd från `Dockerfile`) körs som tre tjänster i `compose.yaml`: `booking-api-1`, `booking-api-2` och `booking-api-3` (containrarna heter `booking-app-1`, `-2` och `-3`). De startas alla av `docker compose up --build -d`. Nginx fördelar requests mellan dem.

## API

Skicka requests till `http://localhost` i Bruno eller Postman.

- `GET /bookings` - hämta alla bokningar
- `GET /bookings/{id}` - hämta en bokning
- `POST /bookings` - skapa en bokning
- `PUT /bookings/{id}` - uppdatera en bokning
- `DELETE /bookings/{id}` - ta bort en bokning
- `GET /instance` - visar vilken instans som svarade

En bokning har namn, e-post, telefonnummer, datum och tid, antal personer och status (`PENDING`, `CONFIRMED`, `CANCELLED` eller `COMPLETED`). Utan status blir den `PENDING`. Ogiltig indata ger `400` och en bokning som inte finns ger `404`.

### Exempel att testa med

Ange `Content-Type: application/json` på requests med body (Bruno och Postman gör det själva när du väljer Body → JSON).

**1. Skapa en bokning** - `POST http://localhost/bookings`

```json
{
  "name": "Anna Andersson",
  "email": "anna@example.com",
  "phoneNumber": "0701234567",
  "numberOfPeople": 4,
  "date": "2026-12-01T18:00:00"
}
```

Svaret ska ha status `200` och innehåller bokningens `id`, `status` (`PENDING`) och `createdAt`. Kopiera värdet på `id` och använd det i exemplen nedan i stället för `{id}`.

**2. Hämta alla bokningar** - `GET http://localhost/bookings`

**3. Hämta en bokning** - `GET http://localhost/bookings/{id}`

**4. Uppdatera en bokning** - `PUT http://localhost/bookings/{id}`

`PUT` ersätter bokningens uppgifter, så skicka med alla fält. Här bekräftas bokningen och antalet ändras:

```json
{
  "name": "Anna Andersson",
  "email": "anna@example.com",
  "phoneNumber": "0701234567",
  "numberOfPeople": 6,
  "date": "2026-12-01T18:00:00",
  "status": "CONFIRMED"
}
```

Hämta bokningen igen med `GET` för att se ändringen. Statusvärden som går att använda är `PENDING`, `CONFIRMED`, `CANCELLED` och `COMPLETED`.

**5. Ta bort en bokning** - `DELETE http://localhost/bookings/{id}`

Svaret är `204` utan innehåll. Ett `GET` på samma id därefter ger `404`.

**6. Felfall som ska ge felmeddelande**

`POST http://localhost/bookings` med ogiltig data ska ge `400`:

```json
{
  "name": "",
  "email": "inte-en-epost",
  "phoneNumber": "0701234567",
  "numberOfPeople": 0,
  "date": "2026-12-01T18:00:00"
}
```

`GET http://localhost/bookings/00000000-0000-0000-0000-000000000000` ska ge `404`, eftersom ingen bokning har det id:t.

## Testa load balancing

1. Skicka `GET http://localhost/instance`. Svaret visar vilken instans som hanterade requesten, till exempel:

   ```json
   { "instance": "api-1" }
   ```

2. Skicka samma request flera gånger i rad. Svaren ska växla mellan `api-1`, `api-2` och `api-3`.

3. Skapa en bokning med `POST /bookings` och hämta den sedan flera gånger med `GET /bookings`. Bokningen finns kvar oavsett vilken instans som svarar, eftersom alla använder samma databas.

## Testa att en instans stoppas

1. Skapa en bokning med `POST /bookings`.
2. Stoppa en instans i terminalen:

   ```
   docker stop booking-app-2
   ```

3. Skicka `GET /instance` några gånger. Nu ska bara `api-1` och `api-3` svara. Första requesten kan ta lite längre tid.
4. Skicka `GET /bookings` och kontrollera att bokningen finns kvar.
5. Skapa gärna en ny bokning medan instansen är stoppad. Det ska fungera.
6. Starta instansen igen:

   ```
   docker start booking-app-2
   ```
