package controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import mainClasses.DatabaseConnection;
import databaseClasses.Payment_tax;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentFormController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surNameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField regionField;
    @FXML
    private TextField paymentField;
    @FXML
    private ChoiceBox<String> paymentStatusField;
    @FXML
    private TextField dataField;
    @FXML
    private DatePicker paymentDataField;

    private int personID;
    private PaymentController paymentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentStatusField.getItems().addAll("Paguar", "Paguar jo plotësisht", "Liruar nga pagesa");

    }

    // Method to set person details
    public void setPersonDetails(int personID) {
        this.personID = personID;
        loadPersonDetails();
    }

    // Set the PaymentController to update the table when a payment is added
    public void setPaymentController(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    private void loadPersonDetails() {
        String sql = "SELECT * FROM person_table WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                fatherNameField.setText(rs.getString("fatherName"));
                surNameField.setText(rs.getString("surname"));
                regionField.setText(rs.getString("region"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Saving to the database is done by this thing
    @FXML
    private void saveToDatabase() {
        String payment = paymentField.getText();
        String data = dataField.getText();
        String paymentStatusText = paymentStatusField.getValue();

        LocalDate paymentDate;
        if (paymentDataField.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Gabim në formular, data e pageses nuk mund të jetë boshe!");
            alert.showAndWait();
            return;
        } else {
            paymentDate = paymentDataField.getValue();
            paymentStatusText = paymentStatusField.getValue();

        }
        String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd")));

        String sql = "INSERT INTO payment_tax(personID, payment, data, paymentData, transactionID, paymentStatus) VALUES(?, ?, ?, ?, ?, ?)";

        if (payment.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            if (data.matches("\\d{4}")) {
                if (formattedDate.trim().matches("\\d{4}-\\d{2}-\\d{2}")) {

                    try (Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement ps = conn.prepareStatement(sql)) {

                        int newTransaction = getNextTransaction();

                        ps.setInt(1, personID);
                        ps.setString(2, payment);
                        ps.setString(3, data);
                        ps.setString(4, formattedDate);
                        ps.setInt(5, newTransaction);
                        ps.setString(6, paymentStatusText);
                        ps.executeUpdate();

                        Payment_tax newPayment = new Payment_tax(personID, payment, data, formattedDate, newTransaction,
                                paymentStatusText);
                        paymentController.addPayment(newPayment);

                        paymentField.clear();
                        dataField.clear();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Kish shku qtu me mir me nje switch case po lyp kogja pun e ski koh
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText(
                            "Inputi është gabim, ju lutem mos shenoni shkronja apo karaktera të tjera");
                    alert.showAndWait();
                    System.out.println("PaymetDatafield problem");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Gabim në formular, viti i dokumentit duhët të jetë dhënë");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Gabim në formular shenoni vëtem numra në kolonen e pagesës, formati euro pastaj centat pas pikës deri në dy vlera (shëmbull 100.25)");
            alert.showAndWait(); // Ensure the alert is shown
            paymentField.clear();
        }

    }

    private int getNextTransaction() {
        int nextId = 1; // Starting ID
        String sql = "SELECT MAX(transactionID) FROM payment_tax";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next() && rs.getInt(1) != 0) {
                nextId = rs.getInt(1) + 1; // Increment the last ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    @FXML
    public void cancelPayment(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

}
