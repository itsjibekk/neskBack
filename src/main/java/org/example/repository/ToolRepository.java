package org.example.repository;

import org.example.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
