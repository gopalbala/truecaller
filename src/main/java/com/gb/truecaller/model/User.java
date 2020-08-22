package com.gb.truecaller.model;

import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactsExceededException;
import lombok.Getter;
import lombok.Setter;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class User extends Account {
    private CountingBloomFilter<String> blockedContacts
            = new FilterBuilder(1000, .01)
            .buildCountingBloomFilter();

    private Map<String, User> contacts = new HashMap<>(10000);

    public void addConcat(User user) throws ContactsExceededException {
        if (contacts.size() >= 10000)
            throw new ContactsExceededException("Default contact size exceeded, Upgrade to premium");
        this.contacts.putIfAbsent(user.getPhoneNumber(), user);
    }

    public void blockNumber(String number) throws BlockLimitExceededException {
        if (blockedContacts.getSize() > 1000)
            throw new BlockLimitExceededException("Upgrade account to increase the limit");
        blockedContacts.add(number);
    }

    public void unblockNumber(String number) {
        blockedContacts.remove(number);
    }

    public void reportSpam(String number, String reason) {

    }
}
