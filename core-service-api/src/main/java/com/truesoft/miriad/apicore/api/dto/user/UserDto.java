package com.truesoft.miriad.apicore.api.dto.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {

    private String url;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserRoleDto> roles;
}
