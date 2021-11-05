package com.truesoft.miriad.backofficeservice.auth.service;

import com.truesoft.miriad.apicore.api.dto.user.response.UserDto;

public interface AuthService {

    UserDto login(String login, String password);
}
