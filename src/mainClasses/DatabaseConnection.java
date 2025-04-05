package mainClasses;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import databaseClasses.Payment_tax;
import databaseClasses.Payment_worker;
import databaseClasses.Person_table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;

public class DatabaseConnection {


    //Goal it to make the db in the local data and access it, after which we read from that
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Database/Pagesat.db");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo nuk duhet të shfaqet");
            alert.showAndWait(); 
            return null;
        }
    }



    public static ObservableList<Person_table> getDataUsersPersonTable() {

        ObservableList<Person_table> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM person_table");) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Person_table(rs.getInt("id"), rs.getString("name"), rs.getString("fatherName"),
                        rs.getString("surname"), rs.getString("region"), rs.getString("outRegion"),
                        rs.getString("phoneNum")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }

        return list;

    }

    public synchronized static ObservableList<Payment_tax> getDescriptionsForChurchTaxUser(int personID) {

        ObservableList<Payment_tax> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT description, data FROM payment_tax WHERE personID = ?");) {

            ps.setInt(1, personID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Payment_tax(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"),
                        rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("paymentStatus"),
                        rs.getString("notice")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }

        return list;

    }

    public static void deletePersonFromDatabase(int personID) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM person_table WHERE id = ?");) {

            stmt.setInt(1, personID); // Corrected index

            stmt.executeUpdate(); // Use executeUpdate() for DELETE operations

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }

    }

    public static void deletePaymentForPersonFromDatabase(int personID) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM payment_tax WHERE personID = ?");
                PreparedStatement stmtForWorker = conn.prepareStatement("DELETE FROM payment_worker WHERE personID = ?");) {
            stmt.setInt(1, personID);
            stmtForWorker.setInt(1, personID);
            stmt.executeUpdate();
            stmtForWorker.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }
    }



    

    public static void deletePaymentFromDatabase(int transactionID) {
        String sql = "DELETE FROM payment_tax WHERE transactionID = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }
    }

    public static void deletePaymentFromDatabaseWorker(int transactionID) {
        String sql = "DELETE FROM payment_worker WHERE transactionID = ?";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }
    }

    public static ObservableList<Payment_tax> getPaymentsFor(int personId) {

        ObservableList<Payment_tax> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_tax WHERE personID = ?")) {
            ps.setInt(1, personId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Payment_tax(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"),
                        rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("paymentStatus")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }
        return list;
    }

    public static ObservableList<Payment_worker> getPaymentsForWorker(int personId) {

        ObservableList<Payment_worker> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_worker WHERE personID = ?")) {
            ps.setInt(1, personId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Payment_worker(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"),
                        rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("paymentStatus")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }
        return list;
    }

    // The changes are made to connections if something were to ever happen, on date
    // 05/09/2024

    public static ObservableList<Payment_tax> getDataUsersPaymentTable() {

        ObservableList<Payment_tax> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_tax ORDER BY paymentData DESC");) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Payment_tax(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"),
                        rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("paymentStatus")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }

        return list;

    }

    public static ObservableList<Payment_worker> getDataUsersPaymentTableWorker() {

        ObservableList<Payment_worker> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT * FROM payment_worker ORDER BY paymentData DESC");) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Payment_worker(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"),
                        rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("paymentStatus")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gabim në input");
            alert.setHeaderText(null);
            alert.setContentText("Gabim në program, kjo dritare nuk duhët të shfaqet!");
            alert.showAndWait(); 
        }

        return list;

    }

}