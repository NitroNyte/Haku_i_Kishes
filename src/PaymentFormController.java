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

public class PaymentFormController {

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
    private PaymentController paymentController;

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
        String sql = "SELECT * FROM person_tax WHERE id = ?";
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
            e.printStackTrace();
        }
    }


    //Saving to the database is done by this thing
    @FXML
    private void saveToDatabase() {
        String payment = paymentField.getText();
        String data = dataField.getText();
        String paymentData = paymentDataField.getText();
        String releasedFromPayment = releasedFromPaymentField.getText();

        String sql = "INSERT INTO payment_tax(personID, payment, data, paymentData, transactionID, releasedFromPayment) VALUES(?, ?, ?, ?, ?, ?)";

        if(payment.matches("\\d+")){
            if(releasedFromPayment.equals("PO") || releasedFromPayment.equals("JO")){
                if(data.matches("\\d{4}")){
                    if(paymentData.trim().matches("\\d{2}/\\d{2}/\\d{4}")){

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
            Payment_tax newPayment = new Payment_tax(personID, payment, data, paymentData, newTransaction, releasedFromPayment);
            paymentController.addPayment(newPayment);

            // Clear the payment, paymentData, date and released from payment field after saving
            paymentField.clear();
            paymentDataField.clear();
            releasedFromPaymentField.clear();
            dataField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
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
                alert.setContentText("Inputi është gabim, duhet të jetë datë P.SH. 20xx");
                alert.showAndWait(); // Ensure the alert is shown
                dataField.clear();

            }

        }

            else{
                // Show the alert if releasedFromPayment is not "PO" or "JO"
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Gabim në input");
                alert.setHeaderText(null);
                alert.setContentText("Inputi është gabim, duhet të jetë PO ose JO (Të mëdha)");
                alert.showAndWait(); // Ensure the alert is shown
                releasedFromPaymentField.clear();
            }
    }

        else {
                    // Show the alert if releasedFromPayment is not "PO" or "JO"
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Inputi është gabim, duhet të jetë vetem vlera numer");
                    alert.showAndWait(); // Ensure the alert is shown
                    releasedFromPaymentField.clear();
                }

        }

            
   


   
  

    private int getNextTransaction() {
        int nextId = 1; // Starting ID
        String sql = "SELECT MAX(transactionID) FROM payment_tax";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next() && rs.getInt(1) != 0) {
                nextId = rs.getInt(1) + 1; // Increment the last ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }


    @FXML
    public void cancelPayment(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
