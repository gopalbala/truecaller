package com.gb.truecaller.model;

import com.gb.truecaller.model.common.PersonalInfo;
import com.gb.truecaller.model.common.SocialInfo;
import com.gb.truecaller.model.common.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Business {
    private String businessName;
    private String businessDescription;
    private Tag tag;
    private BusinessSize businessSize;
    private Map<Days, OperatingingHours> openHours;
    private Contact contact;
    private PersonalInfo personalInfo;
    private SocialInfo socialInfo;
}
