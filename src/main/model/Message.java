package model;

import java.util.ArrayList;

public class Message {

    private String sender;
    private String receiver;
    private ArrayList<String> messages;

    //Modifies: this
    //Effects: Initializes message with a designated
    //sender and receiver along with a list of messages
    public Message(String sender, String receiver, ArrayList<String> messages) {
        setSender(sender);
        setReceiver(receiver);
        setMessages(messages);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

}
