package controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import mainClasses.DatabaseConnection;
import databaseClasses.ChurchDocumentUser;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;

public class ChurchDocumentWorkerPrinter {

    // Folder inside working directory
    String directoryPath = "data"; 
    String filePath = directoryPath + File.separator + "settings.txt";

    @FXML
    private TextField idField;

    @FXML
    private TextField personField;

    @FXML
    private TextField phoneNumField;

    @FXML
    private TextField regionField;

    @FXML
    private TextArea noticeArea;

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
    private TableColumn<ChurchDocumentUser, String> col_paymentStatus;

    @FXML
    private Button saveButton;

    @FXML
    private Button printButton;

    @FXML
    private Button saveNotice;

    @FXML
    private Text massText;

    @FXML
    private Text statusText;


    private ObservableList<ChurchDocumentUser> records = FXCollections.observableArrayList();

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

    @SuppressWarnings("unused")
    private PaymentWorkerController paymentWorkerController;

    @FXML
    private AnchorPane panel;

    public void setPersonDetails(int personID) {
        this.personID = personID;
        loadPersonDetails();
        loadDocumentForPerson();
        
    }

    public void setPaymentWorkerController(PaymentWorkerController paymentWorkerController) {
        this.paymentWorkerController = paymentWorkerController;
    }


    public void setSelectedRecords(ObservableList<ChurchDocumentUser> records) {
        if(!records.isEmpty()) {
            this.records = records;
            Payment_table.setItems(records);
        }
        else {
            loadDocumentForPerson();
        }
       
    }


    @SuppressWarnings("unused")
    public void initialize() throws URISyntaxException {
        col_year.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("data"));
        col_payment.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("payment"));
        col_date.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("paymentData"));
        col_transaction.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, Integer>("transaction"));
        col_paymentStatus.setCellValueFactory(new PropertyValueFactory<ChurchDocumentUser, String>("paymentStatus"));

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

        Payment_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && Payment_table.getSelectionModel().getSelectedItem() != null) { // Double-click
                                                                                                             // detected
                ChurchDocumentUser selectedPerson = Payment_table.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    selectedPersonNotice();
                }
            }
        });


        setMassLocation();

    }

    private void loadDocumentForPerson() {
        String sql = "SELECT data, payment, paymentData, transactionID, paymentStatus FROM payment_worker WHERE personID = ? ORDER BY paymentData DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            records.clear();
            while (rs.next()) {
                ChurchDocumentUser user = new ChurchDocumentUser(
                        rs.getString("data"),
                        rs.getString("payment"),
                        rs.getString("paymentData"),
                        rs.getInt("transactionID"),
                        rs.getString("paymentStatus"));
                records.add(user);
            }

            Payment_table.setItems(records);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
            alert.showAndWait(); // Ensure the alert is shown
        }
    }

    private void loadPersonDetails() {
        String sql = "SELECT * FROM person_table WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                personField.setText(
                        Character.toUpperCase(rs.getString("name").charAt(0))
                                + rs.getString("name").substring(1).toLowerCase() + " "
                                + Character.toUpperCase(rs.getString("fatherName").charAt(0))
                                + rs.getString("fatherName").substring(1).toLowerCase() + " "
                                + Character.toUpperCase(rs.getString("surname").charAt(0))
                                + rs.getString("surname").substring(1).toLowerCase());
                phoneNumField.setText(rs.getString("phoneNum"));
                idField.setText(String.valueOf(personID));
                regionField.setText(Character.toUpperCase(rs.getString("region").charAt(0))
                        + rs.getString("region").substring(1).toLowerCase());
                        statusText.setText(rs.getString("outRegion"));

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
    private void selectedPersonNotice() {

        ChurchDocumentUser selectedTransaction = Payment_table.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {

            String sql = "SELECT notice FROM payment_worker WHERE transactionID = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);) {

                int selected = selectedTransaction.getTransaction();
                stmt.setInt(1, selected);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    noticeArea.setText(rs.getString("notice"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void saveToNoticesForPerson(ActionEvent event) {
        ChurchDocumentUser selectedTransaction = Payment_table.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            String sql = "UPDATE payment_worker SET notice = ? WHERE transactionID = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);) {

                int PersonTransaction = selectedTransaction.getTransaction();
                String text = noticeArea.getText();
                stmt.setString(1, text);
                stmt.setInt(2, PersonTransaction);
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setMassLocation() {
        String folderName = "massLocation";
        String fileName = "settings.txt";
        File file = new File(folderName, fileName);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {  // Ensure line is not null before setting text
                massText.setText(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
    





    @FXML
    private void saveButtonImage(ActionEvent event) {
        // Hide the save button temporarily
        saveButton.setVisible(false);
        saveNotice.setVisible(false);
        printButton.setVisible(false);

        double targetWidth = 2250;
        double targetHeight = 3250;

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
        saveNotice.setVisible(true);

        // Open a FileChooser to let the user choose where to save the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));

        // Show save dialog
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        // If the user selected a file, save the image
        if (file != null) {
            try {
                // Get the selected file extension
                String fileExtension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).replace("*.",
                        "");

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
    private void printButtonFunc(ActionEvent event) {

        saveButton.setVisible(false);
        printButton.setVisible(false);
        saveNotice.setVisible(false);

        double targetWidth = 2250;
        double targetHeight = 3150;

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
        saveNotice.setVisible(true);
        try {

            File tempImageFile = File.createTempFile("writable_image", ".png");
            tempImageFile.deleteOnExit();
            ImageIO.write(SwingFXUtils.fromFXImage(scaledImage, null), "png", tempImageFile);

            Desktop desktop = Desktop.getDesktop();
            desktop.open(tempImageFile);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
