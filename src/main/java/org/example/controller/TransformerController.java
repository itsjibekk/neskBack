package org.example.controller;

import org.example.dto.SensorData;
import org.example.service.SensorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transformers")
public class TransformerController {

    private final SensorService sensorService;

    public TransformerController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/{id}/data")
    public SensorData getTransformerData(@PathVariable Long id) {
        return sensorService.getTransformerData(id);
    }
}
