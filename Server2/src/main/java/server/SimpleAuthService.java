package server;

import java.sql.*;

public class SimpleAuthService implements AuthService{

    private static Connection connection;
    private static Statement stmt;

    private class UserData{
        String login;
        String password;
        String nickname;



        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }




    public SimpleAuthService() {
            connect ();
    }

    private void disconnect() {
        try {
            stmt.close();
            connection.close();
            System.out.println("бу");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect () {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            disconnect();
        }

    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {

        try {
            ResultSet rst = stmt.executeQuery("SELECT nickname FROM clients WHERE login = '" + login +
                    "' AND password = '" + password + "'");
            return rst.getString("nickname");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            ResultSet rst = stmt.executeQuery("SELECT * FROM clients");
            while (rst.next()){
                if (rst.getString("login").equals(login) && rst.getString("nickname").equals(nickname)){
                    return false;
                }
            }
            stmt.executeUpdate("INSERT INTO clients (login, password, nickname)" +
                    " VALUES ('" + login + "', '" + password + "','" + password + "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean rename(String login, String password, String nickname) {
        try {
            int a = stmt.executeUpdate("UPDATE clients SET nickname = '" + nickname +"' WHERE login = '" + login + "'");
            if (a != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}