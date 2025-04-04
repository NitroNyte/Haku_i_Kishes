package controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import mainClasses.DatabaseConnection;
import databaseClasses.ChurchDocumentUser;
import databaseClasses.Payment_tax;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
    private TableColumn<Payment_tax, String> col_paymentStatus;

    @FXML
    private Text userName;

    private ObservableList<Payment_tax> paymentList;
    private ObservableList<Payment_tax> selectedChurchUsers = FXCollections.observableArrayList();

    private int personID;
    private String name;
    private String surname;
    private String region;
    private String phoneNum;
    private int transactionID;

    private String fatherName;

    @FXML
    private Rectangle rectShape;

    @FXML
    private Pane rectPane;

    @SuppressWarnings("unused")
    public void initialize() {

        col_id.setCellValueFactory(new PropertyValueFactory<Payment_tax, Integer>("personID"));
        col_payment.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("payment"));
        col_data.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("data"));
        col_paymentData.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<Payment_tax, Integer>("transactionID"));
        col_paymentStatus.setCellValueFactory(new PropertyValueFactory<Payment_tax, String>("paymentStatus"));

        Payment_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        // Ensures no extra space

        col_id.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));
        col_payment.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));
        col_data.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));
        col_paymentData.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));
        col_transaction.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));
        col_paymentStatus.prefWidthProperty().bind(Bindings.divide(Payment_table.widthProperty(), 6));

        rectShape.heightProperty().bind(rectPane.heightProperty().multiply(2));

        col_payment.setCellFactory(column -> {
            return new TableCell<Payment_tax, String>() {
                @Override
                protected void updateItem(String payment, boolean empty) {
                    super.updateItem(payment, empty);
                    if (empty || payment == null) {
                        setText(null); // Clear text for empty cells
                    } else {
                        setText(formatCurrency(payment) + " €"); // Format and add €
                    }
                }
            };
        });

        paymentList = DatabaseConnection.getDataUsersPaymentTable();
        Payment_table.setItems(paymentList);

        Payment_table.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Payment_tax>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    selectedChurchUsers.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    selectedChurchUsers.removeAll(change.getRemoved());
                }
            }
        });

        Payment_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    // Method to set personID and load payments
    public void setPersonDetails(int personID, String name, String fatherName, String surname, String region,
            String phoneNum) {
        this.personID = personID;
        this.name = name;
        this.fatherName = fatherName;
        this.surname = surname;
        this.region = region;
        this.phoneNum = phoneNum;
        loadPaymentsForPerson();
    }

    private void loadPaymentsForPerson() {
        String sql = "SELECT * FROM payment_tax WHERE personID = ? ORDER BY paymentData DESC";

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
                        rs.getString("paymentStatus"));
                paymentList.add(payment);
            }

            Payment_table.setItems(paymentList);

            if (paymentList.isEmpty()) {
                userName.setText("Nuk ka pagesa të kryera nga: " + Character.toUpperCase(name.charAt(0))
                        + name.substring(1).toLowerCase() + " " + Character.toUpperCase(surname.charAt(0))
                        + surname.substring(1).toLowerCase());
            } else {
                userName.setText(
                        "Pagesat nga: " + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase() + " "
                                + Character.toUpperCase(surname.charAt(0)) + surname.substring(1).toLowerCase());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormPayment.fxml"));
            Parent root = loader.load();

            PaymentFormController paymentFormController = loader.getController();
            paymentFormController.setPersonDetails(personID);
            // Pass the PaymentController instance, without this adding will not work
            paymentFormController.setPaymentController(this);

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
            userName.setText("Nuk ka pagesa të kryera nga: " + Character.toUpperCase(name.charAt(0))
                    + name.substring(1).toLowerCase() + " " + Character.toUpperCase(surname.charAt(0))
                    + surname.substring(1).toLowerCase());
        } else {
            userName.setText("Pagesat nga: " + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase()
                    + " " + Character.toUpperCase(surname.charAt(0)) + surname.substring(1).toLowerCase());
        }
    }

    @FXML
    public void goBackHome(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/TransactionPanel.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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

                    paymentList = DatabaseConnection.getPaymentsFor(personID);

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

    public void openDescription() {
        Payment_tax selectedTax = Payment_table.getSelectionModel().getSelectedItem();

        if (selectedTax != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DescriptionPanel.fxml"));
                Parent root = loader.load();

                DescriptionChurchTaxController descriptionChurchTaxController = loader.getController();
                descriptionChurchTaxController.setPersonIDForChurchTax(selectedTax.getPersonID(), selectedTax.getData(),
                        selectedTax.getNotice(), selectedTax.getTransactionID());

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
    }

    @FXML
    private void documentFormSwitch(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChurchDocumentPanel.fxml"));
            Parent root = loader.load();
            ObservableList<ChurchDocumentUser> records = FXCollections.observableArrayList();
            
            for (Payment_tax payment : selectedChurchUsers) {
                ChurchDocumentUser userToBePassed = new ChurchDocumentUser(payment.getData(), payment.getPayment(),
                        payment.getPaymentData(), payment.getTransactionID(), payment.getPaymentStatus());
                        records.add(userToBePassed);
            }


            ChurchDocumentPrinter churchDocumentPrinter = loader.getController();
            churchDocumentPrinter.setPersonDetails(personID);
            churchDocumentPrinter.setSelectedRecords(records);
            churchDocumentPrinter.setPaymentController(this);

            /*
             * This is the logic so that hte tableviewcontroller is referencing this mf.
             * Get the controller instance and set the TableViewController
             * Without the this keyword you cannot pass the controller to the other one to
             * communicate
             */

            Stage newStage = new Stage();
            newStage.setTitle("Dokumenti");
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
            alert.showAndWait();

        }
    }

    private String formatCurrency(String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            double number = Double.parseDouble(value);
            return String.format("%.2f", number);
        } catch (NumberFormatException e) {
            return value;
        }
    }

}
