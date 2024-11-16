package com.fho.housewarmingparty.security.authentication.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor //needs default constructor for JSON Parsing
@Data
public class JwtRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;
}