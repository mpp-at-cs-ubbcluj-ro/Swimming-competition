package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.controller.LoginController;
import org.example.controller.MainController;
import org.example.service.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("In start");
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("app.server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("app.server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("loginView.fxml"));
        Parent root = loader.load();

        LoginController logCtrl = loader.getController();
        logCtrl.setServer(server);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainView.fxml"));
        Parent mroot = mainLoader.load();
        MainController mainCtrl = mainLoader.getController();
        mainCtrl.setServer(server);

        logCtrl.setMainController(mainCtrl);
        logCtrl.setParent(mroot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400,300));
        primaryStage.show();

       }

       public static void main(String[] args) {
        launch();
       }



    }
