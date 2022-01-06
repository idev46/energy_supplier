package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        Parent root = FXMLLoader.load(getClass().getResource("ui/consumer.fxml"));
        primaryStage.setTitle("Energy Supplier System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
*/
        showLogin();
    }

    //Show login Screeen
    private void showLogin() {
        String pass = "admin";
        String userName = "admin";

        Stage primaryStage = new Stage();
        final int SPACING = 10;
        final int PADDING = 10;
        VBox vBox = new VBox(0);

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));

        /*
         String currentDir = System.getProperty("user.dir") + "\\src\\sample\\images\\logo.png";
                System.out.println("Working Directory = " + currentDir);
        */

        Image image = null;
        try {
            image = new Image(new FileInputStream("src/images/logo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(260);

        //Username
        HBox hBox = new HBox(SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(0, 0, 10, 0));

        Label label = new Label("Username:   ");
        TextField userNameField = new TextField();

        hBox.getChildren().add(label);
        hBox.getChildren().add(userNameField);

        //Password
        HBox hBox1 = new HBox(SPACING);
        hBox1.setAlignment(Pos.CENTER);

        Label label1 = new Label("Password:");
        PasswordField passwordField = new PasswordField();


        hBox1.getChildren().add(label1);
        hBox1.getChildren().add(passwordField);


        HBox hBox2 = new HBox();
        hBox2.setMinHeight(30);

        //BUTTONS
        HBox hBox3 = new HBox(SPACING);
        hBox3.setAlignment(Pos.CENTER);

        Button quitButton = new Button("Quit");
        quitButton.setMinWidth(100);
        Button loginButton = new Button("Login");
        loginButton.setMinWidth(100);

        quitButton.setOnAction(event -> {
            Platform.exit();
        });

        loginButton.setOnAction(event -> {
            if (userNameField.getText().toString().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "UserName is  Required.").showAndWait();
            } else if (passwordField.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Password is  Required.").showAndWait();
            } else {
                if (pass.equals(passwordField.getText()) && userName.equals(userNameField.getText())) {
                    // Hide this current window ()
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    switchToDashboard();
                } else {
                    new Alert(Alert.AlertType.ERROR, "UserName or Password is incorrect").showAndWait();
                }
            }
        });

        hBox3.getChildren().add(quitButton);
        hBox3.getChildren().add(loginButton);

        Group root = new Group();
        vBox.getChildren().add(imageView);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(hBox1);
        vBox.getChildren().add(hBox2);
        vBox.getChildren().add(hBox3);

        root.getChildren().add(vBox);

        primaryStage.setTitle("Energy Supplier System");
        primaryStage.setScene(new Scene(root, 500, 450));
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    //After successful login switch this screen to dashboard screen
    private void switchToDashboard() {
        try {
            String path = "src/sample/ui/";
            URL url = new File(path.concat("Dashboard.fxml")).toURI().toURL();
            Parent parent = FXMLLoader.load(url);

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.setResizable(false);
            stage.setTitle("Energy Supplier System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
