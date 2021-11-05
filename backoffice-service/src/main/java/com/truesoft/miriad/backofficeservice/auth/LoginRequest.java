package com.truesoft.miriad.backofficeservice.auth;

import lombok.Data;

@Data
public class LoginRequest {

    private String login;
    private String password;
}
