package com.truesoft.miriad.backofficeservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truesoft.miriad.apicore.api.CoreServiceClient;
import com.truesoft.miriad.apicore.api.dto.user.request.UserLoginRequest;
import com.truesoft.miriad.apicore.api.dto.user.response.UserDto;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CoreServiceClient coreClient;

    @Override
    public UserDto login(String login, String password) {
        final UserLoginRequest loginRequest = UserLoginRequest.builder()
            .login(login)
            .password(password)
            .build();

        UserDto userDto = coreClient.login(loginRequest);
    }
}
