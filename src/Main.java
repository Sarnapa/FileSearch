import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

    // Ograniczenia dla okna początkowego - stały rozmiar
    private final int WIDTH = 600;
    private final int HEIGHT = 450;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/MainView.fxml"));
        primaryStage.setTitle("Wyszukiwarka plików");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, WIDTH,  HEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
