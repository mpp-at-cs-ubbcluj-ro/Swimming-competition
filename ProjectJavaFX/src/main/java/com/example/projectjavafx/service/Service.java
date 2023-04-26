package com.example.projectjavafx.service;

import com.example.projectjavafx.model.*;
import com.example.projectjavafx.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private IUserRepository userRepository;
    private IEventRepository eventRepository;
    private ISwimmerRepository swimmerRepository;
    private IParticipationRepository participationRepository;

    public Service(IUserRepository userRepository, IEventRepository eventRepository, ISwimmerRepository swimmerRepository, IParticipationRepository participationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.swimmerRepository = swimmerRepository;
        this.participationRepository = participationRepository;
    }

    public User searchUserByEmail(String email) {
        User foundUser = userRepository.findByEmail(email);
        if(foundUser != null) {
            return foundUser;
        }
        return null;
    }
    public void addSwimmer(Swimmer swimmer) {
        swimmerRepository.add(swimmer);
    }
    public void addParticipation(Participation participation) {
        Event event = participation.getEvent();
        for(Event e: eventsForSwimmer(participation.getSwimmer())){
            if(event.equals(e))
                throw new RepositoryException("This swimmer is already enrolled at this event!");
        }
        participationRepository.add(participation);
    }
    public List<Event> getAllEvents() {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .toList();
    }
    public List<Participation> getAllParticipations() {
        return StreamSupport.stream(participationRepository.findAll().spliterator(), false).toList();
    }
    public List<Swimmer> getAllSwimmers() {
        return StreamSupport.stream(swimmerRepository.findAll().spliterator(), false).toList();
    }
    public List<Event> getCheckedEvents() {
        List<Event> checkedEvents = new ArrayList<>();
        List<Event> events = this.getAllEvents();
        for(Event e: events) {
            if(e.getSelect().isSelected())
                checkedEvents.add(e);
        }
        return checkedEvents;
    }

    /*public List<EventNrSwimmersDBO> getEventNrSwimmers() {
        List<EventNrSwimmersDBO> eventNrSwimmersDBOList = new ArrayList<>();
        for(Event e: getAllEvents()) {
            EventNrSwimmersDBO entity = new EventNrSwimmersDBO(e.getDistance(), e.getStyle(), swimmerNumberByEvent(e));
            entity.setId(e.getId());
            eventNrSwimmersDBOList.add(entity);
        }
        return eventNrSwimmersDBOList;
    }*/
    public List<Participation> participationsByEvent(Event event) {
        return StreamSupport.stream(participationRepository.findByEvent(event).spliterator(), false)
                .toList();
    }
    public List<Event> eventsForSwimmer(Swimmer swimmer) {
        List<Event> events = new ArrayList<>();
        for(Participation p: getAllParticipations()) {
            if(p.getSwimmer().equals(swimmer))
                events.add(p.getEvent());
        }
        return events;
    }
    public Integer swimmerNumberByEvent(Event event) {
        List<Participation> swimmersByEvent = StreamSupport.stream(participationRepository.findByEvent(event).spliterator(), false)
                .toList();
        return swimmersByEvent.size();
    }
}
