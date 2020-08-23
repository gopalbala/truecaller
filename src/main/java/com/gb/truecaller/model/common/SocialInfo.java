package com.gb.truecaller.model.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SocialInfo {
    private Map<SocialProfileType, String> socialInfo = new HashMap<>();
}
