package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.models.Reading;
import sample.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label totalConsumersLabel;
    public Label energyLabel;
    public Button consumerMenuBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabelsData();
    }

    //After getting data from files, total kWh and total consumers is setting on labels
    private void setLabelsData() {
        try {
            int count = Utils.getConsumersCount();
            totalConsumersLabel.setText(String.valueOf(count));

            ArrayList<Reading> readings = Utils.getAllReadings();
            double usage = Utils.getConsumedUnits(readings);
            energyLabel.setText(String.valueOf(usage) + " kWh");
        } catch (IOException e) {
            totalConsumersLabel.setText("0");
            energyLabel.setText("0 kWh");
        }
    }

    //Moving to consumer Menu
    public void handleConsumerMenuButton(ActionEvent event) throws IOException {
        switchStages("Consumer.fxml");
    }

    //Moving to Reading Menu
    public void handleReadingsMenuButton(ActionEvent event) {
        switchStages("Readings.fxml");
    }

    /*public void handleConsumerDashBoardButton(ActionEvent event) {
        switchStages("ConsumerDashboard.fxml");
    }*/

    //Logout and exiting the app
    public void handleLogoutButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    //Moving this screen to another by its name, like Consumer.fxml
    private void switchStages(String fileName) {
        try {
            String path = "src/sample/ui/";
            URL url = new File(path.concat(fileName)).toURI().toURL();
            Parent parent = FXMLLoader.load(url);

            /*Parent parent=  FXMLLoader.load(getClass().getResource("ui/Readings.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage =(Stage) consumerMenuBtn.getScene().getWindow();*/

            // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.setResizable(false);
            stage.setTitle("Energy Supplier System");
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                setLabelsData();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
