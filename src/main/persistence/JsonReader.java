package persistence;

import model.Account;
import model.AccountManager;
import model.Message;
import model.MessageManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

// Code influced by the JsonSerizalizationDemo
//Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
public class JsonReader {


    public JsonReader() {
    }

    // EFFECTS: Reads MessageManager data from a JSON file at the specified filePath.
    public MessageManager readMessageManager(Account account, String filePath) throws IOException {
        JSONArray jsonArray = readFile(filePath);
        return parseMessageManager(jsonArray, account);
    }

    // EFFECTS: Reads AccountManager data from a JSON file at the specified filePath.
    public AccountManager readAccountManager(String filePath) throws IOException {
        JSONArray jsonArray = readFile(filePath);
        return parseAccountManager(jsonArray);
    }

    // EFFECTS: Reads content from a JSON file at the specified filePath.
    private JSONArray readFile(String filePath) throws IOException {
        JSONArray jsonArray;

        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        jsonArray = new JSONArray(fileContent);

        return jsonArray;
    }

    // EFFECTS: Parses JSON data into a MessageManager object.
    private MessageManager parseMessageManager(JSONArray jsonArray, Account account) {
        MessageManager messageManager = new MessageManager(account);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Extract sender, receiver, and messages from the JSON object
            String sender = jsonObject.getString("sender");
            String receiver = jsonObject.getString("receiver");
            JSONArray messagesArray = jsonObject.getJSONArray("messages");


            ArrayList<String> messages = new ArrayList<>();

            // Iterate through the "messages" array
            for (int j = 0; j < messagesArray.length(); j++) {
                messages.add(messagesArray.getString(j));
            }

            messageManager.addMessage(new Message(sender, receiver, messages));
        }

        return messageManager;
    }

    // EFFECTS: Parses JSON data into an AccountManager object.
    private AccountManager parseAccountManager(JSONArray jsonArray) {
        AccountManager accountManager = new AccountManager();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Extract sender, receiver, and messages from the JSON object
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            accountManager.addAccount(new Account(username, password));
        }

        return accountManager;
    }
}
