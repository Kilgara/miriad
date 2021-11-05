package com.truesoft.miriad.coreservice.user.controller;

import java.util.Arrays;
import java.util.UUID;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truesoft.miriad.apicore.api.dto.ErrorResponse;
import com.truesoft.miriad.apicore.api.dto.user.CredentialsDto;
import com.truesoft.miriad.apicore.api.dto.user.UserDto;
import com.truesoft.miriad.apicore.api.dto.user.request.UserCreateRequest;
import com.truesoft.miriad.apicore.api.dto.user.request.UserUpdateRequest;
import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.exception.RoleNotFoundException;
import com.truesoft.miriad.coreservice.user.exception.UserAlreadyExistsException;
import com.truesoft.miriad.coreservice.user.exception.UserNotFoundException;
import com.truesoft.miriad.coreservice.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users/0000")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public UserDto create(@RequestBody UserCreateRequest request) {
        final User user = userService.create(UserMapper.userFrom(request));

        return UserMapper.userDtoFrom(user);
    }

    @PostMapping("/{uuid}")
    public UserDto update(@PathVariable("uuid") UUID uuid, @RequestBody @Validated UserUpdateRequest request) {
        final User userToUpdate = UserMapper.userFrom(request);
        final User user = userService.update(uuid, userToUpdate);

        return UserMapper.userDtoFrom(user);
    }

    @GetMapping("/{uuid}")
    public UserDto getUser(@PathVariable("uuid") UUID uuid) {
        return UserMapper.userDtoFrom(userService.get(uuid));
    }

    @GetMapping("/findBy")
    public UserDto findByEmail(@RequestParam("email") @Email String email) {
        return UserMapper.userDtoFrom(userService.findByEmail(email));
    }

    @PostMapping("/findByCredentials")
    public UserDto findByCredentials(@RequestBody CredentialsDto credentials) {
        final User user = userService.findByEmailAndPassword(credentials.getLogin(), credentials.getPassword());

        return UserMapper.userDtoFrom(user);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException(RuntimeException e) {
        log.debug("No such user", e);

        final ErrorResponse response = ErrorResponse.builder()
            .code("user.not.found")
            .message(Arrays.asList(e.getMessage()))
            .build();

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(RuntimeException e) {
        log.debug("Such user already exists", e);

        final ErrorResponse response = ErrorResponse.builder()
            .code("user.exists")
            .message(Arrays.asList(e.getMessage()))
            .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleRoleNotFoundException(RuntimeException e) {
        log.warn("One of the roles in user roles list is invalid", e);

        final ErrorResponse response = ErrorResponse.builder()
            .code("role.not.found")
            .message(Arrays.asList(e.getMessage()))
            .build();

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
