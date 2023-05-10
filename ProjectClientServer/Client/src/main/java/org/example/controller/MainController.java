package org.example.controller;

import org.apache.logging.log4j.core.lookup.MainMapLookup;
import org.apache.logging.log4j.message.Message;
import javafx.application.Platform;
import org.example.Main;
import org.example.model.*;
import org.example.service.IObserver;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.service.IServices;
import org.example.service.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController implements IObserver{
    private IServices server;
    private User user;
    ObservableList<EventDTO> events = FXCollections.observableArrayList();
    ObservableList<ParticipantDTO> participations = FXCollections.observableArrayList();
    @FXML
    TableView<EventDTO> tableViewEvents = new TableView<>();
    @FXML
    TableView<ParticipantDTO> tableViewParticipations;
    @FXML
    TableColumn<EventDTO, Integer> tableColumnDistance = new TableColumn<>();
    @FXML
    TableColumn<EventDTO, String> tableColumnStyle = new TableColumn<>();
    @FXML
    TableColumn<EventDTO, Integer> tableColumnNrSwimmers = new TableColumn<>();

    //TableColumn<EventDTO, String> tableColumnSelection;
    @FXML
    TableColumn<ParticipantDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<ParticipantDTO, String> tableColumnLastName;
    @FXML
    TableColumn<ParticipantDTO, String> tableColumnEvents;
    @FXML
    TableColumn<ParticipantDTO, Integer> tableColumnAge;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    Button addButton;
    @FXML
    Button logOutButton;
    @FXML
    DatePicker datePicker;

    public void setServer(IServices server) throws ServiceException {
        this.server = server;
        initialize1();
        initialize2();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize1() {
        tableColumnDistance.setCellValueFactory(new PropertyValueFactory<EventDTO, Integer>("distance"));
        tableColumnStyle.setCellValueFactory(new PropertyValueFactory<EventDTO, String>("style"));
        tableColumnNrSwimmers.setCellValueFactory(new PropertyValueFactory<EventDTO, Integer>("nrSwimmers"));
        tableViewEvents.setItems(events);
        tableViewEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    @FXML
    public void initialize2() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("lastName"));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, Integer>("age"));
        tableColumnEvents.setCellValueFactory(new PropertyValueFactory<ParticipantDTO, String>("events"));
        tableViewParticipations.setItems(participations);
    }
    public void initModel() {
        try {
            EventDTO[] listEvent = server.getAllEvents();
            List<EventDTO> allEvent = new ArrayList<>(Arrays.asList(listEvent));
            events.setAll(allEvent);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void handleViewButton() {
        try {
            EventDTO eventDTO = tableViewEvents.getSelectionModel().getSelectedItem();
            Event event = new Event(eventDTO.getDistance(), eventDTO.getStyle());
            event.setId(eventDTO.getId());
            ParticipantDTO[] participantDTOS = server.getParticipants(event);
            participations.setAll(participantDTOS);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    public void handleAddButton() throws ServiceException {
        List<EventDTO> events = tableViewEvents.getSelectionModel().getSelectedItems();
        List<Event> selectedEvents = new ArrayList<>();
        for(EventDTO eventDTO: events) {
            Event e = new Event(eventDTO.getDistance(), eventDTO.getStyle());
            e.setId(eventDTO.getId());
            selectedEvents.add(e);
        }


        LocalDateTime birth_date = datePicker.getValue().atTime(0,0,0);
        Swimmer swimmer = new Swimmer(textFieldFirstName.getText(), textFieldLastName.getText(), birth_date);
        server.addSwimmer(swimmer);
        Swimmer addedSwimmer = server.findLastSwimmer();

        try {
            for (Event e : selectedEvents) {
                Participation p = new Participation(addedSwimmer, e);
                System.out.println("PARTICIPATION MAIN" + p);
                server.addParticipation(p);
            }
            server.updateEvent();
        } catch (ServiceException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(ex.toString());
            alert.show();
        }
        textFieldFirstName.clear();
        textFieldLastName.clear();
    }
    public void handleLogOut() {
        try {
            server.logout(user, this);
        } catch(ServiceException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
        Stage thisStage = (Stage) logOutButton.getScene().getWindow();
        thisStage.close();
    }
    @Override
    public void updateEvents() {
        Platform.runLater(() -> {initModel();
            });

    }

}

