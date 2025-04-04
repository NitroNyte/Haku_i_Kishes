package controllers;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseClasses.Person_table;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainClasses.DatabaseConnection;

public class EditPersonChurchTaxController {

    private TableViewController tableViewController;

    @FXML
    private TextField NameField;
    @FXML
    private TextField SurNameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField RegionField;
    @FXML
    private CheckBox outRegionBox;
    @FXML
    private TextField phoneNumField;

    private int personID;

    public synchronized void setTableViewController(TableViewController tableViewController) {
        this.tableViewController = tableViewController;
    }

    public void setPersonDetails(int personID) {
        this.personID = personID;
        loadPersonDetails();
    }

    private void loadPersonDetails() {

        String sql = "SELECT * FROM person_table WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                NameField.setText(Character.toUpperCase(rs.getString("name").charAt(0))
                        + rs.getString("name").substring(1).toLowerCase());
                fatherNameField.setText(Character.toUpperCase(rs.getString("fatherName").charAt(0))
                        + rs.getString("fatherName").substring(1).toLowerCase());
                SurNameField.setText(Character.toUpperCase(rs.getString("surname").charAt(0))
                        + rs.getString("surname").substring(1).toLowerCase());
                        RegionField.setText(Character.toUpperCase(rs.getString("region").charAt(0))
                        + rs.getString("region").substring(1).toLowerCase());
                outRegionBox.setSelected("PO".equals(rs.getString("outRegion")));
                phoneNumField.setText(rs.getString("phoneNum"));
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
            alert.showAndWait(); // Ensure the alert is shown
        }
    }

    public void saveChanges() {
        String nameFromField = NameField.getText().toLowerCase();
        String fatherNameFromField = fatherNameField.getText().toLowerCase();
        String surnameFromField = SurNameField.getText().toLowerCase();
        String regionFromField = RegionField.getText().toLowerCase();
        String outRegionState = outRegionBox.isSelected() ? "PO" : "JO";
        String phoneNumFromField = phoneNumField.getText();

        if (nameFromField.isEmpty() || surnameFromField.isEmpty() || regionFromField.isEmpty()
                || fatherNameFromField.isEmpty()
                || !nameFromField.matches("[\\p{L}\\s]+") || !fatherNameFromField.matches("[\\p{L}\\s]+")
                || !regionFromField.matches("[\\p{L}\\s]+") || !surnameFromField.matches("[\\p{L}\\s]+")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText(
                    "Ju lutëm plotësoni të gjitha fushat dhe vetëm përdorni shkronja (përveç fushës per numrin e telefonit)!");
            alert.show();
            return;
        }

        updateToDatabaseForWorker(nameFromField, fatherNameFromField, surnameFromField, regionFromField, outRegionState,
                phoneNumFromField, personID);

        ObservableList<Person_table> updatedList = DatabaseConnection.getDataUsersPersonTable();
        tableViewController.refreshTableView(updatedList);

    }

    public void updateToDatabaseForWorker(String name, String fatherName, String surname, String region,
            String outRegionState,
            String phoneNum, int id) {
        String updatePersonQuery = "UPDATE person_table set name = ?, fatherName = ?, surname = ?, region = ?, outRegion = ?, phoneNum = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(updatePersonQuery)) {

            stmt.setString(1, name);
            stmt.setString(2, fatherName);
            stmt.setString(3, surname);
            stmt.setString(4, region);
            stmt.setString(5, outRegionState);
            stmt.setString(6, phoneNum);
            stmt.setInt(7, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void cancelEdit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
