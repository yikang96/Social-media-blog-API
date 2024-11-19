package Controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
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
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login",this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages",this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHanlder);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHanlder);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4 ||
            accountService.getAccountByUsername(account.getUsername()) != null) {
        
            ctx.status(400);
            return;
        }

        Account newAccount = accountService.createNewUser(account);
        ctx.json(newAccount);
        
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if (accountService.getAccountByUsernameAndPassword(account.getUsername(),account.getPassword()) == null){
            ctx.status(401);
            return;
        }
        Account existingAcc = accountService.getAccountByUsername(account.getUsername());
        ctx.json(existingAcc);
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        if(message.getMessage_text().isBlank() || message.message_text.length() > 255 ||
            accountService.getAccountById(message.getPosted_by()) == null){
                ctx.status(400);
                return;
        }
        Message addedMessage =  messageService.addMessage(message);
        ctx.json(addedMessage);
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHanlder(Context ctx){

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(messageId);

        if (message == null) {
            ctx.status(200).result();
        } else {
            
            ctx.json(message);
        }
    }

    private void deleteMessageByIdHanlder(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageById(messageId);

        if(deletedMessage == null){
            ctx.status(200).result();
        } else {
            messageService.deleteMessageById(messageId);
            ctx.json(deletedMessage);
        }
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(messageId);

        if (message == null){
            ctx.status(400).result();
            return;
        }

        Map<String, String> requestBody = mapper.readValue(ctx.body(), Map.class);
        String newMessageText = requestBody.get("message_text");
        
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            ctx.status(400).result();
            return;
        }

        Message updatedMessage = messageService.updateMessageById(messageId, newMessageText);
        ctx.json(updatedMessage);
        
    }
    
    private void getAllMessagesByAccountIdHandler(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountID(accountId);

        if(messages == null){
            ctx.status(200).result();
        } else {
            ctx.json(messages);
        }
    }
}