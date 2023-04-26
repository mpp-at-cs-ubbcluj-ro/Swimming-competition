package org.example.service;

import org.example.model.*;

import java.rmi.ServerException;
import java.util.List;

public interface IServices {
    User login(User user, IObserver client) throws ServiceException;
    EventDTO[] getAllEvents() throws ServiceException;
    ParticipantDTO[] getParticipants(Event e) throws ServiceException;
    void addParticipation(Participation participation) throws ServiceException;
    void addSwimmer(Swimmer swimmer) throws ServiceException;
    Swimmer findLastSwimmer() throws ServiceException;
    void logout(User user, IObserver client) throws ServiceException;
    void updateEvent() throws ServiceException;
}
