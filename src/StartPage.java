
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.stage.Stage;
 
public class StartPage extends Application {

    @Override
    public void start(Stage primaryStage) {
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Parent root = loader.load();  // Properly load the FXML file

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Programi per pagesen e Hakut te Kishes");
        primaryStage.setFullScreen(false);
        primaryStage.show();
            
            
            } catch (IOException e) {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR); 
                alert.setContentText("There is an error with the program start");
                alert.show();
                e.printStackTrace();
            }
            
        
    }
 
 public static void main(String[] args) {
    
    launch(args);
    
    }
}


