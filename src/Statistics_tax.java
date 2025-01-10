import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Statistics_tax {
    
@FXML
private TextField selectedYearField;

@FXML
private TextField numberOfPeopleField;


@FXML
public void sendToResults(ActionEvent event) {

    String selectedYear = selectedYearField.getText().trim();
    String numOfPeople = numberOfPeopleField.getText().trim();
    try {
        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistics_Tax_Results.fxml")); // Ensure the file name is correct
        Parent root = loader.load();



        Statistics_tax_Results statistics_tax_Results = loader.getController();
       statistics_tax_Results.setResults(selectedYear, numOfPeople);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Rezultatet");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    } catch (IOException e) {
        e.printStackTrace(); 
    }
}









}
