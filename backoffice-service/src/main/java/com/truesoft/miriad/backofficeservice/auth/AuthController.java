package com.truesoft.miriad.backofficeservice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truesoft.miriad.apicore.api.dto.user.response.UserDto;
import com.truesoft.miriad.backofficeservice.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        UserDto userDto = authService.login(loginRequest.getLogin(), loginRequest.getPassword());
    }
}
