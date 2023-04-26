package org.example.utils;

import org.example.ClientRpcWorker;
import org.example.service.IServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbsConcurrentServer {
    private IServices server;
    public RpcConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
        System.out.println("Swimmers- RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker=new ClientRpcWorker(server, client);
        //ChatClientRpcReflectionWorker worker=new ChatClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
