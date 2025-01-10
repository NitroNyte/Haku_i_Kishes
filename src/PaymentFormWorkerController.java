import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentFormWorkerController {

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
    private TextField releasedFromPaymentField;
    @FXML
    private TextField dataField;
    @FXML
    private TextField paymentDataField;


    

    private int personID;
    private PaymentWorkerController paymentWorkerController;

    // Method to set person details
        public void setPersonDetails(int personID) {
            this.personID = personID;
            loadPersonDetails();
        }

    // Set the PaymentController to update the table when a payment is added
        public void setPaymentWorkerController(PaymentWorkerController paymentWorkerController) {
            this.paymentWorkerController = paymentWorkerController;
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


    @FXML
private void saveToDatabase() {
    String payment = paymentField.getText();
    String releasedFromPayment = releasedFromPaymentField.getText();
    String data = dataField.getText();
    String paymentData = paymentDataField.getText();
    if(payment.matches("\\d+")){
        if(releasedFromPayment.equals("PO") || releasedFromPayment.equals("JO")){
                if(paymentData.trim().matches("\\d{2}/\\d{2}/\\d{4}")){
                    String sql = "INSERT INTO payment_worker(personID, payment, data, paymentData, transactionID, releasedFromPayment) VALUES(?, ?, ?, ?, ?, ?)";

                    try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {

                        int newTransaction = getNextTransaction();

                        stmt.setInt(1, personID);
                        stmt.setString(2, payment);
                        stmt.setString(3, data);
                        stmt.setString(4, paymentData);
                        stmt.setInt(5, newTransaction);
                        stmt.setString(6, releasedFromPayment);
                        stmt.executeUpdate();

                        // Add the new payment to the PaymentController's list
                        Payment_worker newPayment = new Payment_worker(personID, payment, data, paymentData, newTransaction, releasedFromPayment);
                        paymentWorkerController.addPayment(newPayment);

                        // Clear the payment field after saving
                        paymentField.clear();
                        releasedFromPaymentField.clear();
                        dataField.clear();
                        paymentDataField.clear();

                        }   
                            catch (SQLException e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
                            }

                    } 
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Gabim në input");
                        alert.setHeaderText(null);
                        alert.setContentText("Inputi është gabim, duhet të jetë data sipas formatit DD/MM/YYYY dhe vetëm numra");
                        alert.showAndWait(); 
                        paymentDataField.clear();
                    }
                }
                else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Inputi është gabim, duhet të jetë PO ose JO (Të mëdha)");
                    alert.showAndWait(); 
                    paymentField.clear();
                    }
            } 
            else {
                // Show the alert if releasedFromPayment is not "PO" or "JO"
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Inputi është gabim, duhet të jetë vetëm vlera numer");
                alert.showAndWait(); // Ensure the alert is shown
                releasedFromPaymentField.clear();
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
            } 
                catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            }
        return nextId;
    }



    @FXML
    public void cancelPayment(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
