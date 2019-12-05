package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.Message;
import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel implements Runnable{
    String name;
    MessageHistory messageHistory;
    List<User> userList;

    public Channel(String name) {
        this.name = name;
        messageHistory = new MessageHistory();
        userList = new ArrayList<>();
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void run() {
//        System.out.println("Channel " + this.name + " started");
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageHistory.addMessage(this.name);
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
