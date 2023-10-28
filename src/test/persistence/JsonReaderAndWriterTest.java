package persistence;

import model.Account;
import model.AccountManager;
import model.Message;
import model.MessageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonReaderAndWriterTest {

    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private MessageManager messageManager;
    private AccountManager accountManager;

    @BeforeEach
    void runBefore() {
        jsonReader = new JsonReader();
        jsonWriter = new JsonWriter();
        messageManager = new MessageManager(new Account("isaac", "password"));
        accountManager = new AccountManager();
    }

    @Test
    void testReadWriteMessageManager() {
        // Create sample messages
        Message message1 = new Message("sender1", "receiver1", new ArrayList<>(List.of("Hello", "Hi")));
        Message message2 = new Message("sender2", "receiver2", new ArrayList<>(List.of("How are you?")));
        messageManager.addMessage(message1);
        messageManager.addMessage(message2);

        // Set the file path for writing the JSON data
        String filePath = "testJsonReaderAndWriter.json";

        try {
            // Write the MessageManager to a JSON file
            jsonWriter.open(filePath);
            jsonWriter.writeMessageManager(messageManager);
            jsonWriter.close();

            // Read the JSON file and create a new MessageManager from it
            MessageManager readMessageManager = jsonReader.readMessageManager(null, filePath);

            // Check if the MessageManager data matches what was written
            List<Message> readMessages = readMessageManager.getMessageList();
            assertEquals(2, readMessages.size());
            assertEquals(message1.getSender(), readMessages.get(0).getSender());
            assertEquals(message2.getReceiver(), readMessages.get(1).getReceiver());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReadWriteAccountManager() {
        // Create sample accounts
        Account account1 = new Account("user1", "password1");
        Account account2 = new Account("user2", "password2");
        accountManager.addAccount(account1);
        accountManager.addAccount(account2);

        String filePath = "testJsonReaderAndWriter.json";

        try {
            // Write the AccountManager to a JSON file
            jsonWriter.open(filePath);
            jsonWriter.writeAccountManager(accountManager);
            jsonWriter.close();

            // Read the JSON file and create a new AccountManager from it
            AccountManager readAccountManager = jsonReader.readAccountManager(filePath);

            // Check if the AccountManager data matches what was written
            List<Account> readAccounts = readAccountManager.getAccountList();
            assertEquals(2, readAccounts.size());
            assertEquals(account1.getUserName(), readAccounts.get(0).getUserName());
            assertEquals(account2.getPassword(), readAccounts.get(1).getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
