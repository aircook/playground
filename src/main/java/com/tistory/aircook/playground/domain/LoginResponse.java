package com.tistory.aircook.playground.domain;

import lombok.Data;

@Data
public class LoginResponse {

    private Integer id;

    private String userid;

    private String password;

    private String role;

}
