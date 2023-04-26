package com.example.projectjavafx.controller;

import com.example.projectjavafx.model.Event;
import com.example.projectjavafx.model.Participation;
import com.example.projectjavafx.model.Swimmer;
import com.example.projectjavafx.repository.RepositoryException;
import com.example.projectjavafx.service.Service;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private Service service;
    ObservableList<Event> events = FXCollections.observableArrayList();
    ObservableList<Participation> participations = FXCollections.observableArrayList();
    @FXML
    TableView<Event> tableViewEvents;
    @FXML
    TableView<Participation> tableViewParticipations;
    @FXML
    TableColumn<Event, Integer> tableColumnDistance;
    @FXML
    TableColumn<Event, String> tableColumnStyle;
    @FXML
    TableColumn<Event, Integer> tableColumnNrSwimmers;
    @FXML
    TableColumn<Event, String> tableColumnSelection;
    @FXML
    TableColumn<Participation, String> tableColumnFirstName;
    @FXML
    TableColumn<Participation, String> tableColumnLastName;
    @FXML
    TableColumn<Participation, String> tableColumnEvents;
    @FXML
    TableColumn<Participation, Integer> tableColumnAge;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    TextField textFieldAge;
    @FXML
    Button addButton;
    @FXML
    Button logOutButton;
    @FXML
    DatePicker datePicker;

    public void setService(Service service) {
        this.service = service;
        initializeTable1();
        initModel1();
    }

    @FXML
    public void initializeTable1() {
        tableColumnDistance.setCellValueFactory(new PropertyValueFactory<Event, Integer>("distance"));
        tableColumnStyle.setCellValueFactory(new PropertyValueFactory<Event, String>("style"));
        tableColumnNrSwimmers.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Event, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Event, Integer> p) {
                Integer nrSwimmers = service.swimmerNumberByEvent(p.getValue());
                return new SimpleIntegerProperty(nrSwimmers).asObject();
            }
        });
        tableColumnSelection.setCellValueFactory(new PropertyValueFactory<Event, String>("select"));
        tableViewEvents.setItems(events);
        //tableViewEvents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void initializeTable2() {
        tableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Participation, String> p) {
                return new SimpleStringProperty(p.getValue().getSwimmer().getFirstName());
            }
        });

        tableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Participation, String> p) {
                return new SimpleStringProperty(p.getValue().getSwimmer().getLastName());
            }
        });

        tableColumnAge.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participation, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Participation, Integer> p) {
                LocalDate date1 = LocalDate.now();
                LocalDate date2 = p.getValue().getSwimmer().getBirthDate();
                Period period = date2.until(date1);
                int years = period.getYears();
                return new SimpleIntegerProperty(years).asObject();
            }
        });

        tableColumnEvents.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Participation, String> p) {
                List<Event> eventsForSwimmer = service.eventsForSwimmer(p.getValue().getSwimmer());
                return new SimpleStringProperty(eventsForSwimmer.toString());
            }
        });

        tableViewParticipations.setItems(participations);
        tableViewParticipations.getColumns().clear();
        tableViewParticipations.getColumns().addAll(tableColumnFirstName, tableColumnLastName, tableColumnAge, tableColumnEvents);
    }

    public void handleViewButton() {
        Event event= tableViewEvents.getSelectionModel().getSelectedItem();
        List<Participation> participations_list = service.participationsByEvent(event);
        initializeTable2();
        participations.setAll(participations_list);

    }

    private void initModel1() {
        List<Event> events_list = service.getAllEvents();
        events.setAll(events_list);
    }

    public void handleAddButton() {
        //List<Event> events = tableViewEvents.getSelectionModel().getSelectedItems();
        //List<Event> selectedEvents = service.getCheckedEvents();
        List<Event> selectedEvents = new ArrayList<>();
        for(Event e: this.events) {
            if(e.getSelect().isSelected())
                selectedEvents.add(e);
        }
        LocalDate birth_date = datePicker.getValue();
        Swimmer swimmer = new Swimmer(textFieldFirstName.getText(), textFieldLastName.getText(), birth_date);
        int id = service.getAllSwimmers().size() + 1;
        service.addSwimmer(swimmer);
        swimmer.setId(id);
        try {
            for (Event e : selectedEvents) {
                Participation p = new Participation(swimmer, e);
                service.addParticipation(p);
            }
        } catch (RepositoryException ex) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(ex.toString());
            alert.show();
        }
        textFieldFirstName.clear();
        textFieldLastName.clear();
    }
    public void handleLogOut() {
        Stage thisStage = (Stage) addButton.getScene().getWindow();
        thisStage.hide();
    }

}
