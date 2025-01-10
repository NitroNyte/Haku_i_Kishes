import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;

public class DatabaseConnection {
    public static Connection getConnection() {
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Database/Pagesat.db?busy_timeout=10000");
            System.out.println("Connection Succesful");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
            return null;
        }
    }

    public static ObservableList<Person_tax> getDataUsersPersonTable(){
        
        ObservableList<Person_tax> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM person_tax");) {
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                list.add(new Person_tax(rs.getInt("id"), rs.getString("name"), rs.getString("fatherName"), rs.getString("surname"), rs.getString("region"), rs.getString("phoneNum")));
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }

        return list;


    }


    public synchronized static ObservableList<Person_worker> getDataUsersPersonTableWorker(){
        
        ObservableList<Person_worker> list = FXCollections.observableArrayList();
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM person_worker");) {
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                list.add(new Person_worker(rs.getInt("id"), rs.getString("name"), rs.getString("fatherName"), rs.getString("surname"), rs.getString("region"), rs.getString("phoneNum")));
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }

        return list;


    }


    
    public static void deletePersonFromDatabase(int personID) {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM person_tax WHERE id = ?");) {

        stmt.setInt(1, personID); // Corrected index
        
        stmt.executeUpdate(); // Use executeUpdate() for DELETE operations

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
    }
    
}


public static void deletePersonWorkerFromDatabase(int personID) {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM person_worker WHERE id = ?");) {

        stmt.setInt(1, personID); // Corrected index
        
        stmt.executeUpdate(); // Use executeUpdate() for DELETE operations

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
    }
    
}

public static void deletePaymentForPersonFromDatabase(int personID){
    try(Connection conn = getConnection();
    PreparedStatement stmt = conn.prepareStatement("DELETE FROM payment_tax WHERE personID = ?");) {
        stmt.setInt(1, personID);
        stmt.executeUpdate();
    } catch (Exception e) {
        // TODO: handle exception
    }
}


public static void deletePaymentForPersonWorkerFromDatabase(int personID){
    try(Connection conn = getConnection();
    PreparedStatement stmt = conn.prepareStatement("DELETE FROM payment_worker WHERE personID = ?");) {
        stmt.setInt(1, personID);
        stmt.executeUpdate();
    } catch (Exception e) {
        // TODO: handle exception
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
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
    }
}



public static void deletePaymentForWorkerFromDatabase(int transactionID) {
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
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
    }
}



public static ObservableList<Payment_tax> getPaymentsFor(int personId) {
    
    ObservableList<Payment_tax> list = FXCollections.observableArrayList();
    try (Connection conn = getConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_tax WHERE personId = ?")) {
        ps.setInt(1, personId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Payment_tax(rs.getInt("personId"), rs.getString("payment"), rs.getString("data"), rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("releasedFromPayment")));
        }
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
    }
    return list;
}


public static ObservableList<Payment_worker> getPaymentsForWorkerRefreshed(int personId) {
    
    ObservableList<Payment_worker> list = FXCollections.observableArrayList();
    try (Connection conn = getConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_worker WHERE personId = ?")) {
        ps.setInt(1, personId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Payment_worker(rs.getInt("personId"), rs.getString("payment"), rs.getString("data"), rs.getString("paymentData"), rs.getInt("transactionID"), rs.getString("releasedFromPayment")));
        }
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gabim në input");
                    alert.setHeaderText(null);
                    alert.setContentText("Gabim në program, nese shfaqët kjo disa herë kontaktoni krijuesin e programit!");
                    alert.showAndWait(); // Ensure the alert is shown
       

    }
    return list;
}


//The changes are made to connections if something were to ever happen, on date 05/09/2024

public static ObservableList<Payment_tax> getDataUsersPaymentTable(){
        
    ObservableList<Payment_tax> list = FXCollections.observableArrayList();
    try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_tax");) {
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            list.add(new Payment_tax(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"), rs.getString("paymentData"), rs.getInt("transaction"), rs.getString("releasedFromPayment")));
        }
        
    } catch (Exception e) {
        // TODO: handle exception
    }

    return list;

}



public static ObservableList<Payment_worker> getDataUsersPaymentWorkerTable(){
        
    ObservableList<Payment_worker> list = FXCollections.observableArrayList();
    try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_worker");) {
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            list.add(new Payment_worker(rs.getInt("personID"), rs.getString("payment"), rs.getString("data"), rs.getString("paymentData"), rs.getInt("transaction"), rs.getString("releasedFromPayment")));
        }
        
    } catch (Exception e) {
        // TODO: handle exception
    }

    return list;

}





public static ObservableList<ChurchDocumentUser> getUsersPaymentTaxInfo(){
        
    ObservableList<ChurchDocumentUser> list = FXCollections.observableArrayList();
    try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT payment_tax.data, payment_tax.payment, payment_tax.paymentData, payment_tax.transactionID, payment_tax.releasedFromPayment FROM payment_tax");) {
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            list.add(new ChurchDocumentUser(rs.getString("data").substring(6), rs.getString("payment"), rs.getString("data"), rs.getInt("transaction"), rs.getString("releasedFromPayment")));
        }
        
    } catch (Exception e) {
        // TODO: handle exception
    }

    return list;


}



public static ObservableList<ChurchDocumentUser> getUsersPaymentWorkerInfo(){
        
    ObservableList<ChurchDocumentUser> list = FXCollections.observableArrayList();
    try(Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT payment_worker.data, payment_worker.paymentData, payment_tax.payment, payment_tax.transactionID FROM payment_tax");) {
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            list.add(new ChurchDocumentUser(rs.getString("data"), rs.getString("payment"), rs.getString("data"), rs.getInt("transaction"), rs.getString("releasedFromPayment")));
        }
        
    } catch (Exception e) {
        // TODO: handle exception
    }

    return list;


}

}