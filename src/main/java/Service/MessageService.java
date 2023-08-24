package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.*;


public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Requirement 3
    public Message newMessage(Message message) {
            String messageText = message.getMessage_text();
            boolean validUser = messageDAO.postedByUser(message.getMessage_id());
            if (messageText.length() < 255 && validUser && messageText != "") {
                message = messageDAO.createMessage(message);
            } else {
                return null;
            }
        return message;
    }
    
    // Requirement 4
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }

    // Requirement 5
    public Message getMessageById(int message_id) {
            Message message = messageDAO.getMessageByID(message_id);
            return message;
    }

    // Requirement 6
    public Message deleteMessage(int messageId) {
        Message deletedMessage = messageDAO.getMessageByID(messageId);
    
        if (deletedMessage != null) {
            boolean success = messageDAO.deleteById(messageId);
            if (success) {
                return deletedMessage;
            }
        }
        return null;
    }

    // Requirement 7
    public Message updateMessage(Message message) {
        // Extract message ID and updated text from the message object
        int messageId = message.getMessage_id();
        String updatedMessageText = message.getMessage_text();
    
        if (updatedMessageText != null && !updatedMessageText.trim().isEmpty()) {
            Message existingMessage = messageDAO.getMessageByID(messageId);
    
            if (existingMessage != null) {
                existingMessage.setMessage_text(updatedMessageText);
                return messageDAO.update(existingMessage);
            }
        }
        return null;
    }

    // Requirement 8
    public List<Message> findByUserId(int userId) {
        return messageDAO.findByUserId(userId);
    }
}
