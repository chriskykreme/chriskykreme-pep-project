package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    // Requirement 3
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = " INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int key = (int)rs.getLong(1);
                return new Message(key, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Method to confirm that a message was posted by a valid user
    public boolean postedByUser (Integer id) {
        Connection connection = ConnectionUtil.getConnection();
        boolean exists = false;
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){
                exists = true;
            }
            else {
                exists = false;
            } 
        } catch (SQLException e) { 
            System.out.println(e.getMessage());
        }
        return exists;
    }

    // Requirement 4
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Requirement 5 
    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,message_id);
            ResultSet rs = ps.executeQuery();
                
            while(rs.next()) {
                return new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }      
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Requirement 6
    public boolean deleteById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
    
            int rowsDeleted = ps.executeUpdate();
    
            if (rowsDeleted == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; 
        } 
    }

    // Requirment 7
    public Message update(Message message) {
        Connection connection = ConnectionUtil.getConnection();
    
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());
    
            int rowsUpdated = statement.executeUpdate();
    
            if (rowsUpdated > 0) {
                return getMessageByID(message.getMessage_id()); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no rows were updated
    }

    // Requirement 8
    public List<Message> findByUserId(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            
            List<Message> messages = new ArrayList<>();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
    
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return new ArrayList<>();
    }
}