package ui;

import model.AccountManager;
import model.Account;
import model.Message;
import model.MessageManager;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {

    private static Scanner input = new Scanner(System.in);
    private AccountManager accountManager = new AccountManager();
    private MessageManager messageManager;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private static final String MESSAGE_FILEPATH = "./data/messageManager.json";
    private static final String ACCOUNT_FILEPATH = "./data/accountManager.json";

    public void run() {
        jsonReader = new JsonReader();
        jsonWriter = new JsonWriter();

        loadAccountManager();
        performUserAccountOptions();
        messageManager = new MessageManager(accountManager.getAccount());
        performUserMessagingOptions();
    }

    public void performUserMessagingOptions() {
        do {
            displayUserMessagingOptions();
            int option = input.nextInt();
            input.nextLine();

            optionCases(option);

        } while (accountManager.getIsLoggedIn());
    }

    private void optionCases(int option) {
        switch (option) {
            case 1:
                sendMessage();
                break;
            case 2:
                displayUserMessages();
                break;
            case 3:
                displayAllUsers();
                break;
            case 4:
                loadMessageManager();
                System.out.println("Loaded messages. ");
                break;
            case 5:
                accountManager.logOut();
                break;
            default:
                System.out.println("Invalid Choice. ");
                break;
        }
    }

    public void displayUserMessagingOptions() {
        System.out.println("---------------------------");
        System.out.println("Send Message (1)");
        System.out.println("Open Message (2)");
        System.out.println("Display Available Users (3)");
        System.out.println("Load Messages (4)");
        System.out.println("Log Out (5)");
        System.out.println("---------------------------");
    }

    public void performUserAccountOptions() {
        do {
            displayUserAccountOptions();
            int option = input.nextInt();
            input.nextLine();

            switch (option) {
                case 1:
                    String username = makeUsername();
                    String password = makePassword();
                    Account tempAccount = new Account(username, password);
                    accountManager.addAccount(tempAccount);
                    saveAccountToFile();
                    break;
                case 2:
                    logIn();
                    break;
                default:
                    System.out.println("Not a valid option");
            }
        } while (!accountManager.getIsLoggedIn());
    }

    private void displayUserAccountOptions() {
        System.out.println("--------------------");
        System.out.println("Create Account (1)");
        System.out.println("Log in (2)");
        System.out.println("--------------------");
        System.out.print("Enter one of the following: ");
    }

    public void logIn() {
        int num = enterUsername();
        if (num != 1) {
            return;
        }
        enterPassword();
    }

    public void displayAllUsers() {
        ArrayList<Account> accounts = accountManager.getAccountList();
        for (Account account: accounts) {
            if (!accountManager.getAccount().getUserName().equals(account.getUserName())) {
                System.out.println(account.getUserName());
            }
        }
    }

    private void loadMessageManager() {
        try {
            this.messageManager = jsonReader.readMessageManager(accountManager.getAccount(), MESSAGE_FILEPATH);
            this.messageManager.loadUserInbox();
        } catch (IOException e) {
            System.out.println("Error loading the data.");
        }
    }

    private void loadAccountManager() {
        try {
            this.accountManager = jsonReader.readAccountManager(ACCOUNT_FILEPATH);
        } catch (IOException e) {
            System.out.println("Unable to load accounts");
        }
    }

    private String findUserMessages() {
        ArrayList<String> userInboxNames = messageManager.getUserInboxNames();
        String username;

        do {
            System.out.println("Type EXIT to leave");
            System.out.print("Enter the user name of the message you want to see: ");
            username = input.next();
            if (!userInboxNames.contains(username)) {
                System.out.println("Username not found");
            }
        } while (!userInboxNames.contains(username) && !username.equals("EXIT"));

        return username;
    }

    private void displayUserMessages() {
        displayUserInbox();
        String sender = findUserMessages();

        if (sender.equals("EXIT")) {
            return;
        }

        ArrayList<Message> messages = messageManager.getUserMessages(sender);
        ArrayList<String> texts;
        for (int i = 0; i < messages.size(); i++) {
            texts = messages.get(i).getMessages();
            System.out.print("Message " + (i + 1) + ": ");
            for (String text : texts) {
                System.out.print(text);
            }
            System.out.println();
        }
        askKeepMessage(messages);
        saveMessageToFile();
    }

    private void askKeepMessage(ArrayList<Message> messages) {
        int index;
        String answer;
        do {
            System.out.println("Would you like to keep any of these messages? (YES) or (NO)");
            answer = input.next();
            if (answer.equalsIgnoreCase("YES")) {
                index = keepMessage(messages) - 1;
                messages.remove(index);
            }
        } while (answer.equalsIgnoreCase("yes"));

        removeMessage(messages);
    }

    private int keepMessage(ArrayList<Message> messages) {
        int num;
        System.out.println("Enter the message number you would like to keep. ");
        do {
            num = input.nextInt();
            if ((num < 1) || (num > messages.size())) {
                System.out.println("Invalid input.");
            }
        } while ((num < 1) || (num > messages.size()));

        return num;
    }

    private void removeMessage(ArrayList<Message> messages) {
        ArrayList<Message> messageList = messageManager.getMessageList();
        int index;
        for (Message m : messages) {
            index = messageList.indexOf(m);
            if (index != -1) {
                messageList.remove(index);
            }
        }
    }

    private void sendMessage() {
        String sender = accountManager.getAccount().getUserName();
        String receiver = findUserToSendMessage();
        if (receiver.equalsIgnoreCase("EXIT")) {
            return;
        }
        ArrayList<String> messages = writeMessages();

        Message message = new Message(sender, receiver, messages);
        messageManager.addMessage(message);

        saveMessageToFile();
    }

    private void saveMessageToFile() {
        try {
            jsonWriter.open(MESSAGE_FILEPATH);
            jsonWriter.writeMessageManager(messageManager);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving the message.");
        }
    }

    private void saveAccountToFile() {
        try {
            jsonWriter.open(ACCOUNT_FILEPATH);
            jsonWriter.writeAccountManager(accountManager);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving the account.");
        }

    }

    private ArrayList<String> writeMessages() {
        ArrayList<String> messages = new ArrayList<>();
        String userInput;

        //Allows the user to keep sending messages until they enter "EXIT"
        System.out.println("Enter anything you want and if you wish to exit enter EXIT. ");
        do {
            userInput = input.nextLine();
            if (!userInput.trim().equalsIgnoreCase("EXIT")) {
                messages.add(userInput + "\n");
            }
        } while (!userInput.trim().equalsIgnoreCase("EXIT"));
        System.out.println("You have exited. ");

        return messages;
    }

    public void displayUserInbox() {
        ArrayList<String> userInboxNames = messageManager.getUserInboxNames();
        System.out.println("You have received messages from: ");
        for (String names : userInboxNames) {
            System.out.println(names);
        }
        System.out.println();
    }

    private String findUserToSendMessage() {
        String receiver;

        do {
            System.out.println("If you wish to exit type EXIT");
            System.out.print("Enter the user name you would like to send to: ");
            receiver = input.next();
            if (!accountManager.usernameIsTaken(receiver)) {
                System.out.println("Not a valid user name. ");
            }
        } while (!accountManager.usernameIsTaken(receiver) && !receiver.equals("EXIT"));
        return receiver;
    }

    private int enterUsername() {
        String username;
        do {
            System.out.println("If you wish to exit type EXIT");
            System.out.print("Enter your username: ");
            username = input.nextLine();

            accountManager.findAccount(username);

            if (username.trim().equals("EXIT")) {
                return -1;
            } else if (accountManager.getAccount() == null) {
                System.out.println("Account not found. ");
            }
        } while (accountManager.getAccount() == null);
        if (accountManager.getAccount() != null) {
            return 1;
        } else {
            return -1;
        }
    }

    public void enterPassword() {
        boolean exit = false;
        int tries = 3;
        String password;
        do {
            System.out.println("Type EXIT as your password to leave. ");
            System.out.print("Enter your password: ");
            password = input.nextLine();
            if (password.trim().equalsIgnoreCase("exit")) {
                exit = true;
                System.out.println("Exited" + "\n");
            } else if (accountManager.checkPassword(password)) {
                accountManager.logIn();
                System.out.println("Logged in!" + "\n");
            } else {
                System.out.println("Password is incorrect. ");
                System.out.println("Tries remaining: " + tries + "\n");
                tries--;
            }
        } while (!accountManager.getIsLoggedIn() && !exit && tries > 0);
    }

    public String makeUsername() {
        String username;
        do {
            System.out.print("Enter a user name: ");
            username = input.nextLine();
            if (!accountManager.isValidUsername(username)) {
                System.out.println("The user name you entered is not valid. ");
            } else if (accountManager.usernameIsTaken(username)) {
                System.out.println("The user name you entered is taken. ");
                System.out.println("Here are some suggestions: ");
                System.out.println(accountManager.suggestUsernameVariation1(username));
                System.out.println(accountManager.suggestUsernameVariation2(username) + "\n");
            }
        } while (!accountManager.isValidUsername(username) || accountManager.usernameIsTaken(username));

        return username;
    }

    public String makePassword() {
        String password;
        do {
            System.out.print("Enter your password: ");
            password = input.nextLine();
            if (password.length() < 8) {
                System.out.println("The password must be at least 8 characters long. ");
            }
        } while (password.length() < 8);
        return password;
    }
}
