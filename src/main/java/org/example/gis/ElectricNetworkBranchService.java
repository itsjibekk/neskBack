package org.example.gis;

import org.example.gis.entity.ElectricNetworkBranch;
import org.example.gis.repository.ElectricNetworkBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ElectricNetworkBranchService {
    @Autowired
    private ElectricNetworkBranchRepository pesRepository;

    public Optional<ElectricNetworkBranch> getById(Long id) {
        return pesRepository.findById(id);
    }
}

