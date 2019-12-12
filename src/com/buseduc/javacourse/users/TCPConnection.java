package com.buseduc.javacourse.users;

import java.io.*;
import java.net.Socket;

public class TCPConnection {

    private final Socket socket;
    private Thread thread = null;
    private String name;
    private final TCPConnectionListener eventListener;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public TCPConnection(TCPConnectionListener eventListener, String ipAdr, int port) throws IOException {
        this(eventListener, new Socket(ipAdr, port));
    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.eventListener = tcpConnectionListener;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.thread = new Thread(() -> {
            try {
                eventListener.onConnectionReady(TCPConnection.this);
                while(!thread.isInterrupted()) {
                    String message = reader.readLine();
                    eventListener.onReceiveString(TCPConnection.this, message);
                }
            } catch (IOException e) {
                eventListener.onException(TCPConnection.this, e);
            } finally {
                eventListener.onDisconnect(TCPConnection.this);
            }
        });
        this.thread.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void sendMessage(String message) {
        try {
            writer.write(this.name + ":" + "\n" +message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        this.thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }

}
