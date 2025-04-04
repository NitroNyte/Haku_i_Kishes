package controllers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import mainClasses.DatabaseConnection;
import databaseClasses.Person_table;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FormViewController {

    private TableViewController tableViewController;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField surNameField;
    @FXML
    private TextField regionField;
    @FXML
    private CheckBox outRegionBox;
    @FXML
    private TextField phoneNumField;

    @FXML
    private ImageView goBack;

    public synchronized void setTableViewController(TableViewController tableViewController) {
        this.tableViewController = tableViewController;
    }

    @FXML
    public synchronized void addPerson(ActionEvent event) {
        String nameFromField = nameField.getText().toLowerCase();
        String fatherNameFromField = fatherNameField.getText().toLowerCase();
        String surnameFromField = surNameField.getText().toLowerCase();
        String regionFromField = regionField.getText().toLowerCase();
        Boolean outRegionState = outRegionBox.isSelected();
        String outRegionText = "";
        String phoneNumFromField = phoneNumField.getText();

        // There is a feature here where you could also make phonenumber required, but
        // that would defeat the restrictions you gave
        // for the program in version 1.0
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

        if (outRegionState) {
            outRegionText = "PO";
        } else {
            outRegionText = "JO";
        }

        saveToDatabase(nameFromField, fatherNameFromField, surnameFromField, regionFromField, outRegionText,
                phoneNumFromField);

        if (tableViewController != null) {
            ObservableList<Person_table> updatedList = DatabaseConnection.getDataUsersPersonTable();
            tableViewController.refreshTableView(updatedList);
        }
    }

    private void saveToDatabase(String name, String fatherName, String surname, String region, String outRegion,
            String phoneNum) {
        String sql = "INSERT INTO person_table(id, name, fatherName, surname, region, outRegion, phoneNum) VALUES(?,?,?,?,?,?,?)";
        String sqlCheckIfPersonExits = "SELECT 1 FROM person_table WHERE name = ? AND surname = ? AND fatherName = ? AND region = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtForPersonExists = conn.prepareStatement(sqlCheckIfPersonExits);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmtForPersonExists.setString(1, name);
            stmtForPersonExists.setString(2, surname);
            stmtForPersonExists.setString(3, fatherName);
            stmtForPersonExists.setString(4, region);
            ResultSet rs = stmtForPersonExists.executeQuery();

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Personi ekziston në databazë");
                alert.setHeaderText(null);
                alert.setContentText("Ky person: " + name + " " + fatherName + " " + surname + " nga " + region
                        + " ekziston në databazë");
                alert.showAndWait(); // Ensure the alert is shown
                return;
            }

            int newId = getNextPersonId(name, surname, fatherName, region); // Get the next ID

            stmt.setInt(1, newId); // Set the new ID
            stmt.setString(2, name);
            stmt.setString(3, fatherName);
            stmt.setString(4, surname);
            stmt.setString(5, region);
            stmt.setString(6, outRegion);
            stmt.setString(7, phoneNum);
            stmt.executeUpdate();

            try {
                conn.close();
                stmt.close();
            } catch (Exception e) {
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
        nameField.clear();
        fatherNameField.clear();
        surNameField.clear();
        regionField.clear();
        phoneNumField.clear();
    }

    @FXML
    public void cancelAddPerson(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private int getNextPersonId(String name, String surname, String fatherName, String region) {
        int nextId = 24000000; // Starting ID
        String sqlPersonTableByName = "SELECT id FROM person_table WHERE name = ? AND surname = ? AND fatherName = ? AND region = ?";
        String sqlMaxPersonTable = "SELECT MAX(id) FROM person_table";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtPersonTable = conn.prepareStatement(sqlPersonTableByName);
                PreparedStatement stmtMaxPersonTable = conn.prepareStatement(sqlMaxPersonTable);) {

            // 1. Check if person exists in person_tax by name, surname, and fatherName
            stmtPersonTable.setString(1, name);
            stmtPersonTable.setString(2, surname);
            stmtPersonTable.setString(3, fatherName);
            stmtPersonTable.setString(4, region);
            ResultSet rsPersonTableByName = stmtPersonTable.executeQuery();

            if (rsPersonTableByName.next()) {
                int existingId = rsPersonTableByName.getInt("id");
                return existingId;
            }

            // 2. Get the next available ID from person_tax
            ResultSet rsMaxPersonTable = stmtMaxPersonTable.executeQuery();
            if (rsMaxPersonTable.next() && rsMaxPersonTable.getInt(1) != 0) {
                nextId = rsMaxPersonTable.getInt(1) + 1; // Increment the last ID in person_tax
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo nuk duhët të shfaqët!");
            alert.showAndWait(); // Ensure the alert is shown
        }
        return nextId;
    }

}
