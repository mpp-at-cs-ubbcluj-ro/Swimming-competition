package org.example;
import org.example.repository.*;
import org.example.server.ServicesImpl;
import org.example.service.IServices;
import org.example.utils.AbstractServer;
import org.example.utils.RpcConcurrentServer;
import org.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;
public class StartObjectServer {
    private static int defaultPort = 55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        IUserRepository userRepo = new UserDbRepository(serverProps);
        IEventRepository eventRepo = new EventDbRepository(serverProps);
        ISwimmerRepository swimmerRepo = new SwimmerDbRepository(serverProps);
        IParticipationRepository participationRepo = new ParticipationDbRepository(serverProps, eventRepo, swimmerRepo);
        IServices serverImpl = new ServicesImpl(userRepo, eventRepo, swimmerRepo, participationRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("app.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+ serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
        finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.out.println("Error stopping server: " + e.getMessage());
            }
        }
    }
}