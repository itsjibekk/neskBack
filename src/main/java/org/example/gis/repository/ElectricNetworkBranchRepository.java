package org.example.gis.repository;

import org.example.gis.entity.ElectricNetworkBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricNetworkBranchRepository extends JpaRepository<ElectricNetworkBranch, Long> {

}