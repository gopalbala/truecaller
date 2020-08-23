# True caller

## Design True caller / WhosCall / Showcaller

Come up with application that can perform  
* Caller identification  
* Call blocking  
* Spam Detection
* Store users contacts  
* Search for users contacts by name and number 

### Use cases

1. Users should be able to register
2. Users should be able to add contacts
3. Users should be able to import contacts  
4. Users should be able to block contacts
5. Users should be able to report spam
6. Users should be able to unblock numbers
7. Users should be notified when suspected junk caller calls
8. Users should be able to identify caller when call comes
9. Users should be able to upgrade to premium plans
10. Users should be able to search for contacts by name 
11. Users should be able to search for contacts by number
12. Users should be able to add business 
13. Post registration and addition of contacts register with global directory.
14. Users should be able to search from global directory

### Credits
Orestes-Bloomfilter - https://github.com/Baqend/Orestes-Bloomfilter  
Guava BloomFilter  

### Test cases
* Test case 1: Create user and register
* Test case 2: Add contacts to user
* Test case 3: check added contacts count
* Test case 4: search for contacts by name
* Test case 5: search for contacts by name
* Test case 6a: search for contacts by phone
* Test case 6b: search for contacts by phone
* Test case 6c: search for contacts by phone
* Test case 7: Block a number
* Test case 8: should not receive call from blocked caller
* Test case 9: Unblock number
* Test case 10: should receive call from un blocked caller
* Test case 11: Should be able to report spam to global list
* Test case 12: Should be able to block global spammers
* Test case 13: Add contacts to user
* Test case 14: adding business to contact
* Test case 15: getting contacts from global