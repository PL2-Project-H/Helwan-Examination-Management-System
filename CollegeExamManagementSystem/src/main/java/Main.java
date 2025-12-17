import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FileHandler;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize data files on startup
        FileHandler.initializeDataFiles();
        
        URL fxmlUrl = getClass().getResource("/fxml/Login.fxml");
        if (fxmlUrl == null) {
            System.err.println("Cannot find Login.fxml");
            return;
        }
        Parent root = FXMLLoader.load(fxmlUrl);
        
        Scene scene = new Scene(root, 400, 300);
        

        primaryStage.setTitle("College Examination Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
