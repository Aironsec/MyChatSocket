import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void stop() throws Exception {
//        Заверщение клиента по крестику работает отлично
        ControllerMain.sendMessage("/exit" + ControllerMain.getUserID());
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Работун чат");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(400.0);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Main.launch(args);
    }
}
