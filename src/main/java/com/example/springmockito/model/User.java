package com.example.springmockito.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class User {
    private final String login;
    private final String password;
}
