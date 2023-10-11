package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageManagerTest {

    private MessageManager messageManager;
    private Account a1;
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;


    @BeforeEach
    void runBefore() {
        ArrayList<String> message = new ArrayList<>();
        message.add("hi");
        message.add("how are you");
        a1 = new Account("isaac1", "password");
        m1 = new Message("isaac1", "isaac3", message);
        m2 = new Message("isaac2", "isaac3", message);
        m3 = new Message("isaac2", "isaac1", message);
        m4 = new Message("isaac1", "isaac2", message);
        m5 = new Message("isaac3", "isaac1", message);
        messageManager = new MessageManager(a1);
    }

    @Test
    void testAddMessage() {
        messageManager.addMessage(m1);
        messageManager.addMessage(m2);
        messageManager.addMessage(m3);

        ArrayList<Message> messages = messageManager.getMessageList();

        assertTrue(messages.contains(m1));
        assertTrue(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertEquals(3, messages.size());
    }

    @Test
    void testLoadUserInbox() {
        messageManager.addMessage(m1);
        messageManager.addMessage(m2);
        messageManager.addMessage(m3);
        messageManager.addMessage(m4);
        messageManager.addMessage(m5);


        ArrayList<Message> messages = messageManager.getUserInbox();

        assertFalse(messages.contains(m1));
        assertFalse(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertFalse(messages.contains(m4));
        assertTrue(messages.contains(m5));
        assertEquals(2, messages.size());
    }

    @Test
    void testGetUserInboxNames() {
        messageManager.addMessage(m1);
        messageManager.addMessage(m2);
        messageManager.addMessage(m3);
        messageManager.addMessage(m4);
        messageManager.addMessage(m5);
        messageManager.loadUserInbox();

        ArrayList<String> inboxNames = messageManager.getUserInboxNames();
        assertTrue(inboxNames.contains("isaac2"));
        assertTrue(inboxNames.contains("isaac3"));
        assertFalse(inboxNames.contains("isaac"));
        assertEquals(2, inboxNames.size());

    }

    @Test
    void testGetUserMessages() {
        messageManager.addMessage(m1);
        messageManager.addMessage(m2);
        messageManager.addMessage(m3);
        messageManager.addMessage(m4);
        messageManager.addMessage(m5);

        ArrayList<Message> messages = messageManager.getUserMessages("isaac2");

        assertFalse(messages.contains(m1));
        assertFalse(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertFalse(messages.contains(m4));
        assertFalse(messages.contains(m5));
        assertEquals(1, messages.size());
    }
}

