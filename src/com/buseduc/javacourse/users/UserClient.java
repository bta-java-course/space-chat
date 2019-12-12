package com.buseduc.javacourse.users;

import com.buseduc.javacourse.SpaceChat;

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

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public static void main(String[] args) {
        Runnable clientR = () -> {
            String serverAddress = args.length > 0 ? args[0] : SpaceChat.SERVER_IP;
            UserClient client = null;
            try {
                client = new UserClient(serverAddress, SpaceChat.PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.start();
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
            serverCommand = in.readUTF();
            System.out.println(serverCommand);
            while (!command.toLowerCase().equals("s")) { // s -- завершение ввода
//
                command = getCommand();
                out.writeUTF(command);     // запишем ее в поток
                serverCommand = in.readUTF();
                if (SpaceChat.SUBSCRIPTION_OK.equals(serverCommand)) {
                    SubscriptionClient subscriptionClient = new SubscriptionClient(this);
                    subscriptionClient.start();
                    System.out.println(serverCommand);
                    while(true) {
                        command = getCommand();
                        out.writeUTF(command);
                    }
                }
        }
        out.close();
        socket.close();
        } catch(IOException i) { /* МОЛЧАЛИВАЯ ОШИБКА*/ }
    }

    public String getCommand() {
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {

            return sc.nextLine();
        }
        return "q";
    }



}