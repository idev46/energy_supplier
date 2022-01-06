package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.models.Consumer;
import sample.models.Reading;
import sample.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static sample.utils.Utils.READINGS_FILE_NAME;

public class ReadingController implements Initializable {
    public ComboBox<Consumer> accountsCombo;
    public ComboBox<String> monthsCombo;
    public ComboBox<String> yearsCombo;
    public TextField openReadingsTf;
    public TextField closeReadingsTf;
    public ComboBox<String> paymentBillCombo;
    public TextField costTF;
    public ComboBox<Consumer> accountsPCombo;
    public ComboBox<String> paymentCombo;
    public Button updateBtn;
    public TextArea outputTextarea;
    int consumerIndex;
    int readingIndex;
    private ArrayList<Consumer> consumers = new ArrayList<>();
    private ArrayList<Reading> readings = new ArrayList<>();
    private Reading selectedReading;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            consumers = Utils.getAllConsumers();
            updateReadingsList();
            if (consumers.isEmpty()) {
                setOutputText("Error: No consumer found. Add consumer first from consumer menu.");
            } else {
                accountsCombo.getItems().setAll(consumers);
                accountsPCombo.getItems().setAll(consumers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDateComboBoxes();
    }

    //Updating the readings list
    private void updateReadingsList() {

        readings.clear();
        try {
            readings = Utils.getAllReadings();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Settings the data to combo boxes
    private void setDateComboBoxes() {
        yearsCombo.getItems().setAll("2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030");
        yearsCombo.setValue("2022");

        monthsCombo.getItems().setAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        monthsCombo.setValue("01");

        paymentBillCombo.getItems().setAll("Unpaid", "Paid");
        paymentBillCombo.setValue("Unpaid");

        paymentCombo.getItems().setAll("Unpaid", "Paid");
        paymentCombo.setValue("");
    }

    private void setOutputText(String text) {
        if (!outputTextarea.getText().isEmpty()) {
            text = String.format("\n\n%s", text);
        }
        outputTextarea.appendText(text);

        outputTextarea.selectPositionCaret(outputTextarea.getLength());
        outputTextarea.deselect();
    }

    public void accountsPaymentComboBox(ActionEvent event) {
        int selectedIndex = accountsPCombo.getSelectionModel().getSelectedIndex();

        boolean isReadingFound = false;
        for (Reading reading : readings) {
            if (reading.getConsumer().getAccountNumber().equals(consumers.get(selectedIndex).getAccountNumber())) {
                paymentCombo.getItems().setAll("Unpaid", "Paid");
                paymentCombo.setValue(reading.getPaymentStatus());
                isReadingFound = true;
                selectedReading = reading;
            }
        }

        if (!isReadingFound) {
            setOutputText("No previous readings found for account# " + consumers.get(selectedIndex).getAccountNumber());
        }
    }

    public void accountsComboBox(ActionEvent event) {
        consumerIndex = accountsCombo.getSelectionModel().getSelectedIndex();
        Consumer consumer = consumers.get(consumerIndex);

        boolean isReadingFound = false;
        for (Reading reading : readings) {
            if (reading.getConsumer().getAccountNumber().equals(consumer.getAccountNumber())) {
                openReadingsTf.setText(reading.getClosingReadings());
                isReadingFound = true;
            }
        }

        if (!isReadingFound) {
            setOutputText("No previous readings found for account# " + consumer.getAccountNumber());

            openReadingsTf.setText("0");
        }

    }


    //Validiatiing the readings fileds
    public void handleAddReadingsButton(ActionEvent event) {

        if (!accountsCombo.getValue().toString().isEmpty()) {
            if (!closeReadingsTf.getText().isEmpty()) {
                if (!costTF.getText().isEmpty()) {
                    saveReadings(false);
                } else {
                    setOutputText("Error: Cost of electricity is required");
                }
            } else {
                setOutputText("Error: Close Readings is required");
            }
        } else {
            setOutputText("Error: Select Account");
        }

    }
    //Save readings to file
    private void saveReadings(boolean isGenerateInvoice) {
        try {
            FileWriter fw = new FileWriter(READINGS_FILE_NAME, true);
            PrintWriter pw = new PrintWriter(fw);

            Reading reading = new Reading();
            reading.setConsumer(Utils.getConsumerByAccountNumber(consumers.get(consumerIndex).getAccountNumber()));
            reading.setMonth(monthsCombo.getValue().toString());
            reading.setYear(yearsCombo.getValue().toString());
            reading.setClosingReadings(closeReadingsTf.getText());
            reading.setOpeningReadings(openReadingsTf.getText());
            reading.setCostPerUnit(costTF.getText());
            reading.setPaymentStatus(paymentBillCombo.getValue().toString());

            String line = reading.getConsumer().getAccountNumber() + Utils.INPUT_SPLITTER
                    + reading.getMonth() + Utils.INPUT_SPLITTER
                    + reading.getYear() + Utils.INPUT_SPLITTER
                    + reading.getOpeningReadings() + Utils.INPUT_SPLITTER
                    + reading.getClosingReadings() + Utils.INPUT_SPLITTER
                    + reading.getCostPerUnit() + Utils.INPUT_SPLITTER
                    + reading.getPaymentStatus();

            pw.println(line);

            pw.flush();
            pw.close();
            fw.close();

            //After Save
            setOutputText("Success: Reading is added against account #" + consumers.get(consumerIndex).getAccountNumber());

            openReadingsTf.clear();
            closeReadingsTf.clear();

            updateReadingsList();

            if (isGenerateInvoice) {
                String fileName = Utils.generateFileName(reading.getConsumer().getAccountNumber());
                try {
                    Utils.createInvoice(reading, fileName);
                    Utils.openFile(fileName);
                    setOutputText("Success: Invoice is generated. and saved " + fileName);
                } catch (Exception e) {
                    setOutputText("Error: " + e.getMessage());
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            setOutputText("Error " + e.getMessage());
            //new Alert(Alert.AlertType.ERROR, "Error " + e.getMessage()).showAndWait();
        }
    }

    //Generating and adding reading to file
    public void handleAddReadingsAndGenerateButton(ActionEvent event) {
        saveReadings(true);
    }

    //Updating the Payment Status
    public void handleUpdatePaymentButton(ActionEvent event) {
        try {
            Path path = Paths.get(READINGS_FILE_NAME);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            String line = selectedReading.getConsumer().getAccountNumber() + Utils.INPUT_SPLITTER
                    + selectedReading.getMonth().toString() + Utils.INPUT_SPLITTER
                    + selectedReading.getYear().toString() + Utils.INPUT_SPLITTER
                    + selectedReading.getOpeningReadings() + Utils.INPUT_SPLITTER
                    + selectedReading.getClosingReadings() + Utils.INPUT_SPLITTER
                    + selectedReading.getCostPerUnit() + Utils.INPUT_SPLITTER
                    + paymentCombo.getValue();

            lines.set(selectedReading.getRecordNo() - 1, line);
            Files.write(path, lines, StandardCharsets.UTF_8);

            //After Update
            setOutputText("Success: Payment status is updated of account #" + selectedReading.getConsumer().getAccountNumber());
            updateReadingsList();

        } catch (Exception e) {
            setOutputText("Error " + e.getMessage());
        }
    }
}
