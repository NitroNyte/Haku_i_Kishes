package mainClasses;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChooseMassController implements Initializable {

    @FXML
    private TextField textAreaInput;

    @FXML
    private Text textOutput;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    @FXML
private void saveFunc() {
    String inputText = textAreaInput.getText().trim(); // Trim to remove whitespace
    
    if (inputText.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input i zbrazët");
        alert.setHeaderText(null);
        alert.setContentText("Ju lutem shkruani diçka në fushën e tekstit përpara se të ruani!");
        alert.showAndWait();
        return; // Exit the method if input is empty
    }

    try {
        // Define path to chooseMass folder and settings.txt
        String folderName = "massLocation";
        String fileName = "settings.txt";
        
        // Create the folder if it doesn't exist
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        // Create/access the settings file
        File file = new File(folder, fileName);
        
        // Write content to settings.txt
        try (FileWriter writer = new FileWriter(file, false)) { // Overwrite mode
            writer.write(inputText);
            textOutput.setText("Ju identifikoheni si: " + inputText);
        }

    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gabim në ruajtje");
        alert.setHeaderText(null);
        alert.setContentText("Ndodhi një gabim gjatë ruajtjes së të dhënave!");
        alert.showAndWait();
    }
}

private void init() {
    try {
        String folderName = "massLocation";
        String fileName = "settings.txt";
        File folder = new File(folderName);
        File file = new File(folder, fileName);

        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    // Only append newline if there are more lines
                    if (reader.ready()) {
                        content.append("\n");
                    }
                }
                
                String previousIdentification = content.toString().trim();
                if (!previousIdentification.isEmpty()) {
                    textOutput.setText("Ju jeni identifikuar më parë si: " + previousIdentification);
                    textAreaInput.setText(previousIdentification); // Pre-fill the input field
                    System.out.println("✅ U gjetën cilësimet e ruajtura: " + previousIdentification);
                }
            }
        } else {
            System.out.println("ℹ️ Nuk u gjetën të dhëna të identifikimit të mëparshëm");
        }

    } catch (IOException e) {
        System.err.println("❌ Gabim gjatë leximit të skedarit: " + e.getMessage());
        // Don't show error alert for missing file - it's normal on first run
        if (!(e instanceof FileNotFoundException)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në lexim");
            alert.setHeaderText(null);
            alert.setContentText("Ndodhi një gabim gjatë leximit të të dhënave të ruajtura!");
            alert.showAndWait();
        }
    }
}
    @FXML
    public void returnFunc(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
