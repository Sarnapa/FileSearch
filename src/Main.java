import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Ograniczenia dla okna początkowego
        final int MIN_WIDTH = 600;
        final int MIN_HEIGHT = 400;
        Parent root = FXMLLoader.load(getClass().getResource("/Views/MainView.fxml"));
        primaryStage.setTitle("Wyszukiwarka plików");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setScene(new Scene(root, MIN_WIDTH, MIN_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
