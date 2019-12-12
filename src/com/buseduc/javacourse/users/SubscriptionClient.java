package com.buseduc.javacourse.users;

import com.buseduc.javacourse.SpaceChat;

import java.io.IOException;

public class SubscriptionClient extends Thread {
    private UserClient userClient;

    public SubscriptionClient(UserClient userClient) {
        this.userClient = userClient;
    }
    public synchronized void run() {
        String serverCommand = null;
        try {
            serverCommand = userClient.getIn().readUTF();
            System.out.println(serverCommand);
            while (!serverCommand.toLowerCase().equals("/leave")) { // s -- завершение ввода
                serverCommand = userClient.getIn().readUTF();
                System.out.println(serverCommand);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
