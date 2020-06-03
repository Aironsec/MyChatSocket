import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {
    public TextField loginText;
    public PasswordField passText;
    public Label warningMes;

    public ControllerChat controllerChat;
    public static Scene scene;
    private int attempts = 1;

    private Boolean registration = false;

    public Boolean getRegistration() {
        return registration;
    }

    public void butLogin() {
        if (loginText.getText().isEmpty() || passText.getText().isEmpty()) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Имя или пароль не могут быть пустыми!");
            return;
        }
        attempts++;
        if (attempts > 3) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Вход запрещён!");
            Main.stage.close();
//            Platform.exit();
        } else
            runConnect();
    }

    public void butRegistry() {
        if (loginText.getText().isEmpty() || passText.getText().isEmpty()) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Имя или пароль не могут быть пустыми!");
            return;
        }
        registration = true;
        runConnect();
    }

    private void runConnect() {
        ConnectClient connect = new ConnectClient(loginText.getText(), passText.getText(), controllerChat);
        new Thread(connect).start();
    }

    public static void showWinChat() {
        Main.stage.setScene(scene);
        Main.stage.setResizable(true);
        Main.stage.centerOnScreen();
        Main.stage.setAlwaysOnTop(false);
        Main.stage.setTitle("Работун чат");
        Main.stage.setOnCloseRequest(e -> {
            System.out.println("Close form chat");
            try {
                ConnectClient.sendMessage("/exit", ConnectClient.getLogin(), null, null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public static void showError(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerLogin.class.getResource("main.fxml"));
        Parent winChat = null;
        try {
            winChat = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controllerChat = fxmlLoader.getController();
        scene = new Scene(winChat);
    }
}
