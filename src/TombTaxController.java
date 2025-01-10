import java.io.IOException;
import java.util.Optional;

import javax.swing.Action;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TombTaxController {

    @FXML
    TableView<Person_worker> Person_table;

    @FXML
    private TableColumn<Person_worker, Integer> col_id;

    @FXML
    private TableColumn<Person_worker, String> col_name;

    @FXML
    private TableColumn<Person_worker, String> col_fatherName;

    @FXML
    private TableColumn<Person_worker, String> col_surname;

    @FXML
    private TableColumn<Person_worker, String> col_region;

    @FXML
    private TableColumn<Person_worker, String> col_phoneNum;

    public ObservableList<Person_worker> originalData;



    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Person_worker, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Person_worker, String>("name"));
        col_fatherName.setCellValueFactory(new PropertyValueFactory<Person_worker, String>("fatherName"));
        col_surname.setCellValueFactory(new PropertyValueFactory<Person_worker, String>("surname"));
        col_region.setCellValueFactory(new PropertyValueFactory<Person_worker, String>("region"));
        col_phoneNum.setCellValueFactory(new PropertyValueFactory<Person_worker, String>("phoneNum"));

        // Load and store the original data
        originalData = DatabaseConnection.getDataUsersPersonTableWorker();
        Person_table.setItems(originalData);


        Person_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                Person_worker selectedPerson = Person_table.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    sendToPayment();
                }
            }
        });
    }




    @FXML
private void personFormTombSwitch(ActionEvent event){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FormTombTax.fxml"));
        Parent root = loader.load();


        //This is the logic so that hte tableviewcontroller is referencing this mf
        // Get the controller instance and set the TableViewController
        FormTombTaxController formTombTaxController = loader.getController();
        formTombTaxController.setTombTaxController(this);

        Stage newStage = new Stage();
        newStage.setTitle("Formulari plotesues");
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




    @FXML
    public void openSearchForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchFormTombTax.fxml"));
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

    public void applyFilter(ObservableList<Person_worker> filteredData) {
        Person_table.setItems(filteredData);
    }

    public void restoreOriginalData() {
        Person_table.setItems(originalData);
    }

    @FXML
    public void goBackHome(MouseEvent event) {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Faqja kryesore");
            stage.setScene(new Scene(homeRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void sendToPayment() {
        Person_worker selectedPerson = Person_table.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            try {
                // Load PaymentPanel.fxml and get its controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentWorkerPanel.fxml"));
                Parent root = loader.load();
                
                // Get the controller and pass the selected person ID
                PaymentWorkerController paymentWorkerController = loader.getController();
                paymentWorkerController.setPersonDetails(selectedPerson.getId(),selectedPerson.getName(),selectedPerson.getFatherName(), selectedPerson.getSurname(), selectedPerson.getRegion(), selectedPerson.getPhoneNum());
    
                // Switch to the PaymentPanel scene
                Stage stage = (Stage) Person_table.getScene().getWindow();
                stage.setTitle("Pagesat per puntort e varrezave");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
     public void refreshTableView(ObservableList<Person_worker> newPeopleList) {
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
    Person_worker selectedPerson = Person_table.getSelectionModel().getSelectedItem();
if(Person_table.getSelectionModel().getSelectedItem() != null){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Konfirmu fshirjen");
    alert.setHeaderText(null);
    alert.setContentText("A jeni të sigurt që deshironi të fshini këtë person?");
    Optional<ButtonType> result = alert.showAndWait();
    
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (selectedPerson != null) {
            int personId = selectedPerson.getId();
            // Call a method to delete from the database
            DatabaseConnection.deletePersonWorkerFromDatabase(personId);
            DatabaseConnection.deletePaymentForPersonWorkerFromDatabase(personId);
            
            // Refresh the TableView after deletion
            originalData = DatabaseConnection.getDataUsersPersonTableWorker();
            Person_table.setItems(originalData);
            Person_table.refresh();
            }
        }
    }
}

@FXML
public void editPerson(ActionEvent event){
    
            Person_worker selectedPerson = Person_table.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                try {
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPersonPanelTombTax.fxml"));
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
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
   
    alert.setHeaderText(null);
    alert.setContentText("Nuk keni selektuar asnjë person, ju lutem selektoni një!");
            }
        }
    



@FXML
public void StatisticsButton(ActionEvent event){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistics_worker.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Formulari per Statistikat");
        Scene scene = new Scene(root);
        newStage.setScene(scene); 
        newStage.show();

    } catch (Exception e) {
        e.printStackTrace();    
    }
}

    

}
