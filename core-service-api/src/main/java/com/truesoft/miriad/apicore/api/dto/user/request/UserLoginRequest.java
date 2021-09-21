package com.truesoft.miriad.apicore.api.dto.user.request;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String login;
    private String password;
}
