package com.gb.truecaller.model;

import com.gb.truecaller.GlobalContacts;
import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactDoesNotExistsException;
import com.gb.truecaller.exception.ContactsExceededException;
import com.gb.truecaller.model.common.Contact;
import com.gb.truecaller.model.common.GlobalSpam;
import com.gb.truecaller.model.common.PersonalInfo;
import com.gb.truecaller.model.tries.ContactTrie;
import orestes.bloomfilter.FilterBuilder;

import java.util.*;

import static com.gb.truecaller.model.common.Constant.*;


public class User extends Account {

    public User() {
        setContactTrie(new ContactTrie());
    }

    public User(String phoneNumber, String firstName) {
        super(phoneNumber, firstName);
    }

    public User(String phoneNumber, String firstName, String lastName) {
        super(phoneNumber, firstName, lastName);
    }

    public void register(UserCategory userCategory, String userName, String password, String email,
                         String phoneNumber, String countryCode, String firstName) {
        setId(UUID.randomUUID().toString());
        setUserCategory(userCategory);
        setUserName(userName);
        setPassword(password);
        setContact(new Contact(phoneNumber, email, countryCode));
        setPersonalInfo(new PersonalInfo(firstName));
        init(userCategory);
        insertToTries(phoneNumber, firstName);
    }

    private void init(UserCategory userCategory) {
        switch (userCategory) {
            case FREE:
                setContacts(new HashMap<>(MAX_FREE_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_FREE_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_FREE_USER_BLOCKED_CONTACTS));
                break;
            case GOLD:
                setContacts(new HashMap<>(MAX_GOLD_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_GOLD_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_GOLD_USER_BLOCKED_CONTACTS));
                break;

            case PLATINUM:
                setContacts(new HashMap<>(MAX_PLATINUM_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_PLATINUM_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_PLATINUM_USER_BLOCKED_CONTACTS));
                break;
        }
    }

    public void addConcat(User user) throws ContactsExceededException {
        checkAddUser();
        getContacts().putIfAbsent(user.getPhoneNumber(), user);
        insertToTries(user.getPhoneNumber(), user.getPersonalInfo().getFirstName());
    }

    public void removeContact(String number) throws ContactDoesNotExistsException {
        User contact = getContacts().get(number);
        if (contact == null)
            throw new ContactDoesNotExistsException("Contact does not exist");
        getContacts().remove(number);
        getContactTrie().delete(number);
        getContactTrie().delete(contact.getPersonalInfo().getFirstName());
    }

    public void blockNumber(String number) throws BlockLimitExceededException {
        checkBlockUser();
        getBlockedContacts().add(number);
    }

    public void unblockNumber(String number) {
        getBlockedContacts().remove(number);
    }

    public void reportSpam(String number, String reason) {
        getBlockedContacts().add(number);
        GlobalSpam.INSTANCE.reportSpam(number, this.getPhoneNumber(), reason);
    }

    public void upgrade(UserCategory userCategory) {
        int count = 0;
        int blockedCount = 0;
        switch (userCategory) {
            case GOLD:
                count = MAX_GOLD_USER_CONTACTS;
                blockedCount = MAX_GOLD_USER_BLOCKED_CONTACTS;
                break;
            case PLATINUM:
                count = MAX_PLATINUM_USER_CONTACTS;
                blockedCount = MAX_PLATINUM_USER_BLOCKED_CONTACTS;
                break;
        }
        upgradeContacts(count);
        upgradeBlockedContact(blockedCount);
    }

    public boolean isBlocked(String number) {
        return getBlockedContacts().contains(number);
    }

    public boolean canReceive(String number) {
        return !isBlocked(number) &&
                !GlobalSpam.INSTANCE.isGlobalSpam(number);
    }

    @Override
    public boolean importContacts(List<User> users) {
        for (User user : users) {
            try {
                addConcat(user);
            } catch (ContactsExceededException cee) {
                System.out.println("Some of the contact could not be imported as limit exceeded");
                return false;
            }
        }
        return true;
    }

    private void upgradeBlockedContact(int blockedCount) {
        setBlockedContacts(new FilterBuilder(blockedCount, .01)
                .buildCountingBloomFilter());
        Set<String> upgradedSet = new HashSet<>();
        for (String blocked : getBlockedSet()) {
            upgradedSet.add(blocked);
            getBlockedContacts().add(blocked);
        }
    }

    private void upgradeContacts(int count) {
        Map<String, User> upgradedContacts = new HashMap<>(count);
        for (Map.Entry<String, User> entry : getContacts().entrySet()) {
            upgradedContacts.putIfAbsent(entry.getKey(), entry.getValue());
        }
        setContacts(upgradedContacts);
    }

    private void checkAddUser() throws ContactsExceededException {
        switch (this.getUserCategory()) {
            case FREE:
                if (this.getContacts().size() >= MAX_FREE_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case GOLD:
                if (this.getContacts().size() >= MAX_GOLD_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case PLATINUM:
                if (this.getContacts().size() >= MAX_PLATINUM_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
        }
    }

    private void checkBlockUser() throws BlockLimitExceededException {
        switch (this.getUserCategory()) {
            case FREE:
                if (this.getContacts().size() >= MAX_FREE_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case GOLD:
                if (this.getContacts().size() >= MAX_GOLD_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case PLATINUM:
                if (this.getContacts().size() >= MAX_PLATINUM_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
        }
    }

    private void insertToTries(String phoneNumber, String firstName) {
        getContactTrie().insert(phoneNumber);
        getContactTrie().insert(firstName);
        GlobalContacts.INSTANCE.getContactTrie().insert(phoneNumber);
        GlobalContacts.INSTANCE.getContactTrie().insert(firstName);
    }
}
