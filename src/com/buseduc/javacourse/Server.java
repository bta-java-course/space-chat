package com.buseduc.javacourse;

import com.buseduc.javacourse.users.TCPConnection;
import com.buseduc.javacourse.users.TCPConnectionListener;
import com.buseduc.javacourse.users.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements TCPConnectionListener {

    private final List<TCPConnection> connectionList = new ArrayList<>();
    private final List<User> usersList = new ArrayList<>();

    public Server() {
        System.out.println("Starting server");
        try(ServerSocket serverSocket = new ServerSocket(8111)) {
            while (true) {
                try {
                    TCPConnection connection = new TCPConnection(this, serverSocket.accept());
                    connection.getSocket().getInputStream();
                } catch (IOException e) {
                    System.out.println("TCPConnection excptn: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connectionList.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);

    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connectionList.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection excptn: " + e);
    }

    private void sendToAllConnections(String msg) {
        System.out.println(msg);
        connectionList.forEach(c -> c.sendMessage(msg));
    }

}


