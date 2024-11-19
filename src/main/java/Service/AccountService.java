package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createNewUser(Account account){
        Account newAccount = accountDAO.createAccount(account);
        return account;
    }

    public Account getAccountById(int id){
        Account account = accountDAO.getAccountById(id);
        return account;
   }

   public Account getAccountByUsername(String username){
        Account account = accountDAO.getAccountByUsername(username);
        return account;
   }

   public Account getAccountByUsernameAndPassword(String username, String password){
    Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
    return account;
   }
    
}
