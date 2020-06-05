import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ConnectClient implements Runnable {

    private static String login;
    private final String pass;
    private Message message;

    private static ObjectOutputStream out;
    private static UserLog userLog = new UserLog();

    private ControllerChat controllerChat;


    public static String getLogin() {
        return login;
    }

    public ConnectClient(String login, String pass) {
        ConnectClient.login = login;
        this.pass = pass;
    }

    public static void sendMessage(String cmd, String login, String pass, String message) throws IOException {
        if (cmd == null) {
            userLog.writItem(message);
        }
        out.writeObject(new Message(cmd, login, pass, message));
        out.writeChars("q");
        out.flush();
    }

    public void showWinChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerLogin.class.getResource("main.fxml"));
        Parent winChat = fxmlLoader.load();
        controllerChat = fxmlLoader.getController();
        Main.stageForm.setScene(new Scene(winChat));
        Main.stageForm.centerOnScreen();
        Main.stageForm.setTitle("Работун чат");
        Main.stageForm.setOnCloseRequest(e -> {
            System.out.println("Close form chat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("chat_client/src/main/java/" + login + ".log"))) {
                oos.writeObject(userLog);
                oos.flush();
                oos.close();
                sendMessage("/exit", login, null, null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private void loadLog() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("chat_client/src/main/java/" + login + ".log"))) {
            userLog = (UserLog) ois.readObject();
            for (int i = 0; i < userLog.getIndex(); i++) {
                String s = userLog.getLogs()[i].getDate() + "\n" + userLog.getLogs()[i].getMes();
                controllerChat.addListMessage(s);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Файл лога отсутствует");
        }
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 8189)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            exit:
            while (socket.isConnected()) {
                message = (Message) in.readObject();
                in.readChar();
                if (message.getCmd() != null) {
                    switch (message.getCmd()) {
                        case ("/exit"):
                            if (message.getLogin().equals(login)) {
                                break exit;
                            } else {
                                controllerChat.listUser.getItems().remove(message.getLogin());
                            }
                            break;
                        case ("/newUser"):
                            if (message.getLogin().equals("?User")) {
                                //Запрос имени и пароля
                                sendMessage("?User", login, pass, null);
                                break;
                            }
                            if (!message.getLogin().equals(login)) {
                                //Добавился новы пользователь в список
                                controllerChat.listUser.getItems().add(message.getLogin());
                            } else {
                                //Пользователь подключился
                                Platform.runLater(() -> {
                                    try {
                                        showWinChat();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    controllerChat.userName.setText(login);
                                    loadLog();
                                    controllerChat.addListMessage(message.getDate() + "\n" + message.getMessage());
                                });
                            }
                            break;
                        case ("/notUser"):
                            Platform.runLater(() ->
                                    ControllerLogin.showError(Alert.AlertType.ERROR, "Ошибка", "Нет такого пользователя!")
                            );
                            break exit;
                        case ("/notPass"):
                            Platform.runLater(() ->
                                    ControllerLogin.showError(Alert.AlertType.ERROR, "Ошибка", "Не верный пароль!")
                            );
                            break exit;
                        default:
                            break exit;
                    }
                } else {
                    controllerChat.addListMessage("От: " + message.getLogin() + " " + message.getDate() + "\n" + message.getMessage());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
