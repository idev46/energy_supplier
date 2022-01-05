package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label totalConsumersLabel;
    public Label energyLabel;
    public Button consumerMenuBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            int count = Utils.getConsumersCount();
            totalConsumersLabel.setText(String.valueOf(count));
        } catch (IOException e) {
            totalConsumersLabel.setText(String.valueOf("0"));
        }
    }

    public void handleConsumerMenuButton(ActionEvent event) throws IOException {

        switchStages("Consumer.fxml");

    }

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
                try {
                    String primaryFile ="Dashboard.fxml";
                    URL primaryUrl = new File(path.concat(primaryFile)).toURI().toURL();
                    Parent primaryParent = FXMLLoader.load(primaryUrl);

                    Stage primaryStage = new Stage();
                    primaryStage.setScene(new Scene(primaryParent));
                    primaryStage.setResizable(false);
                    primaryStage.setResizable(false);
                    primaryStage.setTitle("Energy Supplier System");
                    primaryStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void handleReadingsMenuButton(ActionEvent event) {
        switchStages("Readings.fxml");
    }

    public void handleConsumerDashBoardButton(ActionEvent event) {
        switchStages("ConsumerDashboard.fxml");
    }

    public void handleLogoutButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
