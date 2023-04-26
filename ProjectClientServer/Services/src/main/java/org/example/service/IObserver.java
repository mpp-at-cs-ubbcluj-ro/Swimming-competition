package org.example.service;

import org.example.model.User;
import org.example.model.Swimmer;
import org.example.model.Event;

public interface IObserver {
   //void updateEvent() throws ServiceException;
   void updateEvents() throws ServiceException;
}
