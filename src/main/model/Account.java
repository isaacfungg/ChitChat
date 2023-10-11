package model;

/**
 * Allows the user to create an account
 * with a username and password
 */
public class Account {

    private String userName;
    private String password;

    //Modifies: this
    //Effects: Initializes account with a specific
    // username and password
    public Account(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
