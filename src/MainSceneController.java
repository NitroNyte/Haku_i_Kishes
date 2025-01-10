import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class MainSceneController {

    private Stage stage;
    private Scene scene;
    //Has to do with paying the taxes, logic behind is that it opens the table, which in that page you can select who is going to pay
    //if its first time you select create name and stuff like surename, payment and other
    //other thing is that it
    @FXML
    
    public void paguajHakun(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TransactionPanel.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle("Paneli i të dhënave për hakun e Kishes");
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("StartPage.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void paguajPuntoret(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TombTaxPanel.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Paneli i të dhënave për pagesat e puntoreve të varrezave");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void importoTeDhena(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Në versionin e ardhshem");
        alert.setHeaderText(null);
        alert.setContentText("Importimi i të dhënave nga programi në program vjen në versionin 2.0!");
        alert.showAndWait(); // Ensure the alert is shown
    }


}
