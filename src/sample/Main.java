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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //showLogin();
        Parent root = FXMLLoader.load(getClass().getResource("ui/consumerdashboard.fxml"));
        primaryStage.setTitle("Energy Supplier System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/
    }

    private void showLogin() {
        Stage primaryStage = new Stage();
        final int SPACING = 10;
        final int PADDING = 10;
        VBox vBox = new VBox(0);

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        String currentDir = System.getProperty("user.dir") + "\\src\\sample\\images\\logo.png";
        System.out.println("Working Directory = " + currentDir);

        Image image = null;
        try {
            image = new Image(new FileInputStream("assets/logo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(260);

        //Email
        HBox hBox = new HBox(SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(0, 0, 10, 0));

        Label label = new Label("Email:      ");
        TextField emailField = new TextField();

        hBox.getChildren().add(label);
        hBox.getChildren().add(emailField);

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
            System.exit(0);
        });

        loginButton.setOnAction(event -> {
            if (emailField.getText().toString().isEmpty()) {

                //Group root = new Group();
                //Label label2 = new Label(":pp");
                //vBox.getChildren().add(hBox3);

                //root.getChildren().add(label2);

                ((Node) (event.getSource())).getScene().getWindow().hide();


                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("ui/Consumer.fxml"));
                    primaryStage.setTitle("Energy Supplier System");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //new Alert(Alert.AlertType.ERROR, "Email is  Required.").showAndWait();
            } else if (passwordField.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Password is  Required.").showAndWait();
            } else if (!validateEmailAddress(emailField.getText().toString())) {
                new Alert(Alert.AlertType.ERROR, "Email is invalid.").showAndWait();
            } else {
                String pass = "admin";
                String email = "admin@admin.com";

                if (pass.equals(passwordField.getText()) && email.equals(emailField.getText())) {


                    // Hide this current window (if this is what you want)

                } else {
                    new Alert(Alert.AlertType.ERROR, "Email or Password is incorrect").showAndWait();
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


    public boolean validateEmailAddress(String emailAddress) {

        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = regexPattern.matcher(emailAddress);
        return regMatcher.matches();

    }
}
