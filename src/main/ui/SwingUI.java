package ui;

import model.Account;
import model.AccountManager;
import model.Message;
import model.MessageManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SwingUI {

    //Program Variables
    private static AccountManager accountManager = new AccountManager();
    private static MessageManager messageManager;
    private static JsonReader jsonReader;
    private static JsonWriter jsonWriter;
    private static final String MESSAGE_FILEPATH = "./data/messageManager.json";
    private static final String ACCOUNT_FILEPATH = "./data/accountManager.json";

    //Login Variables
    private static JFrame loginFrame;
    private static JPanel loginPanel;
    private static JTextField userText;
    private static JPasswordField passwordText;

    //Option Variables
    private static JFrame optionsFrame;
    private static JPanel optionsPanel;
    private static JButton sendMessageButton;
    private static JButton openMessageButton;
    private static JButton displayUsersButton;
    private static JButton loadMessagesButton;
    private static JButton logoutButton;

    //Send Message Variables
    private static JFrame sendMessageFrame;
    private static JButton messageBackButton;
    private static JButton postButton;
    private static JTextArea messageTextArea;
    private static JTextField messageInput;

    //Display User Variables
    private static JComboBox userListBox;
    private static JFrame findUserFrame;

    public static void main(String[] args) {
        jsonReader = new JsonReader();
        jsonWriter = new JsonWriter();
        SwingUtilities.invokeLater(() -> createLoginPage());
    }

    // ----------------------------------------   Create Page Methods  ----------------------------------------

    /**
     * Creates the login page for the user
     * to either log in or create an account
     */
    public static void createLoginPage() {
        setLoginFrame();

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        loginPanel.add(loginButton);
        loginPanel.add(createAccountButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        loginButton.addActionListener(e -> {
            performLoginAction();
        });

        createAccountButton.addActionListener(e -> {
            performCreateAccountAction();
        });
    }

    /**
     * Creates the screen that displays all the options
     * for the user to pick from after they are signed in
     */
    public static void createOptionsPage() {
        setOptionsFrame();
        setOptionsButton();

        optionsFrame.add(optionsPanel);
        optionsFrame.setVisible(true);
    }

    /**
     * Creates the page for the user to
     * send messages to
     */
    private static void createSendMessagePage(String user) {
        setMessagePage(user);

        ArrayList<String> messages = new ArrayList<>();
        postButton.addActionListener(e -> {
            String message = messageInput.getText() + "\n";
            messageTextArea.append(message);
            messages.add(message);
            messageInput.setText("");
        });

        messageBackButton.addActionListener(e -> {
            String sender = accountManager.getAccount().getUserName();
            Message message = new Message(sender, user, messages);
            messageManager.addMessage(message);
            saveMessageToFile();
            createOptionsPage();
            sendMessageFrame.dispose();
        });

        sendMessageFrame.setVisible(true);
    }

    /**
     * Displays all the messages that
     * were sent by the given sender
     */
    private static void createDisplayMessagePage(String sender) {
        JFrame displayMessageFrame = makeFrame();

        JTextArea displayMessageArea = new JTextArea(20, 20);
        displayMessageArea.setEditable(false);
        JPanel displayMessagePanel = new JPanel(new GridLayout(2, 1));
        JScrollPane scrollPane = new JScrollPane(displayMessageArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        appendMessages(displayMessageArea, sender);

        saveButton.addActionListener(e -> {
            displayMessageFrame.dispose();
            createOptionsPage();
        });

        backButton.addActionListener(e -> {
            removeMessage(sender);
            displayMessageFrame.dispose();
            createOptionsPage();
        });

        displayMessagePanel.add(backButton);
        displayMessagePanel.add(saveButton);

        displayMessageFrame.add(scrollPane, BorderLayout.CENTER);
        displayMessageFrame.add(displayMessagePanel, BorderLayout.SOUTH);
        displayMessageFrame.setVisible(true);
    }

    /**
     * Display the users that are available to
     * choose from to see their messages
     */
    private static void createOpenMessagePage() {
        JFrame openMessageFrame = new JFrame("Messaging Program");
        openMessageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        openMessageFrame.setSize(500, 300);
        openMessageFrame.setLocationRelativeTo(null);

        JPanel inboxPanel = new JPanel(new GridLayout(2, 1));
        ArrayList<String> inboxNameList = messageManager.getUserInboxNames();
        JComboBox inboxNameBox = new JComboBox(inboxNameList.toArray());

        inboxNameBox.addActionListener(e -> {
            String selectedUser = (String) inboxNameBox.getSelectedItem();
            createDisplayMessagePage(selectedUser);
            openMessageFrame.dispose();
        });

        inboxPanel.add(inboxNameBox);
        inboxPanel.add(getBackButton(openMessageFrame));
        openMessageFrame.add(inboxPanel);
        openMessageFrame.setVisible(true);
    }

    /**
     * Creates a page for the user to find
     * a user to send a message to
     */
    public static void createFindUserPage() {
        findUserFrame = makeFrame();

        JPanel findUserPanel = new JPanel(new GridLayout(2, 1));
        ArrayList<String> usernameList = new ArrayList<>();
        for (Account account : accountManager.getAccountList()) {
            usernameList.add(account.getUserName());
        }

        userListBox = new JComboBox(usernameList.toArray());

        userListBox.addActionListener(e -> {
            String selectedUsername = (String) userListBox.getSelectedItem();
            createSendMessagePage(selectedUsername);
            findUserFrame.dispose();
        });

        findUserPanel.add(userListBox);
        findUserFrame.add(findUserPanel);
        findUserFrame.setVisible(true);
    }

    /**
     * Creates the page for the user
     * to see all the users available
     */
    private static void createDisplayUsersPage(boolean sort) {
        JFrame displayUsersFrame = makeFrame();
        JPanel displayUsersPanel = new JPanel(new BorderLayout());

        JTextArea userDisplayField = new JTextArea();
        userDisplayField.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(userDisplayField);
        displayUsersPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JButton backButton = getBackButton(displayUsersFrame);
        JButton sortButton = new JButton("Sort");

        buttonPanel.add(sortButton, BorderLayout.WEST);
        buttonPanel.add(backButton, BorderLayout.EAST);

        displayUsersPanel.add(buttonPanel, BorderLayout.SOUTH);

        ArrayList<Account> accountList = accountManager.getAccountList();
        userDisplayField.setText(createString(accountList, sort).toString());

        sortButton.addActionListener(e -> {
            createDisplayUsersPage(true);
            displayUsersFrame.dispose();
        });

        displayUsersFrame.add(displayUsersPanel);
        displayUsersFrame.setVisible(true);
    }


    //---------------------------------------- Set Methods ----------------------------------------

    private static JFrame makeFrame() {
        JFrame frame = new JFrame("Messaging Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        return frame;
    }

    /**
     * Set all the components for the
     * send message page
     */
    private static void setMessagePage(String user) {
        sendMessageFrame = makeFrame();

        messageTextArea = new JTextArea(20, 20);
        messageInput = new JTextField(1);
        postButton = new JButton("Send");
        JPanel inputPanel = new JPanel();
        messageBackButton = new JButton("Back");

        messageInput.setColumns(25);
        messageTextArea.setEditable(false);

        inputPanel.add(messageInput);
        inputPanel.add(postButton);
        inputPanel.add(messageBackButton);

        sendMessageFrame.add(messageTextArea, BorderLayout.CENTER);
        sendMessageFrame.add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a back button that closes
     * the frame given in the parameter
     */
    private static JButton getBackButton(JFrame frame) {
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            frame.dispose();
            createOptionsPage();
        });

        return backButton;
    }

    /**
     * Set the action listener of the options
     * according to their needs
     */
    private static void setOptionsButton() {
        sendMessageButton.addActionListener(e -> {
            createFindUserPage();
            optionsFrame.dispose();
        });

        openMessageButton.addActionListener(e -> {
            createOpenMessagePage();
            optionsFrame.dispose();
        });

        displayUsersButton.addActionListener(e -> {
            createDisplayUsersPage(false);
            optionsFrame.dispose();
        });

        loadMessagesButton.addActionListener(e -> {
            loadMessageManager();
            ImageIcon icon = new ImageIcon("data/ButtonImages/smiley_face.png");
            JOptionPane.showMessageDialog(loginFrame, "Loaded Messages",
                    "Loaded Messages", JOptionPane.INFORMATION_MESSAGE, icon);
        });

        logoutButton.addActionListener(e -> {
            accountManager.logOut();
            optionsFrame.dispose();
            System.exit(0);
        });
    }

    /**
     * Instantiate the buttons for the options
     * page and add them to the panel
     */
    private static void setOptionsFrame() {
        optionsFrame = makeFrame();

        optionsPanel = new JPanel(new GridLayout(5, 1));

        sendMessageButton = new JButton("Send Message");
        openMessageButton = new JButton("Open Messages");
        displayUsersButton = new JButton("Display Available Users");
        loadMessagesButton = new JButton("Load Messages");
        logoutButton = new JButton("Log Out");

        optionsPanel.add(sendMessageButton);
        optionsPanel.add(openMessageButton);
        optionsPanel.add(displayUsersButton);
        optionsPanel.add(loadMessagesButton);
        optionsPanel.add(logoutButton);
    }

    /**
     * Instantiates everything needed
     * for the login page
     */
    private static void setLoginFrame() {
        loadAccountManager();
        loginFrame = new JFrame("Login Page");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLocationRelativeTo(null);

        loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();

        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
    }

    //---------------------------------------- Perform Action Methods ----------------------------------------

    /**
     * Checks if the entered information
     * is correct when the user is logging in
     */
    private static void performLoginAction() {
        String username = userText.getText();
        accountManager.findAccount(username);
        if (accountManager.getAccount() == null) {
            JOptionPane.showMessageDialog(loginFrame, "Invalid username: " + username);
            userText.setText("");
            passwordText.setText("");
            return;
        }

        String password = passwordText.getText();
        if (!accountManager.checkPassword(password)) {
            JOptionPane.showMessageDialog(loginFrame, "Incorrect password");
            passwordText.setText("");
            return;
        }
        accountManager.logIn();
        messageManager = new MessageManager(accountManager.getAccount());
        loadMessageManager();
        loginFrame.dispose();
        createOptionsPage();
    }

    /**
     * Performs the actions needeed to
     * create an account for the user
     */
    private static void performCreateAccountAction() {
        String username = userText.getText();
        String password = passwordText.getText();
        if (!accountManager.isValidUsername(username)) {
            JOptionPane.showMessageDialog(loginFrame, "Invalid username. (Contains special characters)");
            userText.setText("");
            passwordText.setText("");
        } else if (accountManager.usernameIsTaken(username)) {
            String message = "Invalid username. (Username is taken) \n";
            message += "Available Options: " + accountManager.suggestUsernameVariation1(username)
                    + ", " + accountManager.suggestUsernameVariation2(username);
            JOptionPane.showMessageDialog(loginFrame, message);
            userText.setText("");
            passwordText.setText("");
        } else if (password.length() < 8) {
            JOptionPane.showMessageDialog(loginFrame, "Password must be at least 8 characters.");
            passwordText.setText("");
        }
        userText.setText("");
        passwordText.setText("");
        Account tempAccount = new Account(username, password);
        accountManager.addAccount(tempAccount);
        saveAccountToFile();
        JOptionPane.showMessageDialog(loginFrame, "Account created.");
    }

    //---------------------------------------- Other Methods ----------------------------------------

    /**
     * Creates a single string that
     * contains all usernames
     */
    private static StringBuilder createString(ArrayList<Account> accountList, boolean sort) {
        ArrayList<String> accountNames = new ArrayList<>();
        StringBuilder usersText = new StringBuilder();
        for (Account account : accountList) {
            accountNames.add(account.getUserName());
        }
        if (sort) {
            Collections.sort(accountNames, String.CASE_INSENSITIVE_ORDER);
        }
        for (String name : accountNames) {
            usersText.append(name).append("\n");
        }
        return usersText;
    }

    /**
     * Saves all accounts made
     * to the file
     */
    private static void saveAccountToFile() {
        try {
            jsonWriter.open(ACCOUNT_FILEPATH);
            jsonWriter.writeAccountManager(accountManager);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving the account.");
        }
    }

    /**
     * Loads all accounts from the json
     * file into the account manager
     */
    private static void loadAccountManager() {
        try {
            accountManager = jsonReader.readAccountManager(ACCOUNT_FILEPATH);
        } catch (IOException e) {
            System.out.println("Unable to load accounts");
        }
    }

    /**
     * Loads all messages from the json
     * file into the message manager
     */
    private static void loadMessageManager() {
        try {
            messageManager = jsonReader.readMessageManager(accountManager.getAccount(), MESSAGE_FILEPATH);
            messageManager.loadUserInbox();
        } catch (IOException e) {
            System.out.println("Error loading the data.");
        }
    }

    /**
     * Saves all messages in message
     * manager into the file
     */
    private static void saveMessageToFile() {
        try {
            jsonWriter.open(MESSAGE_FILEPATH);
            jsonWriter.writeMessageManager(messageManager);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving the message.");
        }
    }

    private static void removeMessage(String sender) {
        ArrayList<Message> messageList = messageManager.getUserMessages(sender);
        ArrayList<Message> allMessages = messageManager.getMessageList();
        allMessages.removeAll(messageList);
        saveMessageToFile();
    }

    /**
     * Appends all messages for a specific
     * user into the designated text area
     */
    private static void appendMessages(JTextArea displayMessageArea, String sender) {
        ArrayList<Message> messages = messageManager.getUserMessages(sender);
        ArrayList<String> texts;
        for (int i = 0; i < messages.size(); i++) {
            texts = messages.get(i).getMessages();
            displayMessageArea.append("Message " + (i + 1) + ": \n");
            for (String text : texts) {
                displayMessageArea.append(text);
            }
            displayMessageArea.append("\n");
        }
    }


}
