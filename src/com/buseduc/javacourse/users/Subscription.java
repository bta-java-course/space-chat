package com.buseduc.javacourse.users;

import com.buseduc.javacourse.channels.Channel;

public class Subscription extends Thread {
    private Channel channel;

    public Subscription(Channel channel) {
        this.channel = channel;
    }

    public void run() {
        try {
            hearChannel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void hearChannel() throws InterruptedException {
        long messageCounter = channel.getMessageHistory().getHistory().size();
        while (true) {
            Thread.sleep(1000);
            if (messageCounter != channel.getMessageHistory().getHistory().size()) {
                System.out.println(channel.getLastMessage());
                messageCounter = channel.getMessageHistory().getHistory().size();
            }
        }
    }

}
