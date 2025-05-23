package org.example.gis;

import org.example.gis.entity.RegionElectricBranch;
import org.example.gis.repository.RegionElectricBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegionElectricBranchService {

    @Autowired
    private RegionElectricBranchRepository resRepository;

    public Optional<RegionElectricBranch> getById(Long id) {
        return resRepository.findById(id);
    }
}
