package org.example.protobuf;

import org.example.model.*;
import org.example.service.IObserver;
import org.example.service.IServices;
import org.example.service.ServiceException;

import java.io.*;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private BlockingQueue<SwimmersProtobufs.Response> qresponses;
    private volatile boolean finished;
    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<SwimmersProtobufs.Response>();
    }
    @Override
    public User login(User user, IObserver client) throws ServiceException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(user));
        SwimmersProtobufs.Response response=readResponse();
        if (response.getType()==SwimmersProtobufs.Response.Type.Login){
            this.client=client;
            return user;
        }
        if (response.getType()==SwimmersProtobufs.Response.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServiceException(errorText);
        }
        return null;
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

    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            output.flush();
            input=connection.getInputStream();
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    SwimmersProtobufs.Response response=SwimmersProtobufs.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(SwimmersProtobufs.Response.Type type){
        switch (type){
            case Update:  return true;
        }
        return false;
    }


    private void handleUpdate(SwimmersProtobufs.Response updateResponse){
        switch (updateResponse.getType()){
            case Update:{
                System.out.println("Update");
                try {
                    client.updateEvents();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void sendRequest(SwimmersProtobufs.Request request)throws ServiceException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private SwimmersProtobufs.Response readResponse() throws ServiceException{
        SwimmersProtobufs.Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public ParticipantDTO[] getParticipants(Event event) throws ServiceException {
        sendRequest(ProtoUtils.createGetParticipantsRequest(event));
        SwimmersProtobufs.Response response = readResponse();
        if(response.getType() == SwimmersProtobufs.Response.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
        ParticipantDTO[] participantDTOS = ProtoUtils.getParticipants(response);
        return participantDTOS;
    }

    @Override
    public void addParticipation(Participation participation) throws ServiceException {
        System.out.println("ADD PARTICIPATION send request" + participation);
        sendRequest(ProtoUtils.createAddParticipationRequest2(participation));
        SwimmersProtobufs.Response response = readResponse();
        if(response.getType() == SwimmersProtobufs.Response.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
    }

    @Override
    public void addSwimmer(Swimmer swimmer) throws ServiceException {
        sendRequest(ProtoUtils.createAddSwimmerRequest(swimmer));
        SwimmersProtobufs.Response response = readResponse();
        if(response.getType() == SwimmersProtobufs.Response.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
    }

    @Override
    public Swimmer findLastSwimmer() throws ServiceException {
         sendRequest(ProtoUtils.createFindLastSwimmerRequest());
         SwimmersProtobufs.Response response = readResponse();
         if (response.getType() == SwimmersProtobufs.Response.Type.Error){
             String errorText = ProtoUtils.getError(response);
             throw new ServiceException(errorText);
         }
         Swimmer last = ProtoUtils.getLastSwimmer(response);
         return last;
    }

    @Override
    public void logout(User user, IObserver client) throws ServiceException {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        SwimmersProtobufs.Response response = readResponse();
        closeConnection();
        if (response.getType() == SwimmersProtobufs.Response.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
    }

    @Override
    public void updateEvent() throws ServiceException {
        sendRequest(ProtoUtils.createUpdateRequest());
        SwimmersProtobufs.Response response = readResponse();
        if(response.getType() == SwimmersProtobufs.Response.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
    }

    @Override
    public EventDTO[] getAllEvents() throws ServiceException {
        sendRequest(ProtoUtils.createGetEventsRequest());
        SwimmersProtobufs.Response response = readResponse();
        if(response.getType() == SwimmersProtobufs.Response.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
        EventDTO[] events = ProtoUtils.getAllEvents(response);
        return events;
    }

}
