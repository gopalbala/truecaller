package com.gb.truecaller;

import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactsExceededException;
import com.gb.truecaller.model.Account;
import com.gb.truecaller.model.User;
import com.gb.truecaller.model.UserType;
import com.gb.truecaller.model.common.GlobalSpam;
import com.gb.truecaller.model.tries.ContactTrie;

import java.util.List;

public class TrueCallerApplication {
    public static void main(String[] args) throws ContactsExceededException, BlockLimitExceededException {

        //Test case 1: Create user and register
        Account account1 = new User();
        account1.register(UserType.FREE,"u1","pwd",
                "u1@email.com","6826999256", "91");

        // Test case 2: Add contacts to user
        account1.addConcat(new User("9140107431","mahadev"));
        account1.addConcat(new User("8558101117","govind", "hs"));
        account1.addConcat(new User("8723937942","gopala"));
        account1.addConcat(new User("7070063864","mahesha"));
        account1.addConcat(new User("6610448270","parvathi"));
        account1.addConcat(new User("7336175457","parameshwari"));
        account1.addConcat(new User("7202250272","narayan"));
        account1.addConcat(new User("7859999997","lakshmi"));
        account1.addConcat(new User("9653498522","ganesh", "kumar"));
        account1.addConcat(new User("7277115893","ganapathy"));
        account1.addConcat(new User("9495010564","Bhrama"));
        account1.addConcat(new User("9844296241","Saraswathi"));
        account1.addConcat(new User("7917949575","Veena"));

        //Test case 3: check added contacts count
        System.out.println(account1.getContacts().size());

        //Test case 4: search for contacts by name
        System.out.println("***** Getting name with prefix par ******");
        List<String> names = ContactTrie.CONTACT_TRIE.allWordsWithPrefix("par");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 5: search for contacts by name
        System.out.println("***** Getting name with prefix go ******");
        names = ContactTrie.CONTACT_TRIE.allWordsWithPrefix("go");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6a: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 9 *****");
        names = ContactTrie.CONTACT_TRIE.allWordsWithPrefix("9");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6b: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 72 *****");
        names = ContactTrie.CONTACT_TRIE.allWordsWithPrefix("72");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6c: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 6610448270 ***");
        names = ContactTrie.CONTACT_TRIE.allWordsWithPrefix("6610448270");
        for (String n: names) {
            System.out.println(n);
        }

        System.out.println("***** Adding 3949345003, 4953904850, 2782348999 numbers *****");
        account1.addConcat(new User("3949345003","Blocked caller1"));
        account1.addConcat(new User("4953904850","Blocked caller2"));
        account1.addConcat(new User("2782348999","Junk caller3"));

        System.out.println("***** Blocking 3949345003, 4953904850 numbers *****");
        //Test case 7: Block a number
        account1.blockNumber("3949345003");
        account1.blockNumber("4953904850");
        System.out.println(account1.isBlocked("3949345003"));

        //Test case 8: should not receive call from blocked caller
        System.out.println( account1.canReceive("3949345003"));

        System.out.println("*****Number 3949345003 is unblocked should be un blocked***");
        //Test case 9: Unblock number
        account1.unblockNumber("3949345003");
        System.out.println(account1.isBlocked("3949345003"));

        //Test case 10: should receive call from un blocked caller
        System.out.println("*****Should be able to receive call from un blocked but not blocked one***");
        System.out.println( account1.canReceive("3949345003"));
        System.out.println(account1.canReceive("4953904850"));

        //Test case 11: Should be able to report spam to global list
        System.out.println("***** reporting spam *****");
        account1.reportSpam("2782348999","spam banker");
        account1.reportSpam("2782348999","spam banker");

        System.out.println("*** 2782348999 Number should be in global spam ***");
        System.out.println(GlobalSpam.INSTANCE.isGlobalSpam("2782348999"));

        //Test case 12: Should be able to block global spammers
        System.out.println("*** 2782348999 Number should be in global blockedList ***");
        System.out.println(GlobalSpam.INSTANCE.isGloballyBlocked("2782348999"));
    }
}
