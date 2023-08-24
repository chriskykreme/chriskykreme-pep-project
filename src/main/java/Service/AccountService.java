package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Requirement 1
    public Account registerAccount(Account account) {
        try {
            String pw = account.getPassword();
            String username = account.getUsername();
            Boolean userExists = accountDAO.checkUsernameExist(username);
            if (account.getUsername() != "" && pw.length() >= 4 && !userExists) {
                    account = accountDAO.insertAccount(account);
                } else {
                    return null;
                }
            } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    // Requirement 2
    public Account login(Account account) {
        try {
            account = accountDAO.getAccount(account);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return account;
    }
}
