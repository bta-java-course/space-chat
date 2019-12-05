package com.buseduc.javacourse.users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class UserClient {
    private DataOutputStream out = null;
    private DataInputStream in;
    private Socket socket;

    public UserClient(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }

    public static void main(String[] args) {
        Runnable clientR = new Runnable() {
            @Override
            public void run() {
                String serverAddress = "10.0.0.39";
                UserClient client = null;
                try {
                    client = new UserClient(serverAddress, 5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.start();
            }
        };
        new Thread(clientR).start();
    }

    public void start() {
        try {
        String command = "";
        String serverCommand = in.readUTF();
        System.out.println(serverCommand);
        command = getCommand();
        out.writeUTF(command);
        serverCommand = in.readUTF();
            System.out.println(serverCommand);
            command = getCommand();
            out.writeUTF(command);

            while (!command.toLowerCase().equals("s")) { // s -- завершение ввода
                command = getCommand();
                out.writeUTF(command);     // запишем ее в поток
        }
        out.close();
        socket.close();
        } catch(IOException i) { /* МОЛЧАЛИВАЯ ОШИБКА*/ }
    }

    private String getCommand() {
        System.out.println("Enter command: ");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            return sc.nextLine();
        }
        return "q";
    }



}
