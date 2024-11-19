package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message insertMessage (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        message.setMessage_id(generatedKeys.getInt(1)); 
                    }
                }
            } else {
                return null;
            }
    
            return message;
    
        } catch (SQLException e) {
            System.out.println("Error while inserting message: " + e.getMessage());
            return null;
        }
    }
    
    public Message deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);

            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // Retrieve the message details
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
    
                // Delete the message
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();
    
                return message;
            }
        
        } catch (SQLException e) {
        System.out.println(e.getMessage());
        }

        return null;
    } 

    public Message updateMessageById(int id, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
    
            updateStmt.setString(1, messageText);
            updateStmt.setInt(2, id);

            int rowsUpdated = updateStmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                selectStmt.setInt(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error while updating message: " + e.getMessage());
        }

        return null;
    }

    public List<Message> getMessagesByAccountId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
