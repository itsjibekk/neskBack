package org.example.gis.repository;

import org.example.gis.entity.ElectricNetworkBranch;
import org.example.gis.entity.RegionElectricBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionElectricBranchRepository extends JpaRepository<RegionElectricBranch, Long> {
}
