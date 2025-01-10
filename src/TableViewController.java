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


public class TableViewController {

    @FXML
     TableView<Person_tax> Person_table;

    @FXML
    private TableColumn<Person_tax, Integer> col_id;

    @FXML
    private TableColumn<Person_tax, String> col_name;

    @FXML
    private TableColumn<Person_tax, String> col_fatherName;
    
    @FXML
    private TableColumn<Person_tax, String> col_surname;

    @FXML
    private TableColumn<Person_tax, String> col_region;

    @FXML
    private TableColumn<Person_tax, String> col_phoneNum;



     



    public ObservableList<Person_tax> originalDataList;

    



 
    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<Person_tax, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Person_tax, String>("name"));
        col_fatherName.setCellValueFactory(new PropertyValueFactory<Person_tax, String>("fatherName"));
        col_surname.setCellValueFactory(new PropertyValueFactory<Person_tax, String>("surname"));
        col_region.setCellValueFactory(new PropertyValueFactory<Person_tax, String>("region"));
        col_phoneNum.setCellValueFactory(new PropertyValueFactory<Person_tax, String>("phoneNum"));

        


    
        
        originalDataList = DatabaseConnection.getDataUsersPersonTable();
        Person_table.setItems(originalDataList);



        Person_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && Person_table.getSelectionModel().getSelectedItem() != null) { // Double-click detected
                Person_tax selectedPerson = Person_table.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    sendToPayment();
                }
            }
        });
    }

    @FXML
private void personFormSwitch(ActionEvent event){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Form.fxml"));
        Parent root = loader.load();


        //This is the logic so that hte tableviewcontroller is referencing this mf
        // Get the controller instance and set the TableViewController
        FormViewController formViewController = loader.getController();
        formViewController.setTableViewController(this);

        Stage newStage = new Stage();
        newStage.setTitle("Formulari plotësues");
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




@FXML
public void editPerson(ActionEvent event){
    
            Person_tax selectedPerson = Person_table.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                try {
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPersonPanelChurchTax.fxml"));
                    Parent root = loader.load();


                    EditPersonChurchTaxController editPersonController = loader.getController();
                    editPersonController.setTableViewController(this);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistics_Tax.fxml"));
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



    @FXML
     public void refreshTableView(ObservableList<Person_tax> newPeopleList) {
        if (originalDataList != null) {
            originalDataList.clear(); // Clear the existing data
            originalDataList.addAll(newPeopleList); // Add the new data
            Person_table.setItems(originalDataList); // Update the TableView's items
        } else {
            originalDataList = FXCollections.observableArrayList(newPeopleList); // Initialize originalDataList if it's null
            Person_table.setItems(originalDataList); // Set the items for the TableView
        }
        Person_table.refresh(); // Refresh the TableView to reflect changes
    }



    @FXML
    public void goBackHome(MouseEvent event){
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Haku i Kishes");
            stage.setScene(new Scene(homeRoot));
            stage.show();
        
        } catch (IOException e) {
          
            e.printStackTrace();
        }
    }


    @FXML
public void deletePerson(ActionEvent event) {
    Person_tax selectedPerson = Person_table.getSelectionModel().getSelectedItem();
if(Person_table.getSelectionModel().getSelectedItem() != null){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Deletion");
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
            originalDataList = DatabaseConnection.getDataUsersPersonTable();
            Person_table.setItems(originalDataList);
            Person_table.refresh();
            }
        }
    }
}


@FXML
    public void openSearchForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchFormTransaction.fxml"));
            Parent root = loader.load();

            SearchFormTransactionController searchFormTransactionController = loader.getController();
            searchFormTransactionController.setTruceViewController(this);

            Stage newStage = new Stage();
            newStage.setTitle("Formulari i kërkimit");
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void applyFilter(ObservableList<Person_tax> filteredData) {
        Person_table.setItems(filteredData);
    }

    public void restoreOriginalData() {
        Person_table.setItems(originalDataList);
    }



   

    public void sendToPayment() {
        Person_tax selectedPerson = Person_table.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            try {
                // Load PaymentPanel.fxml and get its controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentPanel.fxml"));
                Parent root = loader.load();
                
                // Get the controller and pass the selected person ID
                PaymentController paymentController = loader.getController();
                paymentController.setPersonDetails(selectedPerson.getId(),selectedPerson.getName(),selectedPerson.getFatherName(),selectedPerson.getSurname(), selectedPerson.getRegion(),selectedPerson.getPhoneNum());
                
                
                // Switch to the PaymentPanel scene
                Stage stage = (Stage) Person_table.getScene().getWindow();
                stage.setTitle("Dritarja e pagesave");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    



    






}
