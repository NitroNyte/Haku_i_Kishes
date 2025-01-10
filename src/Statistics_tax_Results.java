import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Statistics_tax_Results {
    
    @FXML
    private Text textToEdit1;

    @FXML
    private Text textToEdit2;


    public void setResults(String Rs1, String Rs2){
        textToEdit1.setText("Shuma e të gjitha pagesave të mbledhura gjatë vitit " + Rs1 +": " + getYearSum(Rs1) + " euro");
        textToEdit2.setText("Numri i popullatës që kanë paguar gjatë " + Rs1 + " kundrejt numrit total: "+ getPeopleThatPaid(Rs1) + " nga " + Rs2 );

    }



    public String getYearSum(String selectedYearNum){
        String totalNum = "";
        String sqlYearSum = "SELECT SUM(CAST(payment_tax.payment AS INTEGER)) FROM payment_tax WHERE SUBSTR(paymentData, 7, 4) = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmtYearSum = conn.prepareStatement(sqlYearSum)) {
            stmtYearSum.setString(1, selectedYearNum);
            ResultSet rsYearSum = stmtYearSum.executeQuery();
            if(rsYearSum.next()){
                totalNum = String.valueOf(rsYearSum.getInt(1));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return totalNum;
    }


    public String getPeopleThatPaid(String peopleThatPaid){
        String totalNum = "";
        String sqlPeopleThatPaidSum = "SELECT COUNT(*) FROM payment_tax WHERE SUBSTR(paymentData, 7, 4) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmtPeopleThatPaidSum = conn.prepareStatement(sqlPeopleThatPaidSum)) {
            
            stmtPeopleThatPaidSum.setString(1, peopleThatPaid);
            ResultSet rsPeopleThatPaid = stmtPeopleThatPaidSum.executeQuery();
            if(rsPeopleThatPaid.next()){
                totalNum = String.valueOf(rsPeopleThatPaid.getInt(1));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalNum;
    }



    @FXML
    public void cancelStatistics(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
