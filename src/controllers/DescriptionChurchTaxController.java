package controllers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainClasses.DatabaseConnection;

public class DescriptionChurchTaxController {

    @FXML
    private Text userName;

    @FXML
    private TextArea description;

    @FXML
    private Text infoText;

    private int personID;
    private int transactionID;
    @SuppressWarnings("unused")
    private String data;
    @SuppressWarnings("unused")
    private String descriptionText;
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String surname;
    @SuppressWarnings("unused")
    private String fatherName;

    public void setPersonIDForChurchTax(int personID, String data, String description, int transactionID) {
        this.personID = personID;
        this.transactionID = transactionID;
        this.data = data;
        descriptionText = description;
        loadDataForPerson();
        loadDataForPersonName();

    }

    public void loadDataForPerson() {
        String sql = "SELECT payment, data, paymentStatus, notice FROM payment_tax WHERE transactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                infoText.setText("Me pagesë: " + rs.getString("payment") + "€, rreth hakut të kishës "
                        + rs.getString("data") + ", me status: " + rs.getString("paymentStatus"));
                description.setText(rs.getString("notice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataForPersonName() {

        String sql = "SELECT name, surname, fatherName FROM person_table WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                userName.setText(
                        Character.toUpperCase(rs.getString("name").charAt(0))
                                + rs.getString("name").substring(1).toLowerCase() + " "
                                + Character.toUpperCase(rs.getString("fatherName").charAt(0))
                                + rs.getString("fatherName").substring(1).toLowerCase() + " "
                                + Character.toUpperCase(rs.getString("surname").charAt(0))
                                + rs.getString("surname").substring(1).toLowerCase());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveToPerson(ActionEvent event) {
        if (description.getText() != null) {
            description.setStyle("-fx-border-color: green; -fx-border-width: 4px;");
        }
        String sql = "UPDATE payment_tax SET notice = ? WHERE transactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String getTextFromField = description.getText();

            stmt.setString(1, getTextFromField);
            stmt.setInt(2, transactionID);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelNotice(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
