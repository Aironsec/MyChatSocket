import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Runnable, Initializable {

    public TextArea textSend;
    public ListView<String> listMess;
    public TextField userName;

    private static DataInputStream in;
    private static DataOutputStream out;

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Тест");
        super.finalize();
    }

    public void send() throws IOException {
        if (textSend.getText().isEmpty()) return;
//        addListMessage(textSend.getText());
        sendMessage(textSend.getText());
        textSend.clear();
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

    public void sendText(KeyEvent keyEvent) {
        /*if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (textSend.getText().isEmpty()) return;
            textSend.clear();
        }*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this).start();
    }

    public static void sendMessage(String str) throws IOException {
        out.writeUTF(str);
        out.flush();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 8189)){
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (socket.isConnected()) {
                String message = in.readUTF();
                if (message.startsWith("/")) {
                    if (message.equals("/exit")) {
                        in.close();
                        out.close();
                        socket.close();
                        break;
                    }
                    if (message.startsWith("/newUser")) {
                        userName.setText(message.replace("/newUser", "").trim());
                    }
                } else {
                    addListMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}