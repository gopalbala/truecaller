package com.gb.truecaller.model;

import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactDoesNotExistsException;
import com.gb.truecaller.exception.ContactsExceededException;
import com.gb.truecaller.model.common.GlobalSpam;
import com.gb.truecaller.model.tries.ContactTrie;

import static com.gb.truecaller.model.common.Constant.*;


public class User extends Account {

    private ContactTrie contactTrie = ContactTrie.CONTACT_TRIE;

    public User(UserType userType, String userName, String password, String email, String phoneNumber, String countryCode) {
        super(userType, userName, password, email, phoneNumber, countryCode);
    }

    public void addConcat(User user) throws ContactsExceededException {
        checkAddUser();
        getContacts().putIfAbsent(user.getPhoneNumber(), user);
        contactTrie.insert(user.getPhoneNumber());
        contactTrie.insert(user.getPersonalInfo().getFirstName());
    }

    public void removeContact(String number) throws ContactDoesNotExistsException {
        User contact = getContacts().get(number);
        if (contact == null)
            throw new ContactDoesNotExistsException("Contact does not exist");
        getContacts().remove(number);
        contactTrie.delete(number);
        contactTrie.delete(contact.getPersonalInfo().getFirstName());
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
        GlobalSpam.INSTANCE.reportSpam(number);
    }

    private void checkAddUser() throws ContactsExceededException {
        switch (this.getUserType()){
            case FREE:
                if (this.getContacts().size()>= MAX_FREE_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case GOLD:
                if (this.getContacts().size()>= MAX_GOLD_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
            case PLATINUM:
                if (this.getContacts().size()>= MAX_PLATINUM_USER_CONTACTS)
                    throw new ContactsExceededException("Default contact size exceeded");
        }
    }

    private void checkBlockUser() throws BlockLimitExceededException {
        switch (this.getUserType()){
            case FREE:
                if (this.getContacts().size()>= MAX_FREE_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case GOLD:
                if (this.getContacts().size()>= MAX_GOLD_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
            case PLATINUM:
                if (this.getContacts().size()>= MAX_PLATINUM_USER_BLOCKED_CONTACTS)
                    throw new BlockLimitExceededException("Exceeded max contacts to be blocked");
        }
    }
}
