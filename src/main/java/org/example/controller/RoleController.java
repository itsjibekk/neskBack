package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.Tool;
import org.example.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepo;

    @GetMapping
    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    @GetMapping("/{id}/tools")
    public Set<Tool> getTools(@PathVariable Long id) {
        return roleRepo.findById(id)
                .map(Role::getTools)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

