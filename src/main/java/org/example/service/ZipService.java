package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ZipService {

    public String  getZipDetails(String zip){
        final String uri = "URI" + zip;
        RestTemplate restTemplate =  new RestTemplate();
        String result;
        result = restTemplate.getForObject(uri, Map.class).toString();
        return result;
    }

}
