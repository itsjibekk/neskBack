package org.example.gis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/pes")
public class PesController {

    @Autowired
    private ElectricNetworkBranchService pesService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getPes(@PathVariable Long id) {
       return pesService.getById(id)
                .map(pes -> Map.ofEntries(
                        Map.entry("name", Objects.toString(pes.getName(), "")),
                        Map.entry("address", Objects.toString(pes.getAddress(), "")),
                        Map.entry("phone", Objects.toString(pes.getPhone(), "")),
                        Map.entry("email", Objects.toString(pes.getEmail(), "")),
                        Map.entry("website", Objects.toString(pes.getWebsite(), "")),
                        Map.entry("region", Objects.toString(pes.getRegion(), ""))
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
