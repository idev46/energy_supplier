package sample.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.Consumer;
import sample.models.Reading;
import sample.models.Usage;
import sample.utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConsumerDashboardController implements Initializable {
    public Label nameLabel;
    public Label addressLabel;
    public Label accountIdLabel;
    public BarChart barChart;
    public Label tariffTypeLabel;
    public Label totalUsageLabel;
    public Label closeReadingsLabel;
    public Label openReadingsLabel;
    public Button generateBillBtn;
    public TableView<Usage> historyTable;
    public TableColumn<Usage, String> monthsColumn;
    public TableColumn<Usage, Double> unitsColumn;
    public TableColumn<Usage, String> paymentColumn;
    public TableColumn<Usage, Double> amountColumn;
    private Consumer consumer;
    private Reading latestReading;

    //Generate and create invoice of consumer reading and open file in pdf viewer
    public void handleGenerateButton(ActionEvent event) {
        String fileName = Utils.generateFileName(consumer.getAccountNumber());
        try {
            Utils.createInvoice(latestReading, fileName);
            Utils.openFile(fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String consumerAccountNo = Utils.accountNo;
            consumer = Utils.getConsumerByAccountNumber(consumerAccountNo);
            accountIdLabel.setText(consumer.getAccountNumber());
            addressLabel.setText(consumer.getCurrentAddress());
            nameLabel.setText(consumer.getName());
            tariffTypeLabel.setText(consumer.getEnergyTariff() + " Tariff");

            ArrayList<Reading> readings = Utils.getAllReadingsByAccountNo(consumerAccountNo);

            if (readings.isEmpty()) {
                generateBillBtn.setDisable(true);
            } else {
                openReadingsLabel.setText(readings.get(readings.size() - 1).getOpeningReadings());
                closeReadingsLabel.setText(readings.get(readings.size() - 1).getClosingReadings());
                double usage = Utils.getConsumedUnits(readings);
                totalUsageLabel.setText(String.valueOf(usage) + " kWh");

                initializeChart(readings);
                initializeHistoryTable(readings);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }

    //Getting reading from file then adding it ho table
    private void initializeHistoryTable(ArrayList<Reading> readings) {
        monthsColumn.setCellValueFactory(new PropertyValueFactory<Usage, String>("month"));
        unitsColumn.setCellValueFactory(new PropertyValueFactory<Usage, Double>("units"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Usage, Double>("amount"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<Usage, String>("PaymentStatus"));

        ObservableList<Usage> usageList = FXCollections.observableArrayList();

        for (int i = 0; i < 12; i++) {

            for (Reading reading : readings) {
                latestReading = reading;

                int month = Integer.parseInt(reading.getMonth());
                month = month - 1;
                if (month == i) {
                    Usage usage = new Usage();
                    usage.setMonth(reading.getMonth() + " - " + Utils.MONTHS.get(i));
                    usage.setAmount(reading.getTotalCost());
                    usage.setUnits(reading.getCurrentUsedUnits());
                    usage.setPaymentStatus(reading.getPaymentStatus());
                    usageList.add(usage);
                }
            }
        }
        historyTable.setItems(usageList);
        historyTable.setEditable(false);
        historyTable.setSelectionModel(null);
    }

    //Initialising the chart after getting reading data from file on monthly basis
    private void initializeChart(ArrayList<Reading> readings) {
        XYChart.Series xySeries = new XYChart.Series();

        for (int i = 0; i < 12; i++) {
            boolean isFound = false;

            for (Reading reading : readings) {
                int month = Integer.parseInt(reading.getMonth());
                month = month - 1;
                if (month == i) {
                    xySeries.getData().add(new XYChart.Data(Utils.MONTHS.get(i), reading.getCurrentUsedUnits()));
                    isFound = true;
                }
            }

            if (!isFound) {
                xySeries.getData().add(new XYChart.Data(Utils.MONTHS.get(i), 0));
            }

        }

        barChart.getData().add(xySeries);
        barChart.setLegendVisible(false);
    }
}
