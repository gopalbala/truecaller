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

    public Account( UserType userType, String userName, String password,
                   String email, String phoneNumber, String countryCode) {
        this.id = UUID.randomUUID().toString();
        this.userType = userType;
        this.userName = userName;
        this.password = password;
        this.contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phoneNumber);
        contact.setCountryCode(countryCode);
        init(userType);
    }

    private void init(UserType userType) {
        switch (userType){
            case FREE:
                contacts = new HashMap<>(MAX_FREE_USER_CONTACTS);
                blockedContacts = new FilterBuilder(MAX_FREE_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter();
                blockedSet = new HashSet<>(MAX_FREE_USER_BLOCKED_CONTACTS);
                break;
            case GOLD:
                contacts = new HashMap<>(MAX_GOLD_USER_CONTACTS);
                blockedContacts = new FilterBuilder(MAX_GOLD_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter();
                blockedSet = new HashSet<>(MAX_GOLD_USER_BLOCKED_CONTACTS);
                break;

            case PLATINUM:
                contacts = new HashMap<>(MAX_PLATINUM_USER_CONTACTS);
                blockedContacts = new FilterBuilder(MAX_PLATINUM_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter();
                blockedSet = new HashSet<>(MAX_PLATINUM_USER_BLOCKED_CONTACTS);
                break;
        }
    }

    public abstract void addConcat(User user) throws ContactsExceededException;
    public abstract void removeContact(String number) throws ContactDoesNotExistsException;
    public abstract void blockNumber(String number) throws BlockLimitExceededException;
    public abstract void unblockNumber(String number);
    public abstract void reportSpam(String number, String reason);
    public abstract void upgrade(UserType userType);
    public abstract boolean isBlocked(String number);
    public abstract boolean canReceive(String number);

}