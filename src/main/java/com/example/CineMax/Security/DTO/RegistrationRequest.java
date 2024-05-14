package com.example.CineMax.Security.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
    @JsonProperty("isAdmin")
    private boolean isAdmin;

}
