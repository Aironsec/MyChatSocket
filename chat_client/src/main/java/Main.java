import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void stop() throws Exception {
        Controller.sendMessage("/exit");
        System.exit(0);
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
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
