import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PaymentController {

    @FXML
    private TableView<Payment_tax> Payment_table;

    @FXML
    private TableColumn<Payment_tax, Integer> col_id;
    @FXML
    private TableColumn<Payment_tax, String> col_payment;
    @FXML
    private TableColumn<Payment_tax, String> col_data;
    @FXML
    private TableColumn<Payment_tax, String> col_paymentData;
    @FXML
    private TableColumn<Payment_tax, Integer> col_transaction;
    @FXML
    private TableColumn<Payment_tax, String> col_releasedFromPayment;

    @FXML
    private Text userName;

    public ObservableList<Payment_tax> paymentList;
    private int personID;
    private String name;
    private String surname;

    private String region;

    private String phoneNum;

    private String fatherName;

    

    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Payment_tax, Integer>("personID"));
        col_payment.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("payment"));
        col_data.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("data"));
        col_paymentData.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<Payment_tax, Integer>("transactionID"));
        col_releasedFromPayment.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("releasedFromPayment"));


        col_payment.setCellFactory(column -> {
            return new TableCell<Payment_tax, String>() {
                @Override
                protected void updateItem(String payment, boolean empty) {
                    super.updateItem(payment, empty);
                    if (empty || payment == null) {
                        setText(null); // Clear text for empty cells
                    } else {
                        setText(payment + " €"); // Append euro sign to the payment value
                    }
                }
            };
        });

        paymentList = DatabaseConnection.getDataUsersPaymentTable();
        Payment_table.setItems(paymentList);
    }

    // Method to set personID and load payments
    public void setPersonDetails(int personID, String name,String fatherName, String surname, String region, String phoneNum) {
        this.personID = personID;
        this.name = name;
        this.fatherName = fatherName;
        this.surname = surname;
        this.region = region;
        this.phoneNum = phoneNum;
        loadPaymentsForPerson();
    }


   
    private void loadPaymentsForPerson() {
        String sql = "SELECT * FROM payment_tax WHERE personID = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();
    
            paymentList.clear();
            while (rs.next()) {
                Payment_tax payment = new Payment_tax(
                    rs.getInt("personID"),
                    rs.getString("payment"),
                    rs.getString("data"),
                    rs.getString("paymentData"),
                    rs.getInt("transactionID"),
                    rs.getString("releasedFromPayment")
                );
                paymentList.add(payment);
            }
    
            Payment_table.setItems(paymentList);



            if (paymentList.isEmpty()) {
                userName.setText("Nuk ka pagesa të kryera nga: " + name + " " + surname);
            } else {
                userName.setText("Pagesat nga: " + name + " " + surname);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            
        }
    }
    

    // Method to open the PaymentForm with details of the selected person
    public void openPaymentForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormPayment.fxml"));
            Parent root = loader.load();

            PaymentFormController paymentFormController = loader.getController();
            paymentFormController.setPersonDetails(personID);
            paymentFormController.setPaymentController(this); // Pass the PaymentController instance

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            
        }
    }

    // Method to add a payment and refresh the table
    public void addPayment(Payment_tax newPayment) {
        paymentList.add(newPayment);
        Payment_table.refresh();


        if (paymentList.isEmpty()) {
            userName.setText("Nuk ka pagesa të kryera nga: " + name + " " + surname);
        } else {
            userName.setText("Pagesat nga: " + name + " " + surname);
        }
    }

    @FXML
    public void goBackHome(MouseEvent event) {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("TransactionPanel.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            
        }
    }





    @FXML
public void deletePayment(ActionEvent event) {
    Payment_tax selectedPayment = Payment_table.getSelectionModel().getSelectedItem();

    if (selectedPayment != null) {
        int transactionID = selectedPayment.getTransactionID();

        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmo fshirjen");
        alert.setHeaderText(null);
        alert.setContentText("A jeni të sigurt që deshironi të fshini pagesën?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete the payment from the database
                DatabaseConnection.deletePaymentFromDatabase(transactionID);

               
                paymentList = DatabaseConnection.getPaymentsFor(personID); // Retrieve fresh data from the database
                
                // Set the updated data in the TableView
                Payment_table.setItems(paymentList);

               
                Payment_table.refresh();

                if (paymentList.isEmpty()) {
                    userName.setText("Nuk ka pagesa të kryera nga: " + name + " " + surname);
                } else {
                    userName.setText("Pagesat nga: " + name + " " + surname);
                }

                
            } catch (Exception e) {
                e.printStackTrace();
                
                
            }
        }
    } else {
        // Show error alert if no payment was selected
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Nuk keni zgjedhur pagesë për fshirje!");
        alert.showAndWait();
    }
}




    @FXML
private void documentFormSwitch(ActionEvent event){
      
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ChurchDocumentPanel.fxml"));
                    Parent root = loader.load();

                    ChurchDocumentPrinter churchDocumentPrinter = loader.getController();
                    churchDocumentPrinter.setPersonDetails(personID);
                    churchDocumentPrinter.setPaymentController(this);

                    //This is the logic so that hte tableviewcontroller is referencing this mf
                    // Get the controller instance and set the TableViewController

                    Stage newStage = new Stage();
                    newStage.setTitle("Dokumenti");
                    Scene scene = new Scene(root);
                    newStage.setScene(scene);
                    newStage.show();
                } 
                    catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
                
                }
            }
    



}
