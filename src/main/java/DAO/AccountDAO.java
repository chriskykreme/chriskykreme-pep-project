package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    // Requirement 1
    public Account insertAccount(Account account) {
    Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) values(?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Method to check if username already exists
    public boolean checkUsernameExist(String username) {
        Connection connection = ConnectionUtil.getConnection();
        boolean exists = false;
        try {
            String sql = "SELECT 1 FROM account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){
                exists = false;
            }
            else {
                exists = true;
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    // Requirement 2
    public Account getAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
            try {
                String username = account.getUsername();
                String password = account.getPassword();
                String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
                PreparedStatement ps = connection.prepareStatement(sql);

                ps.setString(1,username);
                ps.setString(2,password);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                      int accountId = rs.getInt("account_id");
                    return new Account(accountId, username, password);   
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return null;
    }
    
}
