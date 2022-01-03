package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsumerDashboardController implements Initializable {
    public Label nameLabel;
    public Label addressLabel;
    public Label accountIdLabel;
    public CategoryAxis barChart;
    public Label tariffTypeLabel;
    public Label totalUsageLabel;
    public Label closeReadingsLabel;
    public Label openReadingsLabel;

    public void handleGenerateButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
