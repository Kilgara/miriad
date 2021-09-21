package com.truesoft.miriad.apicore.api.dto.user.response;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String url;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
}
