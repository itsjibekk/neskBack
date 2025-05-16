package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.ZipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LayerController {

    @Autowired
    private ZipService zipService;

    @GetMapping("/find/{zip}")
    private String getZipDetails(@PathVariable("zip") String zip){
        return zipService.getZipDetails(zip);
    }

}
