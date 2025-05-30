package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import mainClasses.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseClasses.Person_table;

public class SearchFormTombTaxController {

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
    private CheckBox outRegionBox;


    @FXML
    private Button applyFilterButton;

    private TombTaxController tombTaxController;

    public void setTombTaxController(TombTaxController tombTaxController) {
        this.tombTaxController = tombTaxController;
    }

    @FXML
    public void applyFilterSearch(ActionEvent event) {
        StringBuilder query = new StringBuilder("SELECT * FROM person_table WHERE 1=1");

        if (!idField.getText().trim().isEmpty() && idField.getText().matches("\\d+")) {
            query.append(" AND id = ?");
        } else if (idField.getText().matches("\\p{L}+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Ju lutem shenoni vetem numra ne fushen e ID!");
            alert.showAndWait();
        }

        if (!nameField.getText().trim().isEmpty() && nameField.getText().toLowerCase().matches("\\p{L}+")) {
            query.append(" AND name LIKE ?");
        } else if (nameField.getText().matches("[0-9]")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e emrit!");
            alert.showAndWait();
        }

        if (!fatherNameField.getText().trim().isEmpty() && fatherNameField.getText().toLowerCase().matches("\\p{L}+")) {
            query.append(" AND fatherName LIKE ?");
        } else if (fatherNameField.getText().matches(".*\\d.*")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e emrit babait!");
            alert.showAndWait();
        }

        if (!surnameField.getText().trim().isEmpty() && surnameField.getText().toLowerCase().matches("\\p{L}+")) {
            query.append(" AND surname LIKE ?");
        } else if (surnameField.getText().matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Ju lutem shenoni vetem shkronja ne fushen e mbiemrit!");
            alert.showAndWait();
        }

        if (!regionField.getText().trim().isEmpty() && regionField.getText().toLowerCase().matches("\\p{L}+")) {
            query.append(" AND region LIKE ?");
        } else if (regionField.getText().matches(".*\\d.*")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Ju lutem shenoni vetem shkronja në fushën e vendit!");
            alert.showAndWait();
        }


        if (outRegionBox.isSelected()) {
            query.append(" AND outRegion = ?");
        }

        ObservableList<Person_table> filteredData = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (!idField.getText().trim().isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(idField.getText().toLowerCase()));
            }

            if (!nameField.getText().trim().isEmpty() && nameField.getText().matches("\\p{L}+")) {
                stmt.setString(paramIndex++, "%" + nameField.getText().toLowerCase() + "%");
            }

            if (!fatherNameField.getText().trim().isEmpty() && fatherNameField.getText().matches("\\p{L}+")) {
                stmt.setString(paramIndex++, "%" + fatherNameField.getText().toLowerCase() + "%");
            }

            if (!surnameField.getText().trim().isEmpty() && surnameField.getText().matches("\\p{L}+")) {
                stmt.setString(paramIndex++, "%" + surnameField.getText().toLowerCase() + "%");
            }

            if (!regionField.getText().trim().isEmpty() && regionField.getText().matches("\\p{L}+")) {
                stmt.setString(paramIndex++, "%" + regionField.getText().toLowerCase() + "%");
            }

            if (outRegionBox.isSelected()) {
                stmt.setString(paramIndex++, "PO");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                filteredData.add(new Person_table(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("fatherName"),
                        rs.getString("surname"),
                        rs.getString("region"),
                        rs.getString("outRegion"),
                        rs.getString("phoneNum")));
            }

            tombTaxController.applyFilter(filteredData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        tombTaxController.restoreOriginalData();
    }

    @FXML
    public void cancelFilter(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
