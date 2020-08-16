package com.gb.truecaller.model;

import com.gb.truecaller.model.common.Address;
import com.gb.truecaller.model.common.PersonalInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private String phone;
    private String email;
    private Address address;
    private PersonalInfo personalInfo;
}
