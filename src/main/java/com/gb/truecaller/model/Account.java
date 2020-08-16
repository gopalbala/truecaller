package com.gb.truecaller.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Account {
    private String id;
    private String email;
    private String userName;
    private String password;
    private LocalDateTime lastAccessed;
    private Contact contact;
}