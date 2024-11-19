package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        Message addedMessage = messageDAO.insertMessage(message);
        return addedMessage;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }

    public Message getMessageById(int id){
        Message message = messageDAO.getMessageById(id);
        return message;
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageById(int id, String message_text){
        return messageDAO.updateMessageById(id, message_text);
    }

    public List<Message> getMessagesByAccountID(int id){
        List<Message> messages = messageDAO.getMessagesByAccountId(id);
        return messages;
    }
}
