import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.collections.FXCollections;
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

public class PaymentWorkerController {

    @FXML
    private TableView<Payment_worker> Payment_table;

    @FXML
    private TableColumn<Payment_worker, Integer> col_id;
    @FXML
    private TableColumn<Payment_worker, String> col_payment;
    @FXML
    private TableColumn<Payment_worker, String> col_data;
    @FXML
    private TableColumn<Payment_worker, String> col_paymentData;
    @FXML
    private TableColumn<Payment_worker, Integer> col_transaction;
    @FXML
    private TableColumn<Payment_worker, String> col_releasedFromPayment;

    @FXML
    private Text userName;

    public ObservableList<Payment_worker> paymentList;
    private int personID;
    private String name;
    private String surname;

    private String region;

    private String phoneNum;

    private String fatherName;


    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Payment_worker, Integer>("personID"));
        col_payment.setCellValueFactory(new PropertyValueFactory<Payment_worker, String>("payment"));
        col_data.setCellValueFactory(new PropertyValueFactory<Payment_worker, String>("data"));
        col_paymentData.setCellValueFactory(new PropertyValueFactory<Payment_worker, String>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<Payment_worker, Integer>("transactionID"));
        col_releasedFromPayment.setCellValueFactory(new PropertyValueFactory<Payment_worker, String>("releasedFromPayment"));


        col_payment.setCellFactory(column -> {
            return new TableCell<Payment_worker, String>() {
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
        

        paymentList = DatabaseConnection.getDataUsersPaymentWorkerTable();
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
        String sql = "SELECT * FROM payment_worker WHERE personID = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();
    
            paymentList.clear();
            while (rs.next()) {
                Payment_worker payment = new Payment_worker(
                    rs.getInt("personID"),
                    rs.getString("payment"),
                    rs.getString("data"),
                    rs.getString("paymentData"),
                    rs.getInt("transactionID"),
                    rs.getString("releasedFromPayment")
                );
                paymentList.add(payment);
            }
    
            if (paymentList.isEmpty()) {        
                userName.setText("Nuk ka pagesa të kryera nga: " + name + " " + surname);
            } else {
                userName.setText("Pagesat nga: " + name + " " + surname);
            }
    
            Payment_table.setItems(paymentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Method to open the PaymentForm with details of the selected person
    public void openPaymentForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormPaymentWorker.fxml"));
            Parent root = loader.load();

            PaymentFormWorkerController paymentFormWorkerController = loader.getController();
            paymentFormWorkerController.setPersonDetails(personID);
            paymentFormWorkerController.setPaymentWorkerController(this); // Pass the PaymentController instance

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a payment and refresh the table
    public void addPayment(Payment_worker newPayment) {
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
            Parent homeRoot = FXMLLoader.load(getClass().getResource("TombTaxPanel.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @FXML
    public void deletePayment(ActionEvent event) {
        Payment_worker selectedPayment = Payment_table.getSelectionModel().getSelectedItem();
    
        if (selectedPayment != null) {
            int transactionID = selectedPayment.getTransactionID();
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this payment?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    DatabaseConnection.deletePaymentForWorkerFromDatabase(transactionID);

               
                    paymentList = DatabaseConnection.getPaymentsForWorkerRefreshed(personID); // Retrieve fresh data from the database
                
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No payment selected for deletion.");
            alert.showAndWait();
        }
    }
    



    @FXML
private void documentFormSwitch(ActionEvent event){
      
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ChurchDocumentWorkerPanel.fxml"));
                    Parent root = loader.load();

                    ChurchDocumentWorkerPrinter churchDocumentWorkerPrinter = loader.getController();
                    churchDocumentWorkerPrinter.setPersonDetails(personID);
                    churchDocumentWorkerPrinter.setPaymentWorkerController(this);

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
                }
            }
    



}
