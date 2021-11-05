package com.truesoft.miriad.apicore.api.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDto {

    @NotBlank
    @Size(min = 6, max = 50)
    private String login;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
