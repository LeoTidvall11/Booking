package com.example.booking.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class InstanceController {

    @Value("${instance.name}")
    private String instanceName;

    @GetMapping("/instance")
    public Map<String, String> getInstanceName() {
        return Map.of("instance", instanceName);
    }
}
