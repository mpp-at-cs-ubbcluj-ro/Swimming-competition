package org.example;

import org.example.model.Event;
import org.example.repository.EventDbRepository;

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

        EventDbRepository eventRepo=new EventDbRepository(props);
        eventRepo.add(new Event(200,"spate"));
        System.out.println("Toate event-urile din db");
        for(Event event:eventRepo.findAll())
            System.out.println(event);
    }
}