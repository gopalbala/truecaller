package com.gb.truecaller.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Account {
    private Business business;
}
