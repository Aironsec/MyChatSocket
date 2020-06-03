import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent winLogin = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Войти в чат");
        primaryStage.setScene(new Scene(winLogin));
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setOnCloseRequest(e -> System.out.println("Close form login"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Main.launch(args);
    }
}
