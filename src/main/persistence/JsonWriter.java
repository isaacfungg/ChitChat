package persistence;

import model.Account;
import model.AccountManager;
import model.Message;
import model.MessageManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


// Code influced by the JsonSerizalizationDemo
//Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
public class JsonWriter {

    private static final int TAB = 4;
    private PrintWriter writer;

    public JsonWriter() {
    }

    //Effects: assigns the printwriter to the filepath
    public void open(String filePath) throws FileNotFoundException {
        writer = new PrintWriter(filePath);
    }

    // EFFECTS: Writes MessageManager data to a JSON file.
    public void writeMessageManager(MessageManager messageManager) {
        JSONArray messageArray = new JSONArray();

        for (Message message : messageManager.getMessageList()) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("sender", message.getSender());
            jsonMessage.put("receiver", message.getReceiver());

            // Create a JSON array for messages and add each message
            JSONArray messagesArray = new JSONArray();
            for (String messageText : message.getMessages()) {
                messagesArray.put(messageText);
            }
            jsonMessage.put("messages", messagesArray);

            messageArray.put(jsonMessage);
        }
        saveToFile(messageArray.toString(TAB));
    }

    // EFFECTS: Writes AccountManager data to a JSON file.
    public void writeAccountManager(AccountManager accountManager) {
        JSONArray accountArray = new JSONArray();

        for (Account account : accountManager.getAccountList()) {
            JSONObject jsonAccount = new JSONObject();
            jsonAccount.put("username", account.getUserName());
            jsonAccount.put("password", account.getPassword());

            accountArray.put(jsonAccount);
        }
        saveToFile(accountArray.toString(TAB));
    }

    //MODIFIES: this
    //Effects: Writes the string to the designated file
    private void saveToFile(String json) {
        writer.print(json);
    }

    //MODIFIES: this
    //EFFECTS: Closes the Writer
    public void close() {
        writer.close();
    }
}
