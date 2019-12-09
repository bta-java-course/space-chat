package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.Message;
import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.Subscription;
import com.buseduc.javacourse.users.UserServer;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel implements Runnable{
    String name;
    MessageHistory messageHistory;
    List<Subscription> subscriptionList;


    public Channel(String name) {
        this.name = name;
        messageHistory = new MessageHistory();
        subscriptionList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageHistory getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(MessageHistory messageHistory) {
        this.messageHistory = messageHistory;
    }

    public List<Subscription> getUserServerList() {
        return subscriptionList;
    }

    public void publishMessage(Message message) {
        messageHistory.addMessage(message);
        subscriptionList.stream().forEach(sub -> sub.showNewMessage(message));
    }

    public void addSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }

    public void remove(Subscription subscription) {
        for (Subscription sub : subscriptionList) {
            if (sub.equals(subscription)) {
                subscriptionList.remove(subscription);
            }
        }
    }


    public void run() {
//        System.out.println("Channel " + this.name + " started");
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            messageHistory.addMessage(this.name);
//            System.out.println("Channel " + this.name + " is alive");
        }
    }

    public Message getLastMessage() {
        return messageHistory.getHistory().get(messageHistory.getHistory().size() - 1);
    }

    @Override
    public String toString() {
        return "\n\tChannel{" +
                "name='" + name + '\'' +
                "}";
    }
}
