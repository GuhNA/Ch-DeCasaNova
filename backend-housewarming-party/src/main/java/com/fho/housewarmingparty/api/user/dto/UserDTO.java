package com.fho.housewarmingparty.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String name;

    @NotNull
    @Email
    @Size(max = 320)
    private String email;

    @NotBlank
    @Size(min = 4)
    private String password;

    @NotNull
    @NotBlank
    private String pixKey;
}
