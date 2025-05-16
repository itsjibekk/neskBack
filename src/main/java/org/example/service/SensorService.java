package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.dto.SensorData;

@Service
public class SensorService {
    private final RestTemplate restTemplate = new RestTemplate();

    public SensorData getTransformerData(Long transformerId) {
        String url = "https://run.mocky.io/v3/2356459c-1716-4bfe-b2bb-7ee70cee3057";

        return restTemplate.getForObject(url, SensorData.class);
    }
}
