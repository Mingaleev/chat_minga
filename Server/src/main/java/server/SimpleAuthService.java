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


        try {
            connect ();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            disconnect();
//        }
//            stmt.executeUpdate("INSERT INTO clients (login, password, nickname) VALUES ('admin', 'admin','admin')");
//            stmt.executeUpdate("UPDATE clients SET nickname = 'Minga' WHERE login = 'Minga'");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
//            while (rs.next()){
//                if (rs.getString("login").equals("Mingaleev") && rs.getString("password").equals("admin")){
//                    System.out.println(rs.getString("nickname"));
//                }
//            }


//        users = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
//        }
//        users.add(new UserData("qwe", "qwe", "qwe"));
//        users.add(new UserData("asd", "asd", "asd"));
//        users.add(new UserData("zxc", "zxc", "zxc"));
    }

    private void disconnect() {
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect () throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stmt = connection.createStatement();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {

        try {
            ResultSet rst = stmt.executeQuery("SELECT * FROM clients");
            while (rst.next()){
                if (rst.getString("login").equals(login) && rst.getString("password").equals(password)){
                    return rst.getString("nickname");
                }
            }
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




//        for (UserData user : users){
//            if(user.login.equals(login) || user.nickname.equals(nickname)){
//                return false;
//            }
//        }
//        users.add(new UserData(login, password, nickname));
        return true;
    }

    @Override
    public boolean rename(String login, String password, String nickname) {
        try {
            ResultSet rst = stmt.executeQuery("SELECT * FROM clients");
            while (rst.next()){
                if (rst.getString("login").equals(login) && rst.getString("password").equals(password)){
                    stmt.executeUpdate("UPDATE clients SET nickname = '" + nickname +"' WHERE login = '" + login + "'");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}