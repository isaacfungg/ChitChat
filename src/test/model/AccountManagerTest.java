package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountManagerTest {

    private AccountManager accountManager = new AccountManager();
    private Account a1;
    private Account a2;
    private Account a3;

    @BeforeEach
    void runBefore() {
       a1 = new Account("isaac1", "password");
       a2 = new Account("isaac2", "password");
       a3 = new Account("isaac3", "password");
    }

    @Test
    void testCreateAccount() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);

        ArrayList<Account> accountList = accountManager.getAccountList();
        assertTrue(accountList.contains(a1));
        assertTrue(accountList.contains(a2));
        assertTrue(accountList.contains(a3));
    }

    @Test
    void testCheckPassword() {
        accountManager.addAccount(a1);
        accountManager.setAccount(a1);
        assertFalse(accountManager.checkPassword("notPassword"));
        assertTrue(accountManager.checkPassword("password"));
    }

    @Test
    void testLogIn() {
        accountManager.addAccount(a1);
        accountManager.setAccount(a1);

        accountManager.logIn();
        assertTrue(accountManager.getIsLoggedIn());

        accountManager.logOut();
        assertFalse(accountManager.getIsLoggedIn());
    }

    @Test
    void testUsernameIsTaken() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);
        accountManager.setAccount(a1);

        assertTrue(accountManager.usernameIsTaken("isaac1"));
        assertTrue(accountManager.usernameIsTaken("isaac2"));
        assertTrue(accountManager.usernameIsTaken("isaac3"));
        assertFalse((accountManager.usernameIsTaken("isaac")));
    }

    @Test
    void testFindAccount() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);
        accountManager.setAccount(a1);

        accountManager.findAccount("isaac3");
        Account a4 = accountManager.getAccount();
        assertEquals(a4, a3);
    }

    @Test
    void testFindAccountNotFound() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);
        accountManager.setAccount(a1);

        accountManager.findAccount("isaac10");
        Account a4 = accountManager.getAccount();
        assertFalse(a3.equals(a4));
    }

    @Test
    void testSuggestUsernameVariation1() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);

        String username = accountManager.suggestUsernameVariation1(a1.getUserName());
        assertFalse(accountManager.usernameIsTaken(username));

        String username2 = accountManager.suggestUsernameVariation1(a2.getUserName());
        assertFalse(accountManager.usernameIsTaken(username2));

        String username3 = accountManager.suggestUsernameVariation1(a3.getUserName());
        assertFalse(accountManager.usernameIsTaken(username3));
    }

    @Test
    void testSuggestUsernameVariation2() {
        accountManager.addAccount(a1);
        accountManager.addAccount(a2);
        accountManager.addAccount(a3);

        String username = accountManager.suggestUsernameVariation2(a1.getUserName());
        assertFalse(accountManager.usernameIsTaken(username));

        String username2 = accountManager.suggestUsernameVariation2(a2.getUserName());
        assertFalse(accountManager.usernameIsTaken(username2));

        String username3 = accountManager.suggestUsernameVariation2(a3.getUserName());
        assertFalse(accountManager.usernameIsTaken(username3));
    }

    @Test
    void testIsValidUsername() {
        assertFalse(accountManager.isValidUsername("user name "));
        assertFalse(accountManager.isValidUsername("user name"));
        assertFalse(accountManager.isValidUsername(" username"));

        assertFalse(accountManager.isValidUsername("username,"));
        assertFalse(accountManager.isValidUsername("user,name"));
        assertFalse(accountManager.isValidUsername(",username"));

        assertFalse(accountManager.isValidUsername("username/"));
        assertFalse(accountManager.isValidUsername("user/name"));
        assertFalse(accountManager.isValidUsername("/username"));

        assertFalse(accountManager.isValidUsername("user, name"));
        assertFalse(accountManager.isValidUsername("user name/"));
    }
}

