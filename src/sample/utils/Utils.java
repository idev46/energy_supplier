package sample.utils;

import sample.models.Consumer;
import sample.models.Reading;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String CONSUMERS_FILE_NAME = "record/consumers.txt";
    public static final String READINGS_FILE_NAME = "record/readings.txt";
    public static final String SPLITTER = "\\|";
    public static final String SPLITTER2 = "|";


    public static Consumer getConsumerFromFile(String[] tokens) {
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

    public static ArrayList<Consumer> getAllConsumers() throws IOException {
        Path path = Paths.get(CONSUMERS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        ArrayList<Consumer> consumers = new ArrayList<>();
        for (String line : lines) {
            Consumer consumer = getConsumerFromFile(line.split(Utils.SPLITTER));
            consumers.add(consumer);
        }

        return consumers;
    }

    public static Consumer getConsumerByAccountNumber(String accountNumber) throws IOException {
        Path path = Paths.get(CONSUMERS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Consumer consumer = new Consumer();

        for (String line : lines) {
            if (line.split(Utils.SPLITTER)[0].equals(accountNumber)) {
                consumer = getConsumerFromFile(line.split(Utils.SPLITTER));
                break;
            }
        }

        return consumer;
    }

    public static Reading getReadingsFromLine(String[] tokens) throws IOException {
        Reading reading = new Reading();
        Consumer consumer= Utils.getConsumerByAccountNumber(tokens[0]);
        reading.setConsumer(consumer);
        reading.setMonth(tokens[1]);
        reading.setYear(tokens[2]);
        reading.setOpeningReadings(tokens[3]);
        reading.setClosingReadings(tokens[4]);
        reading.setCostPerUnit(tokens[5]);
        reading.setPaymentStatus(tokens[6]);

        return reading;
    }

    public static ArrayList<Reading> getAllReadings() throws IOException {
        Path path = Paths.get(READINGS_FILE_NAME);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int count=0;
        ArrayList<Reading> readings = new ArrayList<>();
        for (String line : lines) {
            count++;
            Reading reading = getReadingsFromLine(line.split(Utils.SPLITTER));
            reading.setRecordNo(count);
            readings.add(reading);
        }

        return readings;
    }
}
