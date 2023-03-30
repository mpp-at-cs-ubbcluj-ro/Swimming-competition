package com.example.projectjavafx.repository;

import com.example.projectjavafx.model.Event;
import com.example.projectjavafx.model.Participation;
import com.example.projectjavafx.model.Swimmer;

public interface IParticipationRepository extends IRepository<Participation, Integer> {
    Iterable<Participation> findBySwimmer(Swimmer swimmer);

    Iterable<Participation> findByEvent(Event event);

}
