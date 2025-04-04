package controllers;


import java.io.IOException;
import java.util.Optional;

import mainClasses.DatabaseConnection;
import databaseClasses.Person_table;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TombTaxController {


    @FXML
    TableView<Person_table> Person_table;

    @FXML
    private TableColumn<Person_table, Integer> col_id;

    @FXML
    private TableColumn<Person_table, String> col_name;

    @FXML
    private TableColumn<Person_table, String> col_fatherName;

    @FXML
    private TableColumn<Person_table, String> col_surname;

    @FXML
    private TableColumn<Person_table, String> col_region;

    @FXML
    private TableColumn<Person_table, String> col_outRegion;

    @FXML
    private TableColumn<Person_table, String> col_phoneNum;

    @FXML
    private Rectangle rectShape;

    @FXML
    private Pane rectPane;

    public ObservableList<Person_table> originalData;

    @SuppressWarnings("unused")
    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Person_table, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Person_table, String>("name"));
        col_fatherName.setCellValueFactory(new PropertyValueFactory<Person_table, String>("fatherName"));
        col_surname.setCellValueFactory(new PropertyValueFactory<Person_table, String>("surname"));
        col_region.setCellValueFactory(new PropertyValueFactory<Person_table, String>("region"));
        col_outRegion.setCellValueFactory(new PropertyValueFactory<Person_table, String>("outRegion"));
        col_phoneNum.setCellValueFactory(new PropertyValueFactory<Person_table, String>("phoneNum"));

        // Load and store the original data
        originalData = DatabaseConnection.getDataUsersPersonTable();
        Person_table.setItems(originalData);
 
        //Open TableViewController class for details on why the creator did this
        col_id.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.04));       // 8%
        col_name.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.15));     // 15%
        col_fatherName.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.15)); // 15%
        col_surname.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.14));  // 14%
        col_region.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.18));   // 18%
        col_outRegion.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.1)); // 12%
        col_phoneNum.prefWidthProperty().bind(Person_table.widthProperty().multiply(0.20)); 


        rectShape.heightProperty().bind(rectPane.heightProperty().multiply(2));

        
                col_name.setCellFactory(column -> {
            return new TableCell<Person_table, String>() {
           @Override
           protected void updateItem(String name, boolean empty) {
            super.updateItem(name, empty);
            if(empty || name == null) {
                setText(null);
            }
            else {
                setText(Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase());
            }
           }
        };
    });




    col_fatherName.setCellFactory(column -> {
        return new TableCell<Person_table, String>() {
       @Override
       protected void updateItem(String fatherName, boolean empty) {
        super.updateItem(fatherName, empty);
        if(empty || fatherName == null) {
            setText(null);
        }
        else {
            setText(Character.toUpperCase(fatherName.charAt(0)) + fatherName.substring(1).toLowerCase());
        }
       }
    };
});


col_surname.setCellFactory(column -> {
    return new TableCell<Person_table, String>() {
   @Override
   protected void updateItem(String surname, boolean empty) {
    super.updateItem(surname, empty);
    if(empty || surname == null) {
        setText(null);
    }
    else {
        setText(Character.toUpperCase(surname.charAt(0)) + surname.substring(1).toLowerCase());
    }
   }
};
});


col_region.setCellFactory(column -> {
    return new TableCell<Person_table, String>() {
   @Override
   protected void updateItem(String region, boolean empty) {
    super.updateItem(region, empty);
    if(empty || region == null) {
        setText(null);
    }
    else {
        setText(Character.toUpperCase(region.charAt(0)) + region.substring(1).toLowerCase());
    }
   }
};
});



col_phoneNum.setCellFactory(column -> {
    return new TableCell<Person_table, String>() {
   @Override
   protected void updateItem(String phoneNum, boolean empty) {
    super.updateItem(phoneNum, empty);
    if(empty || phoneNum == null) {
        setText(null);
    }
    else {
        setText(phoneNum);
    }
   }
};
});





        Person_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                Person_table selectedPerson = Person_table.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    sendToPayment();
                }
            }
        });
    }

    @FXML
    private void personFormTombSwitch(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormTombTax.fxml"));
            Parent root = loader.load();

            // This is the logic so that hte tableviewcontroller is referencing this mf
            // Get the controller instance and set the TableViewController
            FormTombTaxController formTombTaxController = loader.getController();
            formTombTaxController.setTombTaxController(this);

            Stage newStage = new Stage();
            newStage.setTitle("Formulari plotesues");

            double currentWidth = newStage.getWidth();
            double currentHeight = newStage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openSearchForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchFormTombTax.fxml"));
            Parent root = loader.load();

            SearchFormTombTaxController searchFormTombTaxController = loader.getController();
            searchFormTombTaxController.setTombTaxController(this);

            Stage newStage = new Stage();
            newStage.setTitle("Formulari i kerkimit");
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(ObservableList<Person_table> filteredData) {
        Person_table.setItems(filteredData);
    }

    public void restoreOriginalData() {
        Person_table.setItems(originalData);
    }

    @FXML
    public void goBackHome(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToPayment() {
        Person_table selectedPerson = Person_table.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            try {
                // Load PaymentPanel.fxml and get its controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentWorkerPanel.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected person ID
                PaymentWorkerController paymentWorkerController = loader.getController();
                paymentWorkerController.setPersonDetails(selectedPerson.getId(), selectedPerson.getName(),
                        selectedPerson.getFatherName(), selectedPerson.getSurname(), selectedPerson.getRegion(),
                        selectedPerson.getPhoneNum());

                // Switch to the PaymentPanel scene
                Stage stage = (Stage) Person_table.getScene().getWindow();
                stage.setTitle("Pagesat per puntort e varrezave");
                stage.getScene().setRoot(root);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void refreshTableView(ObservableList<Person_table> newPeopleList) {
        if (originalData != null) {
            originalData.clear(); // Clear the existing data
            originalData.addAll(newPeopleList); // Add the new data
            Person_table.setItems(originalData); // Update the TableView's items
        } else {
            originalData = FXCollections.observableArrayList(newPeopleList); // Initialize originalData if it's null
            Person_table.setItems(originalData); // Set the items for the TableView
        }
        Person_table.refresh(); // Refresh the TableView to reflect changes
    }

    @FXML
    public void deletePerson(ActionEvent event) {
        Person_table selectedPerson = Person_table.getSelectionModel().getSelectedItem();
        if (Person_table.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmu fshirjen");
            alert.setHeaderText(null);
            alert.setContentText("A jeni të sigurt që deshironi të fshini këtë person?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (selectedPerson != null) {
                    int personId = selectedPerson.getId();
                    // Call a method to delete from the database
                    DatabaseConnection.deletePersonFromDatabase(personId);
                    DatabaseConnection.deletePaymentForPersonFromDatabase(personId);

                    // Refresh the TableView after deletion
                    originalData = DatabaseConnection.getDataUsersPersonTable();
                    Person_table.setItems(originalData);
                    Person_table.refresh();
                }
            }
        }
    }

    @FXML
    public void editPerson(ActionEvent event) {

        Person_table selectedPerson = Person_table.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditPersonPanelTombTax.fxml"));
                Parent root = loader.load();

                EditPersonTombTaxController editPersonController = loader.getController();
                editPersonController.setTombTaxController(this);
                editPersonController.setPersonDetails(selectedPerson.getId());

                Stage newStage = new Stage();
                newStage.setTitle("Edito të dhënat e një personi");
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText(null);
            alert.setContentText("Nuk keni selektuar asnjë person, ju lutem selektoni një!");
        }
    }

}
