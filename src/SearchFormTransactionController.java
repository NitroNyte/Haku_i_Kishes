import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SearchFormTransactionController {
    


    


    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField fatherNameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField regionField;

    @FXML
    private TextField phoneNumField;

    @FXML
    private Button applyFilterButton;



    private TableViewController tableViewController;

    public void setTruceViewController(TableViewController tableViewController) {
        this.tableViewController = tableViewController;
    }

    @FXML
    public void applyFilterSearch(ActionEvent event) {
        StringBuilder query = new StringBuilder("SELECT * FROM person_tax WHERE 1=1");

        if (!idField.getText().isEmpty() && idField.getText().matches("\\d+")) {
            query.append(" AND id = ?");
        }
        else if(idField.getText().matches("\\p{L}++")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Gabim në input");
               alert.setHeaderText(null);
               alert.setContentText("Ju lutem shenoni vetem numra ne fushen e ID!");
               alert.showAndWait(); 
        }
        
        if (!nameField.getText().isEmpty() && nameField.getText().matches("\\p{L}++")) {
            query.append(" AND name LIKE ?");
        }
        else if(nameField.getText().matches(".*\\d.*")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Gabim në input");
               alert.setHeaderText(null);
               alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e emrit!");
               alert.showAndWait(); 
       }

        if (!fatherNameField.getText().isEmpty() && fatherNameField.getText().matches("\\p{L}++")) {
            query.append(" AND fatherName LIKE ?");
        }
        else if(fatherNameField.getText().matches(".*\\d.*")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e emrit babait!");
                alert.showAndWait(); 
        }


        if (!surnameField.getText().isEmpty() && surnameField.getText().matches("\\p{L}++")) {
            query.append(" AND surname LIKE ?");
        }
        else if(surnameField.getText().matches(".*\\d.*")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e mbiemrit!");
                alert.showAndWait(); 
        }

        if (!regionField.getText().isEmpty() && regionField.getText().matches("\\p{L}++")) {
            query.append(" AND region LIKE ?");
        }
        else if(regionField.getText().matches(".*\\d.*")){

                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e vendit!");
                    alert.showAndWait(); 
            }

        if (!phoneNumField.getText().isEmpty() && phoneNumField.getText().matches(".*\\d.*")) {
            query.append(" AND phoneNum LIKE ?");
        }

        ObservableList<Person_tax> filteredData = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (!idField.getText().isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(idField.getText()));
            }
            
            if (!nameField.getText().isEmpty() && nameField.getText().matches("\\p{L}++")) {
                stmt.setString(paramIndex++, "%" + nameField.getText() + "%");
            }
            

            if (!fatherNameField.getText().isEmpty() && fatherNameField.getText().matches("\\p{L}++")) {
                stmt.setString(paramIndex++, "%" + fatherNameField.getText() + "%");
            }
            
            if (!surnameField.getText().isEmpty() && surnameField.getText().matches("\\p{L}++")) {
                stmt.setString(paramIndex++, "%" + surnameField.getText() + "%");
            }
            

            if (!regionField.getText().isEmpty() && regionField.getText().matches("\\p{L}++")) {
                stmt.setString(paramIndex++, "%" + regionField.getText() + "%");
            }
            
            
            if (!phoneNumField.getText().isEmpty() && phoneNumField.getText().matches(".*\\d.*")) {
                stmt.setString(paramIndex++, "%" + phoneNumField.getText() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filteredData.add(new Person_tax(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("fatherName"),
                    rs.getString("surname"),
                    rs.getString("region"),
                    rs.getString("phoneNum")
                ));
            }

            tableViewController.applyFilter(filteredData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        tableViewController.restoreOriginalData();
        // Optionally close the search form if needed
    }


    @FXML
    public void cancelFilter(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

