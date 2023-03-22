package org.example;

import org.example.model.Event;
import org.example.model.Participation;
import org.example.model.Swimmer;
import org.example.model.User;
import org.example.repository.EventDbRepository;
import org.example.repository.ParticipationDbRepository;
import org.example.repository.SwimmerDbRepository;
import org.example.repository.UserDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        UserDbRepository userRepo=new UserDbRepository(props);
        System.out.println(userRepo.findByEmail("malina12@gmail.com"));

        EventDbRepository eventRepo=new EventDbRepository(props);
        System.out.println("All the events");
        for(Event event:eventRepo.findAll())
            System.out.println(event);

        SwimmerDbRepository swimmerRepo=new SwimmerDbRepository(props);
        System.out.println("All the swimmers");
        for(Swimmer swimmer: swimmerRepo.findAll())
            System.out.println(swimmer);

        ParticipationDbRepository participationRepo=new ParticipationDbRepository(props, eventRepo, swimmerRepo);
        System.out.println("Participations by swimmer");
        Swimmer swimmer = swimmerRepo.findById(1);
        for(Participation p: participationRepo.findBySwimmer(swimmer))
            System.out.println(p);
    }
}