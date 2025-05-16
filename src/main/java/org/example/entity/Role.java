package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER )
    private Set<Tool> tools = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Tool> getTools() {
        return tools;
    }

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public void setTools(Set<Tool> tools) {
        this.tools = tools;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
