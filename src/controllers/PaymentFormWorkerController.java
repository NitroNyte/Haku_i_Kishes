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

import databaseClasses.Payment_worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentFormWorkerController implements Initializable {

    @FXML
    private TextField NameField;
    @FXML
    private TextField SurNameField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField RegionField;
    @FXML
    private TextField paymentField;
    @FXML
    private ChoiceBox<String> paymentStatusField;
    @FXML
    private TextField dataField;
    @FXML
    private DatePicker paymentDataField;

    private int personID;
    private PaymentWorkerController paymentWorkerController;

    // Method to set person details
    public void setPersonDetails(int personID) {
        this.personID = personID;
        loadPersonDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentStatusField.getItems().addAll("Paguar", "Paguar jo plotësisht", "Liruar nga pagesa");

    }

    // Set the PaymentController to update the table when a payment is added
    public void setPaymentWorkerController(PaymentWorkerController paymentWorkerController) {
        this.paymentWorkerController = paymentWorkerController;
    }

    private void loadPersonDetails() {

        String sql = "SELECT * FROM person_table WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                NameField.setText(rs.getString("name"));
                fatherNameField.setText(rs.getString("fatherName"));
                SurNameField.setText(rs.getString("surname"));
                RegionField.setText(rs.getString("region"));
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo nuk duhët të shfaqet ");
            alert.showAndWait(); // Ensure the alert is shown
        }
    }

    @FXML
    private void saveToDatabase() {
        String payment = paymentField.getText();
        String data = dataField.getText();
        LocalDate paymentDate;
        String formattedDate;
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
            formattedDate = paymentDate.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd")));
        }

        String paymentStatusText = paymentStatusField.getValue();
        if (payment.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            if (data.matches("\\d{4}")) {
                if (formattedDate.trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    String sql = "INSERT INTO payment_worker(personID, payment, data, paymentData, transactionID, paymentStatus) VALUES(?, ?, ?, ?, ?, ?)";

                    try (Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(sql)) {

                        int newTransaction = getNextTransaction();

                        stmt.setInt(1, personID);
                        stmt.setString(2, payment);
                        stmt.setString(3, data);
                        stmt.setString(4, formattedDate);
                        stmt.setInt(5, newTransaction);
                        stmt.setString(6, paymentStatusText);
                        stmt.executeUpdate();

                        // Add the new payment to the PaymentController's list
                        Payment_worker newPayment = new Payment_worker(personID, payment, data, formattedDate,
                                newTransaction,
                                paymentStatusText);
                        paymentWorkerController.addPayment(newPayment);

                        // Clear the payment field after saving
                        paymentField.clear();
                        dataField.clear();

                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Gabim në input");
                        alert.setHeaderText(null);
                        alert.setContentText(
                                "Gabim në program, kjo nuk duhët të shfaqet ");
                        alert.showAndWait(); // Ensure the alert is shown
                    }

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Gabim në formular, viti i dokumentit duhët të jetë dhënë");
                alert.showAndWait();
            }
        }

        else {
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
        String sql = "SELECT MAX(transactionID) FROM payment_worker";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next() && rs.getInt(1) != 0) {
                nextId = rs.getInt(1) + 1; // Increment the last ID
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo nuk duhët të shfaqet ");
            alert.showAndWait(); // Ensure the alert is shown
        }
        return nextId;
    }

    @FXML
    public void cancelPayment(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
