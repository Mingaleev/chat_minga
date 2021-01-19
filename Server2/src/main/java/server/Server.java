package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class Server {

    private static final Logger loger = Logger.getLogger(Server.class.getName());

    private static int PORT = 8189;
    ServerSocket server = null;
    Socket socket = null;
    List<ClientHandler> clients;
    private AuthService authService;
    ExecutorService executorService;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
        executorService = Executors.newCachedThreadPool();
        Handler consolHandler = new ConsoleHandler();
        try {
            Handler fileHandler = new FileHandler("logs.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            loger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loger.addHandler(consolHandler);
        loger.setUseParentHandlers(false);



        try {
            server = new ServerSocket(PORT);
//            System.out.println("Сервер запущен");
            loger.info( "Сервер запущен");

            while (true) {
                socket = server.accept();
                loger.info( "Клиент подключился");
//                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
                executorService.shutdown();
                loger.severe( "ошибка");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void broadCastMsg(ClientHandler sender, String msg) {
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

        String message = String.format("%s %s : %s", formater.format(new Date()), sender.getNickname(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message + "\n");
        }
    }

    public void loger(String str){
        loger.info(str);
    }

    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
        broadClientList();
    }

    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
        broadClientList();
    }

    public AuthService getAuthService(){
        return authService;
    }

    public void privateCastMsg(ClientHandler sender, String receiver,  String msg) {
        String message = String.format("[%s] private [%s] : %s", sender.getNickname(), receiver, msg);
        for (ClientHandler c : clients) {
            if(c.getNickname().equals(receiver)){
                c.sendMsg(message + "\n");
                if(!c.equals(sender)) {
                    sender.sendMsg(message);
                }
                return;
            }
        }
    }

    public boolean isLoginAuthenticated(String login){
        for (ClientHandler c: clients) {
            if(c.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    public void broadClientList() {
        StringBuilder sb = new StringBuilder("/clientList ");
        for(ClientHandler c: clients){
            sb.append(c.getNickname()).append(" ");
        }
        String msg = sb.toString();
        for (ClientHandler c: clients){
            c.sendMsg(msg);
        }
    }
}