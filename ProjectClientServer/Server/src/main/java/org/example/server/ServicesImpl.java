package org.example.server;

import org.example.model.*;
import org.example.repository.IEventRepository;
import org.example.repository.IParticipationRepository;
import org.example.repository.ISwimmerRepository;
import org.example.repository.IUserRepository;
import org.example.service.IObserver;
import org.example.service.IServices;
import org.example.service.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

public class ServicesImpl implements IServices {
    private IUserRepository userRepo;
    private IEventRepository eventRepo;
    private ISwimmerRepository swimmerRepo;
    private IParticipationRepository participationRepo;
    private Map<String, IObserver> loggedClients;

    public ServicesImpl(IUserRepository userRepo, IEventRepository eventRepo, ISwimmerRepository swimmerRepo, IParticipationRepository participationRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.swimmerRepo = swimmerRepo;
        this.participationRepo = participationRepo;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(User user, IObserver client) throws ServiceException {
        String email = user.getEmail();
        String password = user.getPassword();

        User foundUser = userRepo.findByEmail(email);
        if (foundUser == null) {
            throw new ServiceException("It doesn't exist a person with this email!");
        }
        else if(!foundUser.getPassword().equals(password)) {
            throw new ServiceException("Incorrect password!");
        }
        else if(loggedClients.get(foundUser.getEmail()) != null) {
            throw new ServiceException("Client already logged in!");
        }
        loggedClients.put(foundUser.getEmail(), client);
        return foundUser;
    }

    public Integer swimmerNumberByEvent(Event event) {
        List<Participation> swimmersByEvent = StreamSupport.stream(participationRepo.findByEvent(event).spliterator(), false)
                .toList();
        return swimmersByEvent.size();
    }

    /*public synchronized EventDTO[] getAllEvents() {
        System.out.println("Am ajuns in SERVICE IMPL");
        List<EventDTO> eventNrSwimmersDTOList = new ArrayList<>();
        System.out.println("Lista events" + eventRepo.findAll());
        for (Event e : eventRepo.findAll()) {
            EventDTO entity = new EventDTO(e.getDistance(), e.getStyle(), swimmerNumberByEvent(e));
            entity.setId(e.getId());
            eventNrSwimmersDTOList.add(entity);
        }
        System.out.println("Lista events" + eventNrSwimmersDTOList);
        return eventNrSwimmersDTOList.stream().toArray(EventDTO[]::new);
    }*/
    @Override
    public synchronized EventDTO[] getAllEvents() {
        List<EventDTO> events = new ArrayList<>();
        for(Event e: eventRepo.findAll()) {
            EventDTO entity = new EventDTO(e.getDistance(), e.getStyle(), swimmerNumberByEvent(e));
            entity.setId(e.getId());
            events.add(entity);
        }
        return events.stream().toArray(EventDTO[]::new);
    }
    public List<Participation> participationsByEvent(Event event) {
        return StreamSupport.stream(participationRepo.findByEvent(event).spliterator(), false)
                .toList();
    }
    public List<Event> eventsForSwimmer(Swimmer swimmer) {
        List<Event> events = new ArrayList<>();
        for(Participation p: participationRepo.findAll()) {
            if(p.getSwimmer().equals(swimmer))
                events.add(p.getEvent());
        }
        return events;
    }
    @Override
    public synchronized ParticipantDTO[] getParticipants(Event e) {
        List<ParticipantDTO> participantsList = new ArrayList<>();
        for(Participation p: participationsByEvent(e)) {
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = p.getSwimmer().getBirthDate();
            LocalDate date11 = date1.toLocalDate();
            LocalDate date22 = date2.toLocalDate();
            Period period = date22.until(date11);
            int years = period.getYears();

            String events = this.eventsForSwimmer(p.getSwimmer()).toString();
            ParticipantDTO participantDTO = new ParticipantDTO(p.getSwimmer().getFirstName(), p.getSwimmer().getLastName(), years, events);
            participantsList.add(participantDTO);
        }
        return participantsList.stream().toArray(ParticipantDTO[]::new);
    }
    @Override
    public synchronized void addParticipation(Participation participation) throws ServiceException {
        Event event = participation.getEvent();
        for(Event e: eventsForSwimmer(participation.getSwimmer())){
            if(event.equals(e))
                throw new ServiceException("This swimmer is already enrolled at this event!");
        }
        participationRepo.add(participation);
    }
    @Override
    public void addSwimmer(Swimmer swimmer) throws ServiceException {
        swimmerRepo.add(swimmer);
    }
    @Override
    public Swimmer findLastSwimmer() {
        List<Swimmer> swimmers = (List<Swimmer>) swimmerRepo.findAll();
        return swimmers.get(swimmers.size()-1);
    }
    @Override
    public synchronized void logout(User user, IObserver client) throws ServiceException {
        IObserver c = loggedClients.get(user.getEmail());
        if(c == null) {
            throw new ServiceException("User was not logged in!");
        }
    }

    @Override
    public void updateEvent() throws ServiceException {
        /*List<EventDTO> result = new ArrayList<>();
        for(EventDTO e: getAllEvents()) {
            if(Objects.equals(newEvent.getId(), e.getId())) {
                EventDTO added = new EventDTO(e.getDistance(), e.getStyle(), e.getNrSwimmers()+1);
                added.setId(e.getId());

            }
        }*/
        notifyUsers();
    }

    private synchronized void notifyUsers() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for(String email: loggedClients.keySet()) {
            IObserver client = loggedClients.get(email);
            if(client != null) {
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying user: " + email);
                        client.updateEvents();
                    } catch(ServiceException ex) {
                        System.err.println("Error at notify: "+ ex.getMessage());
                    }
                });
            }
        }
        executor.shutdown();
    }
}
