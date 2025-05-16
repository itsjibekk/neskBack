package org.example.controller;

import org.example.dto.RoleDto;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = convertToUser(userDto);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToUserDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setStatus(userDto.getStatus());

        Set<Role> roles = roleRepository.findAllById(
                userDto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok(convertToUserDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto convertToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setStatus(user.getStatus());
        dto.setCreatedOn(user.getCreatedOn());
        dto.setRoles(user.getRoles().stream().map(this::convertToRoleDto).collect(Collectors.toSet()));
        return dto;
    }

    private RoleDto convertToRoleDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

    private User convertToUser(UserDto dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setStatus(dto.getStatus());
        user.setPassword(dto.getPassword());

        Set<Role> roles = roleRepository.findAllById(
                dto.getRoles().stream().map(RoleDto::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.toSet());

        user.setRoles(roles);
        return user;
    }
}
