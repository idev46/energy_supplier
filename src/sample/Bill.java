package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.models.Consumer;
import sample.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.utils.Utils.READINGS_FILE_NAME;

public class Bill implements Initializable {
    public ComboBox<Consumer> accountsCombo;
    public ComboBox<String> monthsCombo;
    public ComboBox<String> yearsCombo;
    public TextField openReadingsTf;
    public TextField closeReadingsTf;
    public ComboBox<String> paymentBillCombo;
    public TextField costTF;
    public ComboBox accountsPCombo;
    public ComboBox<String> paymentCombo;
    public Button updateBtn;
    public TextArea outputTextarea;
    int selectedIndex;
    private ArrayList<Consumer> consumers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            consumers = Utils.getAllConsumers();
            if (consumers.isEmpty()) {
                setOutputText("Error: No consumer found. Add consumer first from consumer menu.");
            } else {

                accountsCombo.getItems().setAll(consumers);
                //setOutputText("Welcome");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDateComboBoxes();
    }

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
        if (outputTextarea.getText().isEmpty()) {
            outputTextarea.appendText(text);
        } else {
            text = String.format("\n\n%s", text);
            outputTextarea.appendText(outputTextarea.getText().concat(text));
        }

        outputTextarea.selectPositionCaret(outputTextarea.getLength());
        outputTextarea.deselect();
    }

    public void accountsComboBox(ActionEvent event) {
        selectedIndex = accountsCombo.getSelectionModel().getSelectedIndex();


    }

    public void handleAddReadingsButton(ActionEvent event) {

        if (!accountsCombo.getValue().toString().isEmpty()) {
            if (!closeReadingsTf.getText().isEmpty()) {
                if (!costTF.getText().isEmpty()) {
                    saveReadings();
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

    private void saveReadings() {
        try {
            FileWriter fw = new FileWriter(READINGS_FILE_NAME, true);
            PrintWriter pw = new PrintWriter(fw);


            String line = consumers.get(selectedIndex).getAccountNumber() + Utils.SPLITTER2
                    + monthsCombo.getValue().toString() + Utils.SPLITTER2
                    + yearsCombo.getValue().toString() + Utils.SPLITTER2
                    + "10" + Utils.SPLITTER2
                    //+ openReadingsTf.getText() + Utils.SPLITTER2
                    + closeReadingsTf.getText() + Utils.SPLITTER2
                    + costTF.getText() + Utils.SPLITTER2
                    + paymentBillCombo.getValue().toString();

            pw.println(line);

            pw.flush();
            pw.close();
            fw.close();

            //After Save
            setOutputText("Success: Reading is added against account #" + consumers.get(selectedIndex).getAccountNumber());

            openReadingsTf.clear();
            closeReadingsTf.clear();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            setOutputText("Error " + e.getMessage());
            //new Alert(Alert.AlertType.ERROR, "Error " + e.getMessage()).showAndWait();
        }
    }

    public void handleAddReadingsAndGenerateButton(ActionEvent event) {
        saveReadings();
    }
}
