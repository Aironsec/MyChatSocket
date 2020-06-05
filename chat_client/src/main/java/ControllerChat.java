import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerChat implements Initializable {

    public TextArea textSend;
    public ListView<String> listMess;
    public Label userName;
    public ListView<String> listUser;
    public Label taskValue;

    public void send() throws IOException {
        if (textSend.getText().isEmpty()) return;
        ConnectClient.sendMessage(null, ConnectClient.getLogin(), null, textSend.getText());
        textSend.clear();
    }

    public void sendText(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER) && keyEvent.isControlDown()) {
            if (textSend.getText().isEmpty()) return;
            ConnectClient.sendMessage(null, ConnectClient.getLogin(), null, textSend.getText());
            textSend.clear();
        }
    }

    public void addListMessage(String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listMess.getItems().add(s);
                listMess.setCellFactory(lv -> new ListCell<String>() {
                    private final Label label = new Label();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            label.setText(item);
                            setGraphic(label);
                        }
                    }
                });
            }
        });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void mouseEnterUserName(MouseEvent mouseEvent) {
        userName.setUnderline(true);
    }

    public void mouseExitUserName(MouseEvent mouseEvent) {
        userName.setUnderline(false);
    }

    public void mouseEnterTask(MouseEvent mouseEvent) {
        taskValue.setUnderline(true);
    }

    public void mouseExitTask(MouseEvent mouseEvent) {
        taskValue.setUnderline(false);
    }
}