package com.example.projectjavafx;

import com.example.projectjavafx.controller.LoginController;
import com.example.projectjavafx.repository.EventDbRepository;
import com.example.projectjavafx.repository.ParticipationDbRepository;
import com.example.projectjavafx.repository.SwimmerDbRepository;
import com.example.projectjavafx.repository.UserDbRepository;
import com.example.projectjavafx.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/loginView.fxml"));
        VBox root = loader.load();

        UserDbRepository userDbRepository = new UserDbRepository(props);
        EventDbRepository eventDbRepository = new EventDbRepository(props);
        SwimmerDbRepository swimmerDbRepository = new SwimmerDbRepository(props);
        ParticipationDbRepository participationDbRepository = new ParticipationDbRepository(props, eventDbRepository, swimmerDbRepository);

        Service service = new Service(userDbRepository, eventDbRepository, swimmerDbRepository, participationDbRepository);
        LoginController ctrl = loader.getController();
        ctrl.setService(service);

        stage.setScene(new Scene(root, 440, 420));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}