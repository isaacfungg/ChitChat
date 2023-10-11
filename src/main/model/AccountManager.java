package model;

import java.util.ArrayList;

/**
 * Keeps track of all the accounts and
 * make sure there's no duplicates
 */
public class AccountManager {

    private ArrayList<Account> accountList = new ArrayList<>();
    private boolean isLoggedIn;
    private Account account;

    //Modifies: this
    //Effects: Sets isLoggedIn as false
    public AccountManager() {
        isLoggedIn = false;
    }

    public ArrayList<Account> getAccountList() {
        return this.accountList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    //Modifies: this
    //Effects: isLoggedIn becomes true
    public void logIn() {
        this.isLoggedIn = true;
    }

    //Modifies: this
    //Effects: isLoggedIn becomes false
    public void logOut() {
        this.isLoggedIn = false;
    }

    //Modifies: this
    //Effects: If the user enters the correct password
    // returns true and false if it doesn't
    public boolean checkPassword(String password) {
        return this.account.getPassword().equals(password);
    }

    //Modifies: this
    //Effects: adds account to accountList
    public void addAccount(Account tempAccount) {
        this.accountList.add(tempAccount);
    }

    //Effects: Checks the accountList if there is
    // a username that exists with that name and
    // returns a boolean statement accordingly
    public boolean usernameIsTaken(String username) {
        for (Account tempAccount: accountList) {
            if (tempAccount.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    //Modifies: this
    //Effects: If there is an account with
    //the same name as username then this.account
    //gets set to the one with the same username
    public void findAccount(String userName) {
        for (Account account : accountList) {
            if (account.getUserName().equals(userName)) {
                this.account = account;
            }
        }
    }

    //Effects: Suggests a new username that
    // is similar to the one they had entered
    public String suggestUsernameVariation1(String username) {
        String suggestion1 = username;

        while (usernameIsTaken(suggestion1)) {
            suggestion1 += (int) (Math.random() * 10);
        }

        return suggestion1;
    }

    //Effects: Suggests a new username that
    // is similar to the one they had entered
    public String suggestUsernameVariation2(String username) {
        String suggestion2 = username;

        while (usernameIsTaken(suggestion2)) {
            suggestion2 = username.charAt(0) + suggestion2;
        }

        return suggestion2;
    }


    //Effects: Checks if the username is valid
    // by seeing if it contains specific characters
    public boolean isValidUsername(String username) {
        return !username.contains(" ") && !username.contains(",") && !username.contains("/");
    }

}
