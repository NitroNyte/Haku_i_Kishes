

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML
    private AnchorPane pane;

    @FXML
    private Button paguajButton;

    @FXML
    private Button puntortButton;

    @FXML
    private Button importButton;

    //Edit as of 03/04/2025 I'm stopping the update of this program, Thank you for using it. All the best in the future

    // Has to do with paying the taxes, logic behind is that it opens the table,
    // which in that page you can select who is going to pay
    // if its first time you select create name and stuff like surename, payment and
    // other
    // other thing is that it

    @FXML
    public void paguajHakun(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TransactionPanel.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Paguaj Hakun e kishës");
        // Replace only the root, keeping size and full-screen state
        stage.getScene().setRoot(root);
    }

    @FXML
    public void paguajPuntoret(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TombTaxPanel.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Paguaj shërbimin e varrezave");

        // Replace only the root, keeping size and full-screen state
        stage.getScene().setRoot(root);
    }

    @FXML
    public void importoTeDhena(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ImportExportPanel.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Importimi dhe eksportimi i të dhënave");
        stage.getScene().setRoot(root);
    }


    @FXML
    public void statistikat(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Statistics.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Statistikat");
        stage.getScene().setRoot(root);
    }



    @FXML
    public void chooseMassFunc(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChooseYourMassPanel.fxml"));
            Parent root = loader.load();



            Stage newStage = new Stage();
            newStage.setTitle("Zgjedhni famullin");
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
}
