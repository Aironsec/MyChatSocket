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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin {
    public TextField loginText;
    public PasswordField passText;
    public Label warningMes;

    private int attempts = 1;

    public void butLogin() {
        if (loginText.getText().isEmpty() || passText.getText().isEmpty()) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Имя или пароль не могут быть пустыми!");
            return;
        }
        attempts++;
        if (attempts > 3) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Вход запрещён!");
            Main.stageForm.close();
        } else
            runConnect();
    }

    public void butRegistry() {
        if (loginText.getText().isEmpty() || passText.getText().isEmpty()) {
            showError(Alert.AlertType.ERROR, "Ошибка", "Имя или пароль не могут быть пустыми!");
            return;
        }
        runConnect();
    }

    private void runConnect() {
        ConnectClient connect = new ConnectClient(loginText.getText(), passText.getText());
        new Thread(connect).start();
    }

    public static void showError(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
