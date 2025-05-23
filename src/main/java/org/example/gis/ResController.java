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
@RequestMapping("/api/res")
public class ResController {
    @Autowired
    private RegionElectricBranchService resService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getRes(@PathVariable Long id) {
        return resService.getById(id)
                .map(pes -> Map.ofEntries(
                        Map.entry("name", Objects.toString(pes.getName(), "")),
                        Map.entry("address", Objects.toString(pes.getAddress(), "")),
                        Map.entry("phone", Objects.toString(pes.getPhone(), ""))
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
