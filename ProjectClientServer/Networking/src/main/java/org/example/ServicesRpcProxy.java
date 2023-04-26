package org.example;

import org.example.model.*;
import org.example.service.IObserver;
import org.example.service.IServices;
import org.example.service.ServiceException;
import org.example.utils.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }
    @Override
    public User login(User user, IObserver client) throws ServiceException{
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new ServiceException(err);
        }
        this.client = client;
        User found = (User) response.data();
        return found;
    }
    @Override
    public EventDTO[] getAllEvents() throws ServiceException{
        Request req = new Request.Builder().type(RequestType.GET_EVENTS).data(null).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
        EventDTO[] foundE = (EventDTO[])resp.data();
        System.out.println("PROXY: Found events" + Arrays.toString(foundE));
        return foundE;
    }
    @Override
    public ParticipantDTO[] getParticipants(Event event) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.GET_PARTICIPANTS).data(event).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
        ParticipantDTO[] foundE = (ParticipantDTO [])resp.data();
        System.out.println("PROXY: Found participants" + Arrays.toString(foundE));
        return foundE;
    }
    @Override
    public void addParticipation(Participation participation) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.ADD_PARTICIPATION).data(participation).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
    }
    @Override
    public void addSwimmer(Swimmer swimmer) throws ServiceException {
        Request req = new Request.Builder().type(RequestType.ADD_SWIMMER).data(swimmer).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
    }
    @Override
    public Swimmer findLastSwimmer() throws ServiceException{
        Request req = new Request.Builder().type(RequestType.FIND_LAST_SWIMMER).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
        Swimmer s = (Swimmer)resp.data();
        System.out.println("PROXY: Found last swimmer" + s);
        return s;
    }
    @Override
    public void logout(User user, IObserver client) throws ServiceException{
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response resp = readResponse();
        closeConnection();
        if(resp.type() == ResponseType.ERROR){
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public void updateEvent() throws ServiceException {
        Request req = new Request.Builder().type(RequestType.UPDATE).build();
        sendRequest(req);
        Response resp = readResponse();
        if(resp.type() == ResponseType.ERROR) {
            String err = resp.data().toString();
            throw new ServiceException(err);
        }
    }
    private void handleUpdate(Response response) throws ServiceException {
        if (response.type()== ResponseType.UPDATE){
            try {
                client.updateEvents();
            } catch (ServiceException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void sendRequest(Request req) throws ServiceException{
        try{
            output.writeObject(req);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object: "+e);
        }
    }

    private Response readResponse(){
        Response response = null;
        try{
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            System.out.println("PROXY: Error on closeConnection: "+ e.getMessage());
            e.printStackTrace();
        }

    }
    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATE;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("PROXY: Response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ServiceException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}



