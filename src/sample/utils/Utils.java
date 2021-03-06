package sample.utils;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import sample.models.Consumer;
import sample.models.Reading;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utils {

    //CONSTANTS
    public static final String CONSUMERS_FILE_NAME = "record/consumers.txt";
    public static final String READINGS_FILE_NAME = "record/readings.txt";
    public static final String INVOICES_LOCATION = "invoices/";

    //TO EXTRACT DATA FROM FILE
    public static final String OUTPUT_SPLITTER = "\\|";
    public static final String INPUT_SPLITTER = "|";

    public static final double GST = 0.19;
    public static final double VAT = 0.05;
    public static final double ADDITIONAL_CHARGES = 0.05;

    public static final String CURRENCY_SYMBOL = "$";
    public static String accountNo="";

    //Months List
    public static final List<String> MONTHS = Arrays.asList("January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December");


    //Retrieve Consumer from Line
    public static Consumer getConsumerFromLine(String[] tokens) {
        Consumer consumer = new Consumer();
        consumer.setAccountNumber(tokens[0]);
        consumer.setName(tokens[1]);
        consumer.setPhoneNo(tokens[2]);
        consumer.setCurrentAddress(tokens[3]);
        consumer.setMeterType(tokens[4]);
        consumer.setEnergyTariff(tokens[5]);
        consumer.setRegistrationDate(tokens[6]);
        return consumer;
    }

    //Retrieve all Consumers from file
    public static ArrayList<Consumer> getAllConsumers() throws IOException {
        Path path = Paths.get(CONSUMERS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        ArrayList<Consumer> consumers = new ArrayList<>();
        for (String line : lines) {
            Consumer consumer = getConsumerFromLine(line.split(Utils.OUTPUT_SPLITTER));
            consumers.add(consumer);
        }

        return consumers;
    }


    //Retrieve number of consumers saved in file
    public static int getConsumersCount() throws IOException {
        Path path = Paths.get(CONSUMERS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines.size();
    }

    //Retrieve consumer By account no from fie
    public static Consumer getConsumerByAccountNumber(String accountNumber) throws IOException {
        Path path = Paths.get(CONSUMERS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Consumer consumer = new Consumer();

        for (String line : lines) {
            if (line.split(Utils.OUTPUT_SPLITTER)[0].equals(accountNumber)) {
                consumer = getConsumerFromLine(line.split(Utils.OUTPUT_SPLITTER));
                break;
            }
        }

        return consumer;
    }

    //Retrieve reading from line that is Retrieved from file
    public static Reading getReadingsFromLine(String[] tokens) throws IOException {
        Reading reading = new Reading();
        Consumer consumer = Utils.getConsumerByAccountNumber(tokens[0]);
        reading.setConsumer(consumer);
        reading.setMonth(tokens[1]);
        reading.setYear(tokens[2]);
        reading.setOpeningReadings(tokens[3]);
        reading.setClosingReadings(tokens[4]);
        reading.setCostPerUnit(tokens[5]);
        reading.setPaymentStatus(tokens[6]);
        return reading;
    }
    //Retrieve all readings from file
    public static ArrayList<Reading> getAllReadings() throws IOException {
        Path path = Paths.get(READINGS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int count = 0;
        ArrayList<Reading> readings = new ArrayList<>();
        for (String line : lines) {
            count++;
            Reading reading = getReadingsFromLine(line.split(Utils.OUTPUT_SPLITTER));
            reading.setRecordNo(count);
            readings.add(reading);
        }

        return readings;
    }


    //Get Readings By consumer account No
    public static ArrayList<Reading> getAllReadingsByAccountNo(String accountNo) throws IOException {
        Path path = Paths.get(READINGS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int count = 0;
        ArrayList<Reading> readings = new ArrayList<>();
        for (String line : lines) {
            count++;
            Reading reading = getReadingsFromLine(line.split(Utils.OUTPUT_SPLITTER));
            if (reading.getConsumer().getAccountNumber().equals(accountNo)) {
                reading.setRecordNo(count);
                readings.add(reading);
            }
        }

        return readings;
    }

    //generate file name for invoice
    public static String generateFileName(String accountNo) {
        return Utils.INVOICES_LOCATION.concat(accountNo).concat(".pdf");
    }

    //get total consumed units by given readings list
    public static double getConsumedUnits(ArrayList<Reading> readings) {
        double total = 0;
        for (Reading reading : readings) {
            total = total + reading.getCurrentUsedUnits();
        }
        return total;
    }

    //Open invoice file that is generated and created
    public static void openFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!Desktop.isDesktopSupported()) {
            new Alert(Alert.AlertType.ERROR, "No supported program is found to open generated bill. File: " + fileName).showAndWait();
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file.exists())
            desktop.open(file);
    }

    //Create inoice for consumer bill
    public static void createInvoice(Reading reading, String fileName) throws FileNotFoundException, DocumentException {
        Document document = new Document();

        Consumer consumer = reading.getConsumer();

        com.itextpdf.text.Font catFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font redFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12,
                com.itextpdf.text.Font.NORMAL, BaseColor.RED);
        com.itextpdf.text.Font smallBold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12,
                com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 12);


        PdfWriter.getInstance(document, new FileOutputStream(fileName));

        document.open();

        Paragraph preface = new Paragraph();

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(consumer.getAccountNumber(), catFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "Consumer Name: " + consumer.getName(),
                smallFont));

        preface.add(new Paragraph(
                "Consumer PhoneNo: " + consumer.getPhoneNo(),
                smallFont));

        preface.add(new Paragraph(
                "Consumer Address: " + consumer.getCurrentAddress(),
                smallFont));

        preface.add(new Paragraph(
                "Tariff Type: " + consumer.getEnergyTariff(),
                smallFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "Open readings: " + reading.getOpeningReadings(),
                smallFont));

        preface.add(new Paragraph(
                "Close readings: " + reading.getClosingReadings(),
                smallFont));

        preface.add(new Paragraph(
                "Consumed Units: " + reading.getCurrentUsedUnits(),
                smallFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "Cost Calculations ", smallBold));

        preface.add(new Paragraph(
                "Cost per Unit: " + reading.getCostPerUnit() + CURRENCY_SYMBOL,
                smallFont));

        preface.add(new Paragraph(
                "Energy Cost: " + reading.getCostWithoutTaxes() + CURRENCY_SYMBOL,
                smallFont));

        preface.add(new Paragraph(
                "Vat: " + Utils.VAT * 100 + "%",
                smallFont));

        preface.add(new Paragraph(
                "GST: " + Utils.GST * 100 + "%",
                smallFont));

        preface.add(new Paragraph(
                "Additional Charges: " + Utils.ADDITIONAL_CHARGES * 100 + "%",
                smallFont));

        preface.add(new Paragraph(
                "Taxes & Additional Charges: " + reading.getTaxes() + CURRENCY_SYMBOL,
                smallFont));

        preface.add(new Paragraph(
                "Total Bill: " + reading.getCostWithoutTaxes() + " + " + reading.getTaxes() + " = " + reading.getTotalCost() + CURRENCY_SYMBOL,
                smallFont));

        addEmptyLine(preface, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        preface.add(new Paragraph(
                "Bill is generated at " + formatter.format(date) + "\nNorthampton Energy Supplier ??2022",
                redFont));

        document.add(preface);
        document.close();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
