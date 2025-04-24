package org.example.mapper;

import org.example.dto.SignUpDto;
import org.example.entity.User;
import org.example.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    public User signUpToUser(SignUpDto signUpDto);
}
