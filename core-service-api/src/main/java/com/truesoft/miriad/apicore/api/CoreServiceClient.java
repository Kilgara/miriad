package com.truesoft.miriad.apicore.api;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.truesoft.miriad.apicore.api.dto.user.request.UserCreateRequest;
import com.truesoft.miriad.apicore.api.dto.user.request.UserLoginRequest;
import com.truesoft.miriad.apicore.api.dto.user.request.UserUpdateRequest;
import com.truesoft.miriad.apicore.api.dto.user.response.UserDto;

@FeignClient("core-service")
public interface CoreServiceClient {

    @GetMapping(value = "/api/users/0000/${uuid}")
    UserDto getUser(@PathVariable("uuid") UUID uuid);

    @GetMapping(value = "/api/users/0000/findBy")
    UserDto findByEmail(@RequestParam("email") String email);

    @PutMapping(value = "/api/users/0000/")
    UserDto create(@RequestBody UserCreateRequest request);

    @PostMapping(value = "/api/users/0000/${uuid}")
    UserDto create(@PathVariable("uuid") UUID uuid, @RequestBody UserUpdateRequest request);

    @PostMapping(value = "/api/users/0000/login")
    UserDto login(@RequestBody UserLoginRequest userLoginRequest);
}
