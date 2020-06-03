import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;

public class ConnectClient implements Runnable {

    private static String login;
    private final String pass;
    private Message message;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    private final ControllerChat controllerChat;

    public static String getLogin() {
        return login;
    }

    public ConnectClient(String login, String pass, ControllerChat controllerChat) {
        ConnectClient.login = login;
        this.pass = pass;
        this.controllerChat = controllerChat;
    }

    public static void sendMessage(String cmd, String login, String pass, String message) throws IOException {
        out.writeObject(new Message(cmd, login, pass, message));
        out.writeChars("q");
        out.flush();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 8189)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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
                                sendMessage("?User", login, pass, null);
                                break;
                            }
                            if (!message.getLogin().equals(login)) {
                                controllerChat.listUser.getItems().add(message.getLogin());
                            } else {
                                Platform.runLater(() -> {
                                    ControllerLogin.showWinChat();
                                    controllerChat.userName.setText(login);
                                    controllerChat.addListMessage(message.getDate() + "\n" + message.getMessage());
                                });
                            }
                            break;
                        case ("/notUser"):
                            Platform.runLater(() ->
                                    ControllerLogin.showError(Alert.AlertType.ERROR, "Ошибка", "Нет такого пользователя")
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
                    controllerChat.addListMessage(message.getDate() + "\n" + message.getMessage());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
