package com.example.projectjavafx.controller;

import com.example.projectjavafx.HelloApplication;
import com.example.projectjavafx.model.User;
import com.example.projectjavafx.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Service service;
    @FXML
    TextField emailText;
    @FXML
    PasswordField passwordText;
    @FXML
    Button loginButton;

    @FXML
    Button hereButton;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void loginButtonClicked() throws IOException {
        String email = emailText.getText();
        String password = passwordText.getText();

        emailText.clear();
        passwordText.clear();
        User foundUser = service.searchUserByEmail(email);
        if (foundUser != null) {
            if(foundUser.getPassword().equals(password)) {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/mainView.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(loader.load(), 600, 600);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                MainController eventsCtrl = loader.getController();
                eventsCtrl.setService(service);

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Hello, " + foundUser.getFirstName() + " " + foundUser.getLastName() + " !");
                stage.show();
                stage.centerOnScreen();

                Stage thisStage = (Stage) loginButton.getScene().getWindow();
                thisStage.hide();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Email or password incorrect!");
            alert.show();
        }
    }

}
