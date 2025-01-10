import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class ChurchDocumentWorkerPrinter {

    @FXML
    private TextField idField;
    
    @FXML
    private TextField personField;

    @FXML
    private TextField phoneNumField;

    @FXML
    private TextField regionField;
    
    @FXML
    TableView<ChurchDocumentUser> Payment_table;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_year;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_payment;
    
    @FXML
    private TableColumn<ChurchDocumentUser, String> col_date;
    
    @FXML
    private TableColumn<ChurchDocumentUser, Integer> col_transaction;

    @FXML
    private TableColumn<ChurchDocumentUser, String> col_releasedFromPayment;

    @FXML
    private Button saveButton;

    @FXML
    private Button printButton;

    public ObservableList<ChurchDocumentUser> SelectedPersonDetails;
    private int personID;
    private String name;
    private String fatherName;
    private String surname;

    private String phoneNum;

    private String region;


    private PaymentWorkerController paymentWorkerController;

    @FXML
    private AnchorPane panel;

    public void setPersonDetails(int personID){
        this.personID = personID;
        loadDocumentForPerson();
        loadPersonDetails();
    }

    public void setPaymentWorkerController(PaymentWorkerController paymentWorkerController) {
        this.paymentWorkerController = paymentWorkerController;
    }
    
    public void initialize() {
        col_year.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("data"));
        col_payment.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("payment"));
        col_date.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, Integer>("transaction"));
        col_releasedFromPayment.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("releasedFromPayment"));


        col_payment.setCellFactory(column -> {
            return new TableCell<>() {
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

        SelectedPersonDetails = DatabaseConnection.getUsersPaymentTaxInfo();
        Payment_table.setItems(SelectedPersonDetails);



        
    }





    private void loadDocumentForPerson() {
        String sql = "SELECT payment_worker.data, payment_worker.payment, payment_worker.paymentData, payment_worker.transactionID, payment_worker.releasedFromPayment FROM payment_worker WHERE personID = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();
    
            SelectedPersonDetails.clear();
            while (rs.next()) {
                ChurchDocumentUser user = new ChurchDocumentUser(
                    rs.getString("data"),
                    rs.getString("payment"),
                    rs.getString("paymentData"),
                    rs.getInt("transactionID"),
                    rs.getString("releasedFromPayment")
                );
                SelectedPersonDetails.add(user);
            }
    
            Payment_table.setItems(SelectedPersonDetails);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
        }
    }



    private void loadPersonDetails() {
        String sql = "SELECT * FROM person_worker WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                personField.setText(rs.getString("name") + " " +  rs.getString("fatherName") + " " + rs.getString("surname"));
                phoneNumField.setText(rs.getString("phoneNum"));
                idField.setText(String.valueOf(personID));
                regionField.setText(rs.getString("region"));
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
        }
    }
 



    @FXML
    private void saveButtonImage(ActionEvent event) {
        // Hide the save button temporarily
        saveButton.setVisible(false);
        printButton.setVisible(false);
    
    
    
    
        double targetWidth = 2650;
        double targetHeight = 3050;
    
        // Calculate scaling factors based on the target dimensions
        double scaleX = targetWidth / panel.getWidth();
        double scaleY = targetHeight / panel.getHeight();
    
    
        // Define scale factor (e.g., 2.0 for double the resolution)
       
        
        // Apply scaling to the panel to increase resolution
        panel.setScaleX(scaleX);
        panel.setScaleY(scaleY);
    
        // Take a snapshot of the scaled panel
        WritableImage scaledImage = new WritableImage((int) targetWidth, (int) targetHeight);
        panel.snapshot(new SnapshotParameters(), scaledImage);
    
        // Reset the panel to its original scale
        panel.setScaleX(1.0);
        panel.setScaleY(1.0);
    
    
        // Make the save button visible again
        saveButton.setVisible(true);
        printButton.setVisible(true);
    
        // Open a FileChooser to let the user choose where to save the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
    
        // Show save dialog
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
    
        // If the user selected a file, save the image
        if (file != null) {
            try {
                // Get the selected file extension
                String fileExtension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).replace("*.", "");
    
                // Save the image as PNG or JPG
                ImageIO.write(SwingFXUtils.fromFXImage(scaledImage, null), fileExtension, file);
    
            } catch (IOException e) {
                // Show error alert if saving fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Saving Image");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while saving the image. Please try again.");
                alert.showAndWait();
            }
        }
    }


    @FXML
    private void printButtonFunc(ActionEvent event){



        saveButton.setVisible(false);
        printButton.setVisible(false);
    
    
    
    
        double targetWidth = 2650;
        double targetHeight = 3050;
    
        // Calculate scaling factors based on the target dimensions
        double scaleX = targetWidth / panel.getWidth();
        double scaleY = targetHeight / panel.getHeight();
    
    
        // Define scale factor (e.g., 2.0 for double the resolution)
       
        
        // Apply scaling to the panel to increase resolution
        panel.setScaleX(scaleX);
        panel.setScaleY(scaleY);
    
        // Take a snapshot of the scaled panel
        WritableImage scaledImage = new WritableImage((int) targetWidth, (int) targetHeight);
        panel.snapshot(new SnapshotParameters(), scaledImage);
    
        // Reset the panel to its original scale
        panel.setScaleX(1.0);
        panel.setScaleY(1.0);
    
    
        // Make the save button visible again
        saveButton.setVisible(true);
        printButton.setVisible(true);
        try{

        File tempImageFile = File.createTempFile("writable_image", ".png");
        tempImageFile.deleteOnExit();
        ImageIO.write(SwingFXUtils.fromFXImage(scaledImage, null), "png", tempImageFile);


        Desktop desktop = Desktop.getDesktop();
        desktop.open(tempImageFile);
        }
        catch(IOException e){
            e.printStackTrace();

        }



    }


}
