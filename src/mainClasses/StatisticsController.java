package mainClasses;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StatisticsController implements Initializable {

    @FXML
    private PieChart pieChart1;

    @FXML
    private PieChart pieChart2;

    @FXML
    private ComboBox<String> listOfItems1;
    @FXML
    private ComboBox<String> listOfItems2;
    @FXML
    private Text totalSumChurchTax;
    @FXML
    private Text totalSumPersonWorker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfItems1.getItems().addAll(getAllYearsChurchTax());
        listOfItems1.getSelectionModel().selectLast();
        listOfItems2.getItems().addAll(getAllYearsPersonWorker());
        listOfItems2.getSelectionModel().selectLast();

        ObservableList<PieChart.Data> pieChartData1 = FXCollections
                .observableArrayList(dataOfPieChartChurchTax(listOfItems1.getValue()));

        pieChartData1.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " totalisht: ", data.pieValueProperty().intValue())));

        ObservableList<PieChart.Data> pieChartData2 = FXCollections
                .observableArrayList(dataOfPieChartPersonTax(listOfItems2.getValue()));

        pieChartData2.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " totalisht: ", data.pieValueProperty().intValue())));

        pieChart1.getData().addAll(pieChartData1);
        pieChart2.getData().addAll(pieChartData2);
        getTotalSumChurchTax(listOfItems1.getValue());
        getTotalSumPersonWorker(listOfItems2.getValue());

        listOfItems1.setOnAction(event -> {
            String selectedItem = listOfItems1.getValue();
            if (selectedItem != null) {
                getTotalSumChurchTax(selectedItem);
                updatePieChart1(selectedItem);

            }
        });

        listOfItems2.setOnAction(event -> {
            String selectedItem = listOfItems2.getValue();
            if (selectedItem != null) {
                getTotalSumPersonWorker(selectedItem);
                updatePieChart2(selectedItem);
            }
        });

    }

    private ObservableList<PieChart.Data> dataOfPieChartChurchTax(String chosenYear) {

        String chosenYearQuery = "SELECT COUNT(personID), paymentStatus FROM payment_tax WHERE strftime('%Y-%m-%d', paymentData) BETWEEN ? AND ? GROUP BY paymentStatus";

        ObservableList<PieChart.Data> dataOfChart = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement chosenYearStmt = conn.prepareStatement(chosenYearQuery)) {

            chosenYearStmt.setString(1, chosenYear + "-01-01");
            chosenYearStmt.setString(2, chosenYear + "-12-31");
            ResultSet rs = chosenYearStmt.executeQuery();

            while (rs.next()) {
                dataOfChart.add(new PieChart.Data(rs.getString(2), rs.getInt(1)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return dataOfChart;

    }

    private void updatePieChart1(String selectedYear) {
        pieChart1.getData().clear();
        ObservableList<PieChart.Data> newPieChartData = dataOfPieChartChurchTax(selectedYear);
        pieChart1.getData().addAll(newPieChartData);

        newPieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName(), " totalisht: ", data.pieValueProperty().intValue())));
    }

    private ObservableList<PieChart.Data> dataOfPieChartPersonTax(String chosenYear) {

        String chosenYearQuery = "SELECT COUNT(personID), paymentStatus FROM payment_worker WHERE strftime('%Y-%m-%d', paymentData) BETWEEN ? AND ? GROUP BY paymentStatus";

        ObservableList<PieChart.Data> dataOfChart = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement chosenYearStmt = conn.prepareStatement(chosenYearQuery)) {

            chosenYearStmt.setString(1, chosenYear + "-01-01");
            chosenYearStmt.setString(2, chosenYear + "-12-31");
            ResultSet rs = chosenYearStmt.executeQuery();

            while (rs.next()) {
                dataOfChart.add(new PieChart.Data(rs.getString(2), rs.getInt(1)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return dataOfChart;

    }

    private void updatePieChart2(String selectedYear) {
        pieChart2.getData().clear();
        ObservableList<PieChart.Data> newPieChartData = dataOfPieChartPersonTax(selectedYear);
        pieChart2.getData().addAll(newPieChartData);

        newPieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName(), " totalisht: ", data.pieValueProperty().intValue())));
    }

    private List<String> getAllYearsChurchTax() {

        String GetYearsQuery = "SELECT substr(paymentData, 1, 4) AS year FROM payment_tax GROUP BY year";

        List<String> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtGetYearQuery = conn.prepareStatement(GetYearsQuery)) {

            ResultSet rs = stmtGetYearQuery.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    private List<String> getAllYearsPersonWorker() {

        String GetYearsQuery = "SELECT substr(paymentData, 1, 4) AS year FROM payment_worker GROUP BY year";

        List<String> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtGetYearQuery = conn.prepareStatement(GetYearsQuery)) {

            ResultSet rs = stmtGetYearQuery.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    private void getTotalSumChurchTax(String chosenYear) {
        String sqlGetTotalAmoutPerYearChurchTax = "SELECT SUM(payment) FROM payment_tax WHERE paymentData BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtGetTotalSumChurchTax = conn.prepareStatement(sqlGetTotalAmoutPerYearChurchTax);) {

            stmtGetTotalSumChurchTax.setString(1, chosenYear + "-01-01");
            stmtGetTotalSumChurchTax.setString(2, chosenYear + "-12-31");
            ResultSet rs = stmtGetTotalSumChurchTax.executeQuery();

            if (rs.next()) {
                totalSumChurchTax.setText(rs.getString(1) + "€");
                System.out.println(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTotalSumPersonWorker(String chosenYear) {
        String sqlGetTotalAmoutPerYearPersonWorker = "SELECT SUM(payment) FROM payment_worker WHERE paymentData BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtGetTotalSumPersonWorker = conn
                        .prepareStatement(sqlGetTotalAmoutPerYearPersonWorker);) {

            stmtGetTotalSumPersonWorker.setString(1, chosenYear + "-01-01");
            stmtGetTotalSumPersonWorker.setString(2, chosenYear + "-12-31");
            ResultSet rs = stmtGetTotalSumPersonWorker.executeQuery();

            if (rs.next()) {
                totalSumPersonWorker.setText(rs.getString(1) + "€");
                System.out.println(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
