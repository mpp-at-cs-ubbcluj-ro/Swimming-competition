package org.example.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.message.Message;
import org.example.Main;
import org.example.model.User;
import org.example.service.IServices;
import org.example.service.ServiceException;

import java.io.IOException;

public class LoginController {
    private IServices server;
    private MainController mainController;
    @FXML
    TextField emailText;
    @FXML
    PasswordField passwordText;
    @FXML
    Button loginButton;
    Parent mainParent;

    public LoginController() {
        System.out.println("Login Controller called");
    }

    public void setServer(IServices service) {
        this.server = service;
    }

    public void setParent(Parent p) {
        mainParent = p;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void loginButtonClicked(ActionEvent actionEvent) throws IOException, ServiceException {
        try {
            String email = emailText.getText();
            String password = passwordText.getText();

            emailText.clear();
            passwordText.clear();

            User newUser = new User(null, null, email, password);
            User logged = server.login(newUser, mainController);

            showNewWindow(logged);
        } catch (ServiceException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void showNewWindow(User user) throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);

        mainController.initModel();
        mainController.setUser(user);

        stage.setTitle("Swimmers window");
        stage.setWidth(800);
        stage.setHeight(650);
        stage.show();

        Stage thisStage = (Stage) loginButton.getScene().getWindow();
        thisStage.hide();
    }

}

