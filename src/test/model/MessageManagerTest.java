package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageManagerTest {

    private MessageManager messageManger;
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
        messageManger = new MessageManager(a1);
    }

    @Test
    void testAddMessage() {
        messageManger.addMessage(m1);
        messageManger.addMessage(m2);
        messageManger.addMessage(m3);

        ArrayList<Message> messages = messageManger.getMessageList();

        assertTrue(messages.contains(m1));
        assertTrue(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertEquals(3, messages.size());
    }

    @Test
    void testLoadUserInbox() {
        messageManger.addMessage(m1);
        messageManger.addMessage(m2);
        messageManger.addMessage(m3);
        messageManger.addMessage(m4);
        messageManger.addMessage(m5);

        ArrayList<Message> messages = messageManger.getUserInbox();

        assertFalse(messages.contains(m1));
        assertFalse(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertFalse(messages.contains(m4));
        assertTrue(messages.contains(m5));
        assertEquals(2, messages.size());
    }

    @Test
    void testGetUserMessages() {
        messageManger.addMessage(m1);
        messageManger.addMessage(m2);
        messageManger.addMessage(m3);
        messageManger.addMessage(m4);
        messageManger.addMessage(m5);

        ArrayList<Message> messages = messageManger.getUserMessages("isaac2");

        assertFalse(messages.contains(m1));
        assertFalse(messages.contains(m2));
        assertTrue(messages.contains(m3));
        assertFalse(messages.contains(m4));
        assertFalse(messages.contains(m5));
        assertEquals(1, messages.size());
    }
}

