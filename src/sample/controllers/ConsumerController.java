package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.models.Consumer;
import sample.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static sample.utils.Utils.CONSUMERS_FILE_NAME;

public class ConsumerController implements Initializable {

    public Button submitBtn;
    public RadioButton addRb;
    public RadioButton updateRb;
    public RadioButton searchRb;
    public ToggleGroup selection;
    public TextField accountTF;
    public TextField nameTF;
    public TextField phoneTf;
    public ComboBox<String> meterTypeCombo;
    public TextArea outputTextArea;
    public TextArea addressTF;
    public ComboBox<String> tariffTypeCombo;
    public Label addressLabel;
    public Label meterTypeLabel;
    public Label tariffTypeLabel;
    public Label accountLabel;
    public Label hintLabel;
    private int recordNo;


    public void handleSubmitButton(ActionEvent event) {
        if (addRb.isSelected()) {
            if (nameTF.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Name is  Required.").showAndWait();
            } else {
                if (phoneTf.getText().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Phone Number is  Required.").showAndWait();
                } else {
                    if (addressTF.getText().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "Current Address is  Required.").showAndWait();
                    } else {
                        SaveConsumer();
                    }
                }
            }
        } else if (updateRb.isSelected()) {
            if (!phoneTf.getText().isEmpty() || !accountTF.getText().isEmpty() || !nameTF.getText().isEmpty()) {
                if (submitBtn.getText().equals("Update Consumer")) {
                    updateRecord();
                } else {
                    searchConsumer(true);
                }
            } else {
                setOutputText("Error: One of these Field is required to search.");
            }
        } else if (searchRb.isSelected()) {
            if (!phoneTf.getText().isEmpty() || !accountTF.getText().isEmpty() || !nameTF.getText().isEmpty()) {
                searchConsumer(false);
            } else {
                setOutputText("Error: One of these Field is required to search.");
            }
        }
    }

    private void updateRecord() {
        try {
            Path path = Paths.get(CONSUMERS_FILE_NAME);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            String line = accountTF.getText() + "|"
                    + nameTF.getText() + "|"
                    + phoneTf.getText() + "|"
                    + addressTF.getText() + "|"
                    + meterTypeCombo.getValue() + "|"
                    + tariffTypeCombo.getValue() + "|"
                    + java.time.LocalDateTime.now();


            lines.set(recordNo - 1, line);
            Files.write(path, lines, StandardCharsets.UTF_8);

            //After Update
            submitBtn.setText("Search Consumer");
            setOutputText("Success: Consumer record is updated against account #" + accountTF.getText());
            accountTF.clear();
            disableAccountComponent(false);
            disableComponents(true);

            //tariffTypeCombo.getItems().removeAll(true)

            setDefaultComoBoxValues();

            nameTF.clear();
            phoneTf.clear();
            addressTF.clear();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            setOutputText("Error " + e.getMessage());
            //new Alert(Alert.AlertType.ERROR, "Error " + e.getMessage()).showAndWait();
        }
    }

    private void searchConsumer(boolean isUpdate) {
        try {
            String[] tokens;

            FileReader fr = new FileReader(CONSUMERS_FILE_NAME);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            if (line.isEmpty()) {
                setOutputText("Error: File is Empty");
                br.close();
                fr.close();
                return;
            }
            recordNo = 0;
            boolean isFound = false;

            while (line != null) {
                tokens = line.split(Utils.OUTPUT_SPLITTER);

                Consumer consumer = Utils.getConsumerFromFile(tokens);
                recordNo++;

                System.out.println(consumer);

                if (consumer.getAccountNumber().toLowerCase().equals(accountTF.getText().toLowerCase())
                        || consumer.getName().toLowerCase().equals(nameTF.getText().toLowerCase())
                        || consumer.getPhoneNo().toLowerCase().equals(phoneTf.getText().toLowerCase())) {

                    ///////////FOR UPDATE
                    if (isUpdate) {
                        disableAccountComponent(true);
                        disableComponents(false);
                        accountTF.setText(consumer.getAccountNumber());
                        nameTF.setText(consumer.getName());
                        phoneTf.setText(consumer.getPhoneNo());
                        addressTF.setText(consumer.getCurrentAddress());

                        meterTypeCombo.setValue(consumer.getMeterType());
                        tariffTypeCombo.setValue(consumer.getEnergyTariff());

                        setOutputText("Success: Consumer Found.");
                        submitBtn.setText("Update Consumer");

                        isFound = true;
                        break;
                    }
                    //////////////////////


                    StringBuilder builder = new StringBuilder();

                    if (!isFound) {
                        builder.append("Consumer Search Results");
                    }
                    isFound = true;

                    builder.append("\n").append("Account Number: ").append(consumer.getAccountNumber());
                    builder.append("\n").append("Consumer Name: ").append(consumer.getName());
                    builder.append("\n").append("Consumer Phone No: ").append(consumer.getPhoneNo());
                    builder.append("\n").append("Current Address: ").append(consumer.getCurrentAddress());
                    builder.append("\n").append("Meter Type: ").append(consumer.getMeterType());
                    builder.append("\n").append("Tariff Type: ").append(consumer.getEnergyTariff());
                    builder.append("\n").append("Consumer Registration Date: ").append(consumer.getRegistrationDate());

                    builder.append("\n");

                    setOutputText(builder.toString());
                }

                line = br.readLine();

            }
            br.close();
            fr.close();

            if (!isFound) {
                setOutputText("Success: No Consumer Found against this inputs");
            }

        } catch (Exception ex) {
            setOutputText("Error: " + ex.getMessage());
        }
    }


    private void SaveConsumer() {
        try {
            FileWriter fw = new FileWriter(CONSUMERS_FILE_NAME, true);
            PrintWriter pw = new PrintWriter(fw);


            String line = accountTF.getText() + "|"
                    + nameTF.getText() + "|"
                    + phoneTf.getText() + "|"
                    + addressTF.getText() + "|"
                    + meterTypeCombo.getValue() + "|"
                    + tariffTypeCombo.getValue() + "|"
                    + java.time.LocalDateTime.now();
            pw.println(line);

            pw.flush();
            pw.close();
            fw.close();

            //After Save
            setOutputText("Success: Consumer record is Added against account #" + accountTF.getText());
            accountTF.setText(generateRandomId());
            disableAccountComponent(true);
            disableComponents(false);

            //tariffTypeCombo.getItems().removeAll(true)

            setDefaultComoBoxValues();

            nameTF.clear();
            phoneTf.clear();
            addressTF.clear();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            setOutputText("Error " + e.getMessage());
            //new Alert(Alert.AlertType.ERROR, "Error " + e.getMessage()).showAndWait();
        }
    }

    private void setOutputText(String text) {
        if (!outputTextArea.getText().isEmpty()) {
            text = String.format("\n\n%s", text);
        }
        outputTextArea.appendText(text);

        outputTextArea.selectPositionCaret(outputTextArea.getLength());
        outputTextArea.deselect();
    }

    private void setDefaultComoBoxValues() {
        tariffTypeCombo.getItems().setAll("Commercial", "Non-Commercial");
        tariffTypeCombo.setValue("Commercial");
        meterTypeCombo.setValue("Prepaid Meter");
        meterTypeCombo.getItems().setAll("Prepaid Meter", "Postpaid Meter");
    }

    public void handleRadioButtons(ActionEvent event) {
        handleRadioGroup();
    }

    public void handleClearButton(ActionEvent event) {
        outputTextArea.clear();
    }

    private void handleRadioGroup() {
        if (addRb.isSelected()) {
            submitBtn.setText("Add Consumer");
            hintLabel.setText("");

            accountTF.setText(generateRandomId());
            disableAccountComponent(true);
            disableComponents(false);

            setDefaultComoBoxValues();
        } else if (updateRb.isSelected()) {
            submitBtn.setText("Search Consumer");
            hintLabel.setText("You can update the record.");

            accountTF.setText("");
            disableComponents(true);
            disableAccountComponent(false);
        } else if (searchRb.isSelected()) {
            submitBtn.setText("Search Consumer");
            hintLabel.setText("You can search by account number,name & phone number.");

            accountTF.setText("");
            disableComponents(true);
            disableAccountComponent(false);
        }
    }

    private void disableAccountComponent(boolean isDisable) {
        accountTF.setDisable(isDisable);
        //addressLabel.setDisable(isDisable);
    }

    private void disableComponents(boolean isDisable) {
        meterTypeCombo.setDisable(isDisable);
        meterTypeLabel.setDisable(isDisable);
        tariffTypeCombo.setDisable(isDisable);
        tariffTypeLabel.setDisable(isDisable);
        addressLabel.setDisable(isDisable);
        addressTF.setDisable(isDisable);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTF.setText(generateRandomId());
        handleRadioGroup();
    }

    private String generateRandomId() {
        return "CONSUMER-" + (new Date().getTime() / 1000L) % Integer.MAX_VALUE;
    }
}

