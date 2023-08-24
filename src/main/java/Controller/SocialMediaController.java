package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;

import java.util.*;

public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByIdHandler);
        return app;
    }

    // Requirement 1
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(), Account.class);
        Account newAccount = accountService.registerAccount(account);

        if (newAccount == null) {
            context.status(400);
        } else {
            context.json(objectMapper.writeValueAsString(newAccount));
            context.status(200);
        }
    }

    // Requirment 2
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(), Account.class);

        Account authAccount = accountService.login(account);

        if (authAccount != null) {
            context.json(objectMapper.writeValueAsString(authAccount));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    // Requirement 3
    private void newMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.newMessage(message);

        if (newMessage == null) {
            context.status(400);
        } else {
            context.json(objectMapper.writeValueAsString(newMessage));
            context.status(200);
        }
    }

    // Requirement 4
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
 
    // Requirement 5
    private void getMessageByIDHandler(Context context) {        
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            context.json(message);
        } else { 
            context.status(200);
            context.json("");
        }
    }

    // Requirement 6
    private void deleteMessageHandler(Context context) {        
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId); 
        
        if (deletedMessage != null) {
            context.json(deletedMessage); 
        } else {
            context.status(200);
            context.json(""); 
        }
    }

    // Requirement 7
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = objectMapper.readValue(context.body(), Message.class);
        String updatedMessageText = updatedMessage.getMessage_text();
        
        if (updatedMessageText == null || updatedMessageText.length() > 254) {
            context.status(400).json("");
            return;
        }
    
        // Update the message and get the updated message with full details
        Message resultingMessage = new Message();
        resultingMessage.setMessage_id(messageId);
        resultingMessage.setMessage_text(updatedMessageText);
        Message result = messageService.updateMessage(resultingMessage);
    
        if (result != null) {
            context.status(200).json(result); // Return the Message object itself
        } else {
            context.status(400).json("");
        }
    }
 
    // Requirement 8
    private void getMessagesByIdHandler(Context context) {    
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> userMessages = messageService.findByUserId(userId);
    
        if (!userMessages.isEmpty()) {
            context.status(200);
            context.json(userMessages);
        } else {
            context.status(200);
            context.json(new ArrayList<Message>()); 
        }
    }
}