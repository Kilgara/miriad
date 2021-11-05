package com.truesoft.miriad.coreservice.user.controller;

import java.util.Set;
import java.util.stream.Collectors;

import com.truesoft.miriad.apicore.api.dto.user.UserDto;
import com.truesoft.miriad.apicore.api.dto.user.UserRoleDto;
import com.truesoft.miriad.apicore.api.dto.user.request.UserCreateRequest;
import com.truesoft.miriad.apicore.api.dto.user.request.UserUpdateRequest;
import com.truesoft.miriad.coreservice.user.domain.Role;
import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.domain.UserRole;

import static java.util.Objects.nonNull;

public class UserMapper {

    private static final String USER_BASE = "/user/0000/";

    public static UserDto userDtoFrom(User user) {
        return UserDto.builder()
            .url(USER_BASE.concat(user.getUuid()))
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .roles(user.getRoles().stream()
                .map(Role::getName)
                .map(UserRole::name)
                .map(name -> Enum.valueOf(UserRoleDto.class, name))
                .collect(Collectors.toSet()))
            .build();
    }

    public static User userFrom(UserCreateRequest request) {
        final User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRoles(from(request.getRoles()));

        return user;
    }

    public static User userFrom(UserUpdateRequest request) {
        final User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setRoles(nonNull(request.getRoles()) ? from(request.getRoles()) : null);

        return user;
    }

    private static Set<Role> from(Set<String> roles) {
        return roles.stream()
            .map(UserRole::valueOf)
            .map(Role::new)
            .collect(Collectors.toSet());
    }
}
