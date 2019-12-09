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

    public UserServer getUserServer() {
        return userServer;
    }

    public void setUserServer(UserServer userServer) {
        this.userServer = userServer;
    }

    public void run() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(Message message) {
        this.notify();
        this.channel.publishMessage(message);
    }

    public void remove() {
        this.channel.remove(this);
        this.userServer.setSubscription(null);
    }

    public void showNewMessage(Message message) {
        System.out.println(message);
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
