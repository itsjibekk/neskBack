package org.example.mapper;

import org.example.dto.RoleDto;
import org.example.dto.SignUpDto;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class, Set.class})
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(this::toRoleDto).collect(Collectors.toSet()))")
    UserDto userToUserDto(User user);

    RoleDto toRoleDto(Role role);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

    User toUser(UserDto userDto);

    Set<Role> toRoleSet(Set<RoleDto> roles);
}
