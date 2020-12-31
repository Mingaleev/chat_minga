package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;
    @FXML
    public HBox authPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public HBox msgPanel;
    @FXML
    public ListView<String> clientList;

    private Socket socket;



    DataInputStream in;
    DataOutputStream out;

    private boolean authenticated;
    private String nickname;
    private final String TITLE = "ГикЧат";

    private Stage stage;
    private Stage regStage;
    private Stage renameStage;
    private RegController regController;
    private RenController renController;



    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);

        if (!authenticated) {
            nickname = "";
        }
        textArea.clear();
        setTitle(nickname);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
        createRegWindow();
        createRenameWindow();
        Platform.runLater(() -> {
            stage = (Stage) textField.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("byu");
                    try {
                        out.writeUTF("/end");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    private void newSocket (){
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connection() {

        if (socket == null || socket.isClosed()){
            newSocket();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File history;
                try {
                    // цикл аутентификации
                    while (true) {
                        String str = in.readUTF();


                        if (str.startsWith("/authok")) {
                            nickname = str.split("\\s", 2)[1];
                            setAuthenticated(true);

                            history = new File("C:\\Users\\Minga\\IdeaProjects\\chat_minga\\client_2\\src\\main\\java\\client\\history\\" +
                                    nickname + ".txt");
                            if (!history.exists()){
                                history.createNewFile();
                            }
                            FileReader fr = new FileReader(history);
                            BufferedReader reader = new BufferedReader(fr);
                            String line = reader.readLine();
                            while (line != null) {
                                textArea.appendText(line + "\n");
                                // считываем остальные строки в цикле
                                line = reader.readLine();
                            }
//                            textArea.appendText(str + "\n");
                            break;
                        }

                        if(str.startsWith("/regok")){
                            regController.addMsgToTextArea("Регистрация прошла успешно");
                        }
                        if(str.startsWith("/regno")){
                            regController.addMsgToTextArea("Регистрация не получилась \n возможно логин или пароль заняты");
                        }
                        if(str.startsWith("/renok")){
                            renController.addMsgToTextArea("Переименование успешно");
                            nickname = str.split("\\s", 2)[1];
                            setTitle(nickname);
                        }
                        if(str.startsWith("/renno")){
                            renController.addMsgToTextArea("Переименование не получилась \n возможно логин или пароль не подходят");
                        }
                        textArea.appendText(str + "\n");
                    }

                    // цикл работы
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {

                            if (str.equals("/end")) {
                                System.out.println("Клиент отключился");
                                break;
                            }

                            if (str.startsWith("/clientList")) {
                                String[] token = str.split("\\s+");
                                Platform.runLater(() -> {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < token.length; i++) {
                                        clientList.getItems().add(token[i]);
                                    }
                                });
                            }

                        } else {
                            textArea.appendText(str + "\n");
                            byte[] outData = str.getBytes();
                            try (FileOutputStream out = new FileOutputStream(history, true)) {
                                out.write(outData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Мы отключились от сервера");
                    setAuthenticated(false);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connection();
        }
        try {
            out.writeUTF(String.format("/auth %s %s", loginField.getText().trim(),
                    passwordField.getText().trim()));
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitle(String nick) {
        Platform.runLater(() -> {
            ((Stage) textField.getScene().getWindow()).setTitle(TITLE + " " + nick);
        });
    }

    public void clickClientList(MouseEvent mouseEvent) {
        String receiver = clientList.getSelectionModel().getSelectedItem();
        textField.setText("/w " + receiver + " ");
    }

    private void createRegWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reg.fxml"));
            Parent root = fxmlLoader.load();
            regStage = new Stage();
            regStage.setTitle("Reg window");
            regStage.setScene(new Scene(root, 400, 250));

            regController = fxmlLoader.getController();
            regController.setController(this);

            regStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRenameWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rename.fxml"));
            Parent root = fxmlLoader.load();
            renameStage = new Stage();
            renameStage.setTitle("Rename window");
            renameStage.setScene(new Scene(root, 400, 250));

            renController = fxmlLoader.getController();
            renController.setController(this);

            renameStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToReg(String login, String password, String nickname){
        String msg = String.format("/reg %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connection();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToRen(String login, String password, String nickname){
        String msg = String.format("/ren %s %s %s", login, password, nickname);

        if (socket == null || socket.isClosed()) {
            connection();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registration(ActionEvent actionEvent) {
        regStage.show();
    }

    public void rename(ActionEvent actionEvent) {
        renameStage.show();
    }
}