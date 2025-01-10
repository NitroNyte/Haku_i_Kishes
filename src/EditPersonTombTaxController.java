import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPersonTombTaxController {

    private TombTaxController tombTaxController;
    

    @FXML
    private TextField NameField;
    @FXML
    private TextField SurNameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField RegionField;
    @FXML
    private TextField phoneNumField;
                    
                    
                    
    private int personID;
                        
                    
        public synchronized void setTombTaxController(TombTaxController tombTaxController) {
            this.tombTaxController = tombTaxController;
        }
                    
                    
        public void setPersonDetails(int personID) {
            this.personID = personID;
            loadPersonDetails();
        }
                    
                    
                    
        private void loadPersonDetails() {
                    
            String sql = "SELECT * FROM person_worker WHERE id = ?";
                    
                try (Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                            stmt.setInt(1, personID);
                            ResultSet rs = stmt.executeQuery();
                    
                                    if (rs.next()) {
                                        NameField.setText(rs.getString("name"));
                                        fatherNameField.setText(rs.getString("fatherName"));
                                        SurNameField.setText(rs.getString("surname"));
                                        RegionField.setText(rs.getString("region"));
                                        phoneNumField.setText(rs.getString("phoneNum"));
                                    }
                    
                                } 
                                catch (SQLException e) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Gabim në input");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                                        alert.showAndWait(); // Ensure the alert is shown
                                }
                        }
                    
        public void saveChanges(){
            String nameFromField = NameField.getText();
            String fatherNameFromField = fatherNameField.getText();
            String surnameFromField = SurNameField.getText();
            String regionFromField = RegionField.getText();
            String phoneNumFromField = phoneNumField.getText();

            if (nameFromField.isEmpty() || surnameFromField.isEmpty() || regionFromField.isEmpty() || fatherNameFromField.isEmpty()
        || !nameFromField.matches("[\\p{L}\\s]+") || !fatherNameFromField.matches("[\\p{L}\\s]+") || !regionFromField.matches("[\\p{L}\\s]+") || !surnameFromField.matches("[\\p{L}\\s]+")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Ju lutëm plotësoni të gjitha fushat dhe vetëm përdorni shkronja (përveç fushës per numrin e telefonit)!");
            alert.show();
            return;
        }


        updateToDatabaseForWorker(nameFromField, fatherNameFromField, surnameFromField, regionFromField, phoneNumFromField, personID);
        
        
        
            ObservableList<Person_worker> updatedList = DatabaseConnection.getDataUsersPersonTableWorker();
            tombTaxController.refreshTableView(updatedList);
        

    }

    public void updateToDatabaseForWorker(String name, String fatherName, String surname, String region, String phoneNum, int id){
        String updatePersonQuery = "UPDATE person_worker set name = ?, fatherName = ?, surname = ?, region = ?, phoneNum = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(updatePersonQuery)) {

            stmt.setString(1, name);
            stmt.setString(2, fatherName);
            stmt.setString(3, surname);
            stmt.setString(4, region);
            stmt.setString(5, phoneNum);
            stmt.setInt(6, id);
            stmt.executeUpdate();

            
        } catch (Exception e) {
            // TODO: handle exception
        }


    }



    @FXML
    public void cancelEdit(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



   
}
