package org.example.repository;

import org.example.model.Event;
import org.example.model.Participation;
import org.example.model.Swimmer;

public interface IParticipationRepository extends IRepository<Participation, Integer> {
    Iterable<Participation> findBySwimmer(Swimmer swimmer);

    Iterable<Participation> findByEvent(Event event);
}
