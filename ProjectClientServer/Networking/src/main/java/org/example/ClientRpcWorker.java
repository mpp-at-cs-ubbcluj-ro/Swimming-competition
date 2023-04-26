package org.example;

import org.example.model.*;
import org.example.service.IObserver;
import org.example.service.IServices;
import org.example.service.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class ClientRpcWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                System.out.println("WORKER Request: " + request);

                Response response = handleRequest((Request) request);
                System.out.println("WORKER Response: " + response.type());
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("LOGIN request ..." + request.type());
            User user = (User) request.data();
            try {
                User foundUser = server.login(user, this);
                return new Response.Builder().type(ResponseType.LOGIN).data(foundUser).build();
            } catch (ServiceException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type() == RequestType.GET_EVENTS) {
            System.out.println("GET_EVENTS request..." + request.type());
            try {
                EventDTO[] found = server.getAllEvents();
                return new Response.Builder().type(ResponseType.GET_EVENTS).data(found).build();
            } catch (ServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type() == RequestType.GET_PARTICIPANTS) {
            System.out.println("GET_PARTICIPANTS request..." + request.type());
            Event event = (Event) request.data();
            try {
                ParticipantDTO[] found = server.getParticipants(event);
                return new Response.Builder().type(ResponseType.GET_PARTICIPANTS).data(found).build();
            } catch (ServiceException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.ADD_SWIMMER) {
            System.out.println("ADD_SWIMMER request..." + request.type());
            Swimmer swimmer = (Swimmer) request.data();
            try {
                server.addSwimmer(swimmer);
                return new Response.Builder().type(ResponseType.ADD_SWIMMER).build();
            } catch (ServiceException ex) {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }

        }

        if(request.type() == RequestType.FIND_LAST_SWIMMER) {
            System.out.println("FIND_LAST_SWIMMER request..." + request.type());
            try {
                Swimmer s = server.findLastSwimmer();
                return new Response.Builder().type(ResponseType.FIND_LAST_SWIMMER).data(s).build();
            } catch(ServiceException ex) {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }

        if(request.type() == RequestType.ADD_PARTICIPATION) {
            System.out.println("ADD_PARTICIPATION request..." + request.type());
            Participation participation = (Participation) request.data();
            try {
                server.addParticipation(participation);
                return new Response.Builder().type(ResponseType.ADD_PARTICIPATION).build();
            } catch (ServiceException ex) {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }
        if(request.type() == RequestType.LOGOUT){
            System.out.println("LOGOUT request: "+ request.type());
            User user = (User)request.data();
            try{
                server.logout(user, this);
                connected = false;
                return new Response.Builder().type(ResponseType.OK).data(null).build();
            }catch (ServiceException ex){
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }
        if(request.type() == RequestType.UPDATE) {
            System.out.println("UPDATE request: " + request.type());
            try {
                server.updateEvent();
                return new Response.Builder().type(ResponseType.OK).build();
            } catch (ServiceException ex) {
                return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void updateEvents() {
        Response resp = new Response.Builder().type(ResponseType.UPDATE).build();
        try {
            sendResponse(resp);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}

