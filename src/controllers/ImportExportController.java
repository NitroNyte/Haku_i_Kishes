package controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import mainClasses.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImportExportController {

    @FXML
    private Button importData;
    @FXML
    private Button exportData;
    @FXML
    private Button deleteAndImportData;
    @FXML
    private Button deleteDataChurchTax;
    @FXML
    private Button deleteDataPersonTax;

    private ExtensionFilter csvFilter = new ExtensionFilter("csv", "*.csv");

    @FXML
    public void setPersonTableDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(csvFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        System.out.println(file);
        String deletePersonQuery = "DELETE FROM person_table";
        String insertIntoPersonWorkerQuery = "INSERT INTO person_table(id, name, fatherName, surname, region, outRegion, phoneNum) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                PreparedStatement stmtForDeletingPerson = conn.prepareStatement(deletePersonQuery);
                PreparedStatement stmtForInsertingPersonTax = conn.prepareStatement(insertIntoPersonWorkerQuery);) {
            reader.readLine();

            conn.setAutoCommit(false);
            stmtForDeletingPerson.executeUpdate();

            int personTaxID;
            String personTaxName, personTaxfatherName, personTaxSurname, personTaxregion, personTaxoutRegion,
                    personTaxPhoneNum;



            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                if (row.length < 7) {
                    personTaxID = Integer.valueOf(row[0].trim());
                    personTaxName = row[1].trim();
                    personTaxfatherName = row[2].trim();
                    personTaxSurname = row[3].trim();
                    personTaxregion = row[4].trim();
                    personTaxoutRegion = row[5].trim();
                    personTaxPhoneNum = null;
                } else {
                    personTaxID = Integer.valueOf(row[0].trim());
                    personTaxName = row[1].trim();
                    personTaxfatherName = row[2].trim();
                    personTaxSurname = row[3].trim();
                    personTaxregion = row[4].trim();
                    personTaxoutRegion = row[5].trim();
                    personTaxPhoneNum = row[6].trim();
                }

                stmtForInsertingPersonTax.setInt(1, personTaxID);
                stmtForInsertingPersonTax.setString(2, personTaxName);
                stmtForInsertingPersonTax.setString(3, personTaxfatherName);
                stmtForInsertingPersonTax.setString(4, personTaxSurname);
                stmtForInsertingPersonTax.setString(5, personTaxregion);
                stmtForInsertingPersonTax.setString(6, personTaxoutRegion);
                stmtForInsertingPersonTax.setString(7, personTaxPhoneNum);
                stmtForInsertingPersonTax.addBatch();

            }
            stmtForInsertingPersonTax.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void setPaymentTaxDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(csvFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        System.out.println(file);

        String deletePaymentQuery = "DELETE FROM payment_tax";
        String insertIntoPaymentTaxQuery = "INSERT INTO payment_tax(personID, payment, data, paymentData, transactionID, paymentStatus, notice) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                PreparedStatement stmtForDeletingPayment = conn.prepareStatement(deletePaymentQuery);
                PreparedStatement stmtForInsertingIntoPaymentTax = conn.prepareStatement(insertIntoPaymentTaxQuery);) {
            // Skips the header line
            reader.readLine();

            stmtForDeletingPayment.executeUpdate();

            conn.setAutoCommit(false);

            int paymentTaxID, PaymentTaxTransactionID;
            String paymentTax, paymentTaxData, paymentTaxPaymentData, paymentTaxPaymentStatus, PaymentTaxNotice;

            System.out.println("Mrrin te pjesa1");

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                paymentTaxID = Integer.valueOf(row[0].trim());
                paymentTax = row[1].trim();
                paymentTaxData = row[2].trim();
                paymentTaxPaymentData = row[3].trim();
                PaymentTaxTransactionID = Integer.valueOf(row[4].trim());
                paymentTaxPaymentStatus = row[5].trim();
                PaymentTaxNotice = (!row[6].isEmpty()) ? row[6].trim() : null;
                System.out.println("Mrrini te pjesa 2");

                stmtForInsertingIntoPaymentTax.setInt(1, paymentTaxID);
                stmtForInsertingIntoPaymentTax.setString(2, paymentTax);
                stmtForInsertingIntoPaymentTax.setString(3, paymentTaxData);
                stmtForInsertingIntoPaymentTax.setString(4, paymentTaxPaymentData);
                stmtForInsertingIntoPaymentTax.setInt(5, PaymentTaxTransactionID);
                stmtForInsertingIntoPaymentTax.setString(6, paymentTaxPaymentStatus);
                stmtForInsertingIntoPaymentTax.setString(7, PaymentTaxNotice);
                stmtForInsertingIntoPaymentTax.addBatch();

                for (String index : row) {
                    System.out.printf("%-10s", index);
                }

            }
            stmtForInsertingIntoPaymentTax.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void setPaymentWorkerDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(csvFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        System.out.println(file);

        String deletePaymentQuery = "DELETE FROM payment_worker";
        String insertIntoPaymentTaxQuery = "INSERT INTO payment_worker(personID, payment, data, paymentData, transactionID, paymentStatus, notice) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                PreparedStatement stmtForDeletingPayment = conn.prepareStatement(deletePaymentQuery);
                PreparedStatement stmtForInsertingIntoPaymentTax = conn.prepareStatement(insertIntoPaymentTaxQuery);) {
            reader.readLine();

            stmtForDeletingPayment.executeUpdate();

            conn.setAutoCommit(false);

            int paymentWorkerID, PaymentWorkerTransactionID;
            String paymentWorker, paymentWorkerData, paymentWorkerPaymentData, paymentWorkerPaymentStatus,
                    PaymentWorkerNotice;

            System.out.println("Mrrin te pjesa1");

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                paymentWorkerID = Integer.parseInt(row[0].trim());
                paymentWorker = row[1].trim();
                paymentWorkerData = row[2].trim();
                paymentWorkerPaymentData = row[3].trim();
                PaymentWorkerTransactionID = Integer.parseInt(row[4].trim());
                paymentWorkerPaymentStatus = row[5].trim();
                PaymentWorkerNotice = (!row[6].isEmpty()) ? row[6].trim() : null;
                System.out.println("Mrrini te pjesa 2");

                stmtForInsertingIntoPaymentTax.setInt(1, paymentWorkerID);
                stmtForInsertingIntoPaymentTax.setString(2, paymentWorker);
                stmtForInsertingIntoPaymentTax.setString(3, paymentWorkerData);
                stmtForInsertingIntoPaymentTax.setString(4, paymentWorkerPaymentData);
                stmtForInsertingIntoPaymentTax.setInt(5, PaymentWorkerTransactionID);
                stmtForInsertingIntoPaymentTax.setString(6, paymentWorkerPaymentStatus);
                stmtForInsertingIntoPaymentTax.setString(7, PaymentWorkerNotice);
                stmtForInsertingIntoPaymentTax.addBatch();

            }
            stmtForInsertingIntoPaymentTax.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void exportPersonAllDB(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(csvFilter);
        File filePath = fileChooser.showSaveDialog(null);
        File personTable = new File(filePath + "Haku_I_Kishes.csv");
        File paymentTax = new File(filePath + "Pagesat_Haku_I_Kishes.csv");
        File paymentWorker = new File(filePath + "Mirembajtja_e_varrezave_Haku_I_Kishes.csv");

        String getPersonTableQuery = "SELECT * FROM person_table";
        String getPaymentTaxQuery = "SELECT * FROM payment_tax";
        String getPaymentWorkerQuery = "SELECT * FROM payment_worker";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmtForPersonTable = conn.prepareStatement(getPersonTableQuery);
                PreparedStatement stmtForPaymentTax = conn.prepareStatement(getPaymentTaxQuery);
                PreparedStatement stmtForPaymentWorker = conn.prepareStatement(getPaymentWorkerQuery);) {

            PrintWriter writeContent = new PrintWriter(personTable);

            ResultSet rsForPerson = stmtForPersonTable.executeQuery();

            writeContent.printf("%s,%s,%s,%s,%s,%s,%s %n", "ID", "emri", "emri i babes", "mbiemri", "vendi",
                    "jasht vendi", "numri i telefonit");

            while (rsForPerson.next()) {
                System.out.println(rsForPerson.getInt("id"));
                writeContent.printf("%d,%s,%s,%s,%s,%s,%s %n", rsForPerson.getInt("id"),
                        rsForPerson.getString("name"),
                        rsForPerson.getString("fatherName"), rsForPerson.getString("surname"),
                        rsForPerson.getString("region"),
                        rsForPerson.getString("outRegion"), rsForPerson.getString("phoneNum"));
            }

            writeContent.close();

            PrintWriter writeContentPayment = new PrintWriter(paymentTax);

            ResultSet rsForPayment = stmtForPaymentTax.executeQuery();
            writeContentPayment.printf("%s,%s,%s,%s,%s,%s,%s %n", "ID", "pagesa", "data", "data e pageses",
                    "transactionID", "statusi i pageses", "verejtjet");

            while (rsForPayment.next()) {
                writeContentPayment.printf("%d, %s, %s, %s, %s, %s, %s %n", rsForPayment.getInt("personID"),
                        rsForPayment.getString("payment"),
                        rsForPayment.getString("data"), rsForPayment.getString("paymentData"),
                        rsForPayment.getInt("transactionID"),
                        rsForPayment.getString("paymentStatus"), rsForPayment.getString("notice"));
            }

            writeContentPayment.close();

            PrintWriter writeContentPaymentWorker = new PrintWriter(paymentWorker);

            ResultSet rsForPaymentWorker = stmtForPaymentWorker.executeQuery();

            writeContentPaymentWorker.printf("%s,%s,%s,%s,%s,%s,%s %n", "ID", "pagesa", "data", "data e pageses",
                    "transactionID", "Statusi i pageses", "verejtjet");

            while (rsForPaymentWorker.next()) {
                writeContentPaymentWorker.printf("%d,%s,%s,%s,%s,%s,%s %n", rsForPaymentWorker.getInt("personID"),
                        rsForPaymentWorker.getString("payment"),
                        rsForPaymentWorker.getString("data"), rsForPaymentWorker.getString("paymentData"),
                        rsForPaymentWorker.getInt("transactionID"),
                        rsForPaymentWorker.getString("paymentStatus"), rsForPaymentWorker.getString("notice"));
            }

            writeContentPaymentWorker.close();
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
