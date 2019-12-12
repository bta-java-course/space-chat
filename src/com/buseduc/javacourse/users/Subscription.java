package com.buseduc.javacourse.users;

import com.buseduc.javacourse.channels.Channel;
import com.buseduc.javacourse.messages.Message;

import java.util.Objects;

public class Subscription extends Thread{

    private Channel channel;
    private UserServer userServer;

    public Subscription(Channel channel, UserServer userServer) {
        this.channel = channel;
        this.userServer = userServer;
        channel.addSubscription(this);
    }
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public UserServer getUserServer() {
        return userServer;
    }

    public void setUserServer(UserServer userServer) {
        this.userServer = userServer;
    }

    public synchronized void run() {
        while(true) {
            try {
                Thread.sleep(200);
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
//            messageHistory.addMessage(this.name);
//            System.out.println("Channel " + this.name + " is alive");
            }
        }
    }

    public synchronized void publishMessage(Message message) {
        try {
            this.notify();
            this.channel.publishMessage(message);
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        this.channel.remove(this);
        this.userServer.setSubscription(null);
    }

    public void showNewMessage(Message message) {
        userServer.sendMessageToClient(message.toString());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return channel.equals(that.channel) &&
                userServer.equals(that.userServer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, userServer);
    }



}
