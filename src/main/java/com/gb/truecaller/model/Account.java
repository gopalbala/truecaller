package com.gb.truecaller.model;

import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactDoesNotExistsException;
import com.gb.truecaller.exception.ContactsExceededException;
import com.gb.truecaller.model.common.Contact;
import com.gb.truecaller.model.common.PersonalInfo;
import com.gb.truecaller.model.common.SocialInfo;
import com.gb.truecaller.model.common.Tag;
import lombok.Getter;
import lombok.Setter;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.time.LocalDateTime;
import java.util.*;

import static com.gb.truecaller.model.common.Constant.*;

@Getter
@Setter
public abstract class Account {
    private String id;
    private String phoneNumber;
    private String userName;
    private String password;
    private LocalDateTime lastAccessed;
    private Tag tag;
    private Contact contact;
    private PersonalInfo personalInfo;
    private SocialInfo socialInfo;
    private Business business;
    private UserType userType;
    private Map<String, User> contacts;
    private CountingBloomFilter<String> blockedContacts;
    private Set<String> blockedSet;

    public Account() {
    }

    public Account(String phoneNumber, String firstName) {
        this.phoneNumber = phoneNumber;
        this.personalInfo = new PersonalInfo();
        this.personalInfo.setFirstName(firstName);
    }

    public Account(String phoneNumber, String firstName, String lastName) {
        this(phoneNumber,firstName);
        this.personalInfo.setLastName(lastName);
    }

    public abstract void register(UserType userType, String userName, String password,
                                  String email, String phoneNumber, String countryCode);
    public abstract void addConcat(User user) throws ContactsExceededException;
    public abstract void removeContact(String number) throws ContactDoesNotExistsException;
    public abstract void blockNumber(String number) throws BlockLimitExceededException;
    public abstract void unblockNumber(String number);
    public abstract void reportSpam(String number, String reason);
    public abstract void upgrade(UserType userType);
    public abstract boolean isBlocked(String number);
    public abstract boolean canReceive(String number);

}