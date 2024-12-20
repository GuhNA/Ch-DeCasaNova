package com.fho.housewarmingparty.security.authentication.dto;

import java.io.Serial;
import java.io.Serializable;

public class JwtResponse implements Serializable {

    @Serial
	private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}