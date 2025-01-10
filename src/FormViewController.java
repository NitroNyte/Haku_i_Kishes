import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FormViewController {

    private TableViewController tableViewController;
    @FXML
    private TextField idField;
    @FXML
    private TextField NameField;
    @FXML
    private TextField FatherNameField;
    @FXML
    private TextField SurNameField;
    @FXML
    private TextField RegionField;
    @FXML
    private TextField PhoneNumField;








    @FXML
    private ImageView goBack;

    
    public synchronized void setTableViewController(TableViewController tableViewController) {
        this.tableViewController = tableViewController;
    }



    @FXML
    public synchronized void addPerson(ActionEvent event) {
        String nameFromField = NameField.getText();
        String fatherNameFromField = FatherNameField.getText();
        String surnameFromField = SurNameField.getText();
        String regionFromField = RegionField.getText();
        String phoneNumFromField = PhoneNumField.getText();
    
        //There is a feature here where you could also make phonenumber required, but that would defeat the restrictions you gave
        //for the program in version 1.0    
        if (nameFromField.isEmpty() || surnameFromField.isEmpty() || regionFromField.isEmpty() || fatherNameFromField.isEmpty()
        || !nameFromField.matches("[\\p{L}\\s]+") || !fatherNameFromField.matches("[\\p{L}\\s]+") || !regionFromField.matches("[\\p{L}\\s]+") || !surnameFromField.matches("[\\p{L}\\s]+")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Ju lutëm plotësoni të gjitha fushat dhe vetëm përdorni shkronja (përveç fushës per numrin e telefonit)!");
            alert.show();
            return;
        }
    
        saveToDatabase(nameFromField,fatherNameFromField, surnameFromField, regionFromField, phoneNumFromField);
    
        if (tableViewController != null) {
            ObservableList<Person_tax> updatedList = DatabaseConnection.getDataUsersPersonTable();
            tableViewController.refreshTableView(updatedList);
        }
    }
    

    private void saveToDatabase(String name, String fatherName, String surname, String region, String phoneNum) {
        String sql = "INSERT INTO person_tax(id, name, fatherName, surname, region, phoneNum) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            int newId = getNextPersonId(name, surname, fatherName); // Get the next ID
    
            stmt.setInt(1, newId); // Set the new ID
            stmt.setString(2, name);
            stmt.setString(3, fatherName);
            stmt.setString(4, surname);
            stmt.setString(5, region);
            stmt.setString(6, phoneNum);
            stmt.executeUpdate();

            try{
                conn.close();
                stmt.close();
            }
            catch(Exception e){
                System.err.println(e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown // Handle exception appropriately
        }
       
        
        // Clear the fields after saving
        NameField.clear();
        FatherNameField.clear();
        SurNameField.clear();
        RegionField.clear();
        PhoneNumField.clear();
    }
    

    @FXML
    public void cancelAddPerson(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    private int getNextPersonId(String name, String surname, String fatherName) {
        int nextId = 24000000; // Starting ID
        String sqlPersonWorkerByName = "SELECT id FROM person_worker WHERE name = ? AND surname = ? AND fatherName = ?";
        String sqlMaxPersonTax = "SELECT MAX(id) FROM person_tax";
        String sqlCheckPersonTaxById = "SELECT id FROM person_worker WHERE id = ?";
        String sqlCheckPersonWorkerById = "SELECT id FROM person_tax WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtPersonTax = conn.prepareStatement(sqlPersonWorkerByName);
             PreparedStatement stmtMaxPersonTax = conn.prepareStatement(sqlMaxPersonTax);
             PreparedStatement stmtCheckPersonTaxById = conn.prepareStatement(sqlCheckPersonTaxById);
             PreparedStatement stmtCheckPersonWorkerById = conn.prepareStatement(sqlCheckPersonWorkerById)) {
    
            // 1. Check if person exists in person_tax by name, surname, and fatherName
            stmtPersonTax.setString(1, name);
            stmtPersonTax.setString(2, surname);
            stmtPersonTax.setString(3, fatherName);
            ResultSet rsPersonTaxByName = stmtPersonTax.executeQuery();
    
            if (rsPersonTaxByName.next()) {
                // Person exists in person_tax, assign the same ID
                int existingId = rsPersonTaxByName.getInt("id");
    
                // Check if this ID already exists in person_worker
                stmtCheckPersonWorkerById.setInt(1, existingId);
                ResultSet rsCheckPersonWorkerById = stmtCheckPersonWorkerById.executeQuery();
                if (!rsCheckPersonWorkerById.next()) {
                    // Person with this ID does not exist in person_worker, return the same ID
                    return existingId; // Return the existingId only if it's not in person_worker
                }
                // If it exists in person_worker, we will generate a new ID below
            }
    
            // 2. Get the next available ID from person_tax
            ResultSet rsMaxPersonTax = stmtMaxPersonTax.executeQuery();
            if (rsMaxPersonTax.next() && rsMaxPersonTax.getInt(1) != 0) {
                nextId = rsMaxPersonTax.getInt(1) + 1; // Increment the last ID in person_tax
            }
    
            // 3. Ensure the new ID is not used in person_worker or person_tax
            while (true) {
                // Check if the ID exists in person_worker
                stmtCheckPersonWorkerById.setInt(1, nextId);
                ResultSet rsCheckPersonWorkerByIdAgain = stmtCheckPersonWorkerById.executeQuery();
                if (!rsCheckPersonWorkerByIdAgain.next()) {
                    // Check if the ID exists in person_tax
                    stmtCheckPersonTaxById.setInt(1, nextId);
                    ResultSet rsCheckPersonTaxByIdAgain = stmtCheckPersonTaxById.executeQuery();
                    if (!rsCheckPersonTaxByIdAgain.next()) {
                        // The ID is not used in person_worker or person_tax, break the loop
                        break;
                    }
                }
                nextId++; // If the ID is used in either table, increment and check again
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
        }
        return nextId;
    }






    


}
