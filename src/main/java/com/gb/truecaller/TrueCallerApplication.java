package com.gb.truecaller;

import com.gb.truecaller.exception.BlockLimitExceededException;
import com.gb.truecaller.exception.ContactsExceededException;
import com.gb.truecaller.model.*;
import com.gb.truecaller.model.common.Contact;
import com.gb.truecaller.model.common.GlobalSpam;
import com.gb.truecaller.model.common.Tag;

import java.util.List;

public class TrueCallerApplication {
    public static void main(String[] args) throws ContactsExceededException, BlockLimitExceededException {

        //Test case 1: Create user and register
        Account account1 = new User();
        account1.register(UserCategory.FREE,"u1","pwd",
                "u1@email.com","6826999256", "91", "u1");

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
        account1.addConcat(new User("7917949575","a.Veena"));

        //Test case 3: check added contacts count
        System.out.println(account1.getContacts().size());

        //Test case 4: search for contacts by name
        System.out.println("***** Getting name with prefix par ******");
        List<String> names = account1.getContactTrie().allWordsWithPrefix("par");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 5: search for contacts by name
        System.out.println("***** Getting name with prefix go ******");
        names = account1.getContactTrie().allWordsWithPrefix("go");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6a: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 9 *****");
        names = account1.getContactTrie().allWordsWithPrefix("9");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6b: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 72 *****");
        names = account1.getContactTrie().allWordsWithPrefix("72");
        for (String n: names) {
            System.out.println(n);
        }

        //Test case 6c: search for contacts by phone
        System.out.println("***** Getting numbers with prefix 6610448270 ***");
        names = account1.getContactTrie().allWordsWithPrefix("6610448270");
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

        Account account2 = new User();
        account2.register(UserCategory.FREE,"u2","pwd",
                "u2@email.com","6826999256", "91", "u2");

        // Test case 13: Add contacts to user
        account2.addConcat(new User("7373048205","anil"));
        account2.addConcat(new User("7373113132","aravind", "hs"));
        account2.addConcat(new User("7373292767","vimal"));
        account2.addConcat(new User("7373358224","smitha"));
        account2.addConcat(new User("7373441841","alamelu"));
        account2.addConcat(new User("7373514930","alagappan"));
        account2.addConcat(new User("7373659939","aruna"));
        account2.addConcat(new User("7373782605","seetha"));
        account2.addConcat(new User("7373860476","rama", "kumar"));
        account2.addConcat(new User("7373954330","ramganesh"));
        account2.addConcat(new User("7071043159","muruga"));
        account2.addConcat(new User("7071115445","kandha"));
        account2.addConcat(new User("7071215591","siva"));

        //Test case 14: adding business to contact
        Business business = new Business("u2 Solutions", Tag.SERVICES);
        business.setBusinessDescription("Software product development");
        Contact contact = new Contact("91","9945012000","contact@u2sol.com");
        business.setContact(contact);
        business.setBusinessSize(BusinessSize.LARGE);

        account1.setBusiness(business);

        //Test case 15: getting contacts from global
        System.out.println("******** Case1 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("u2");
        for (String s: names){
            System.out.println(s);
        }

        System.out.println("******** Case2 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("ram");
        for (String s: names){
            System.out.println(s);
        }

        System.out.println("******** Case3 Searching from global directory ******");
        names = GlobalContacts.INSTANCE.getContactTrie().allWordsWithPrefix("a");
        for (String s: names){
            System.out.println(s);
        }
    }
}
