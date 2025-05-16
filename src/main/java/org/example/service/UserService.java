package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CredentialsDto;
import org.example.dto.SignUpDto;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.repository.UserRepository;

import java.nio.CharBuffer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto){
        User user =  userRepository.findByLogin(credentialsDto.login()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword())){
            return  userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> oUser = userRepository.findByLogin(signUpDto.login());

        if(oUser.isPresent()){
            throw new AppException("Login already exists",HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setStatus("Active");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        user.setCreatedOn(sqlDate);
        Set<Role> set = new HashSet<>();
        set.add(new Role("manager"));
        user.setRoles(set);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }


    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

    public UserDto createUser(UserDto userDto) {
        // Проверка: нет ли уже такого логина
//        Optional<User> existingUser = userRepository.findByLogin(userDto.getLogin());
//        if (existingUser.isPresent()) {
//            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
//        }
//
//        // Маппинг DTO -> Entity
//        User user = userMapper.toUser(userDto);
//
//        // Шифруем пароль
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//        // Дата создания
//        Date date = new Date();
//        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//        user.setCreatedOn(sqlDate);
//
//        // Статус
//        user.setStatus("Active");
//
//        // Роли (если roles пусто — можно добавить роль по умолчанию)
//        if (user.getRoles() == null || user.getRoles().isEmpty()) {
//            Set<Role> defaultRoles = new HashSet<>();
//            defaultRoles.add(new Role("manager")); // или "user" по твоей логике
//            user.setRoles(defaultRoles);
//        }
//
//        // Сохраняем пользователя
//        User savedUser = userRepository.save(user);
//
//        // Возвращаем обратно DTO
//        return userMapper.toUserDto(savedUser);
//    }
//
//
//    public UserDto updateUser(Long id, UserDto userDto) {
//        // Ищем пользователя
////        User user = userRepository.findById(id)
////                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
////
////        // Обновляем нужные поля
////        user.setFirstName(userDto.getFirstName());
////        user.setLastName(userDto.getLastName());
////        user.setLogin(userDto.getLogin());
////        user.setStatus(userDto.getStatus());
////
////        // Если пришёл пароль — обновляем
////        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
////            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
////        }
////
////        // Обновляем роли, если переданы
////        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
////            user.setRoles(userMapper.toRoleSet(userDto.getRoles())); // предполагаем, что userMapper умеет конвертировать
////        }
////
////        // Сохраняем изменения
////        User updatedUser = userRepository.save(user);
////
////        // Возвращаем обратно DTO
////        return userMapper.toUserDto(updatedUser);
        return null;
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        return null;
    }
}
