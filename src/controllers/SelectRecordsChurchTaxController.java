package controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import mainClasses.DatabaseConnection;

import databaseClasses.ChurchDocumentUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SelectRecordsChurchTaxController implements Initializable {
    @FXML
    private TableView<ChurchDocumentUser> Payment_table;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_year;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_payment;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_date;

    @FXML
    private TableColumn<ChurchDocumentUser, Integer> col_transaction;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_paymentStatus;

    public ObservableList<ChurchDocumentUser> SelectedPersonDetailsOriginalList;

    public ObservableList<ChurchDocumentUser> SelectedPersonDetailsEditedList;

    @SuppressWarnings("unused")
    private PaymentController paymentController;

    private int personID;
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String fatherName;
    @SuppressWarnings("unused")
    private String surname;
    @SuppressWarnings("unused")
    private String phoneNum;
    @SuppressWarnings("unused")
    private String region;

    public void setPersonDetails(int personID) {
        this.personID = personID;
  
    }

    public void setPaymentController(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        col_year.setCellValueFactory(new PropertyValueFactory<>("data"));
        col_payment.setCellValueFactory(new PropertyValueFactory<>("payment"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        col_paymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        col_payment.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String payment, boolean empty) {
                    super.updateItem(payment, empty);
                    if (empty || payment == null) {
                        setText(null); 
                    } else {
                        setText(payment + " €");
                    }
                }
            };

        });
        SelectedPersonDetailsOriginalList = loadDocumentForPerson();
        Payment_table.setItems(SelectedPersonDetailsOriginalList);

        Payment_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && Payment_table.getSelectionModel().getSelectedItem() != null) {
                ChurchDocumentUser selectedPerson = Payment_table.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    addElement();
                }
            }
        });

    }

    private ObservableList<ChurchDocumentUser> loadDocumentForPerson() {
        ObservableList<ChurchDocumentUser> list = FXCollections.observableArrayList();
        String sql = "SELECT data, payment, paymentData, transactionID, paymentStatus FROM payment_tax WHERE personID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChurchDocumentUser user = new ChurchDocumentUser(
                        rs.getString("data"),
                        rs.getString("payment"),
                        rs.getString("paymentData"),
                        rs.getInt("transactionID"),
                        rs.getString("paymentStatus"));
                list.add(user);

            }

            
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
            alert.showAndWait(); // Ensure the alert is shown
        }
        return list;
    }

    public void addElement() {
        String sqlGetSelectedPayment = "SELECT data, payment, paymentData, transactionID, paymentStatus FROM payment_tax WHERE transactionID = ?";
        ChurchDocumentUser getSelectedPayment = Payment_table.getSelectionModel().getSelectedItem();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtGetSelectedPayment = conn.prepareStatement(sqlGetSelectedPayment)) {

            stmtGetSelectedPayment.setInt(1, getSelectedPayment.getTransaction());

            ResultSet rs = stmtGetSelectedPayment.executeQuery();

            if (rs.next()) {
                ChurchDocumentUser user = new ChurchDocumentUser(
                        rs.getString("data"),
                        rs.getString("payment"),
                        rs.getString("paymentData"),
                        rs.getInt("transactionID"),
                        rs.getString("paymentStatus"));

                SelectedPersonDetailsEditedList.add(user);
                System.out.println("Smrrin qtu");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
