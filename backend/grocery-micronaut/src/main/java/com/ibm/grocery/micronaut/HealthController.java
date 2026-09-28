package com.ibm.grocery.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.Map;

@Controller("/micronaut")
public class HealthController {

    @Get("/status")
    public Map<String, String> status() {
        return Map.of("framework", "micronaut", "status", "running");
    }
}
