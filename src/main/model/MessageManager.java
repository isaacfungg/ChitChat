package model;

import java.util.ArrayList;

public class MessageManager {

    private ArrayList<Message> messageList; //Contains all messages
    private ArrayList<Message> userInbox;   //Contains all messages for a specific user
    private Account account;

    //Modifies: this
    //Effects: Create an empty list of all messages and another
    // for user inbox and assigns account to this
    public MessageManager(Account account) {
        this.messageList = new ArrayList<>();
        this.userInbox = new ArrayList<>();
        this.account = account;

    }

    //Modifies: this
    //Effects: Adds message to messageList
    public void addMessage(Message message) {
        this.messageList.add(message);
    }

    public ArrayList<Message> getMessageList() {
        return this.messageList;
    }

    public ArrayList<Message> getUserInbox() {
        loadUserInbox();
        return userInbox;
    }

    //Modifies: this
    //Effects: creates a new list of the names in the
    // user inbox and also refreshes userInbox for any updates
    public ArrayList<String> getUserInboxNames() {
        loadUserInbox();
        ArrayList<String> userInboxNames = new ArrayList<>();

        for (Message message : userInbox) {
            if (!userInboxNames.contains(message.getSender())) {
                userInboxNames.add(message.getSender());
            }
        }

        return userInboxNames;
    }

    //Requires: username is contained in userInboxNames
    //Modifies: this
    //Effects: Creates a new list of messages from a specific
    //user that has been requested and refreshes userInbox for updates
    public ArrayList<Message> getUserMessages(String username) {
        ArrayList<Message> inboxMessages = new ArrayList<>();
        loadUserInbox();

        for (Message message : userInbox) {
            if (message.getSender().equals(username)) {
                inboxMessages.add(message);
            }
        }
        return inboxMessages;
    }

    //Modifies: this
    //Effects: Updates userInbox if there are any new messages
    //that have been added since the last time this was run
    private void loadUserInbox() {
        String username = account.getUserName();

        //Finds which messages are for this user
        for (Message message : messageList) {
            if (username.equals(message.getReceiver()) && !userInbox.contains(message)) {
                userInbox.add(message);
            }
        }
    }
}
