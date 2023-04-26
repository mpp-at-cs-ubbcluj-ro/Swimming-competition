package org.example;

import org.example.model.User;
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
        UserDbRepository userRepo = new UserDbRepository(props);
        User user = userRepo.findByEmailPassword("mail", "parola");
        System.out.println(user);

    }
}