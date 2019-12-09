package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.Message;
import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.UserServer;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel implements Runnable {
    String name;
    MessageHistory messageHistory;
    List<UserServer> userServerList;

    public Channel(String name) {
        this.name = name;
        messageHistory = new MessageHistory();
        userServerList = new ArrayList<>();
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

    public List<UserServer> getUserServerList() {
        return userServerList;
    }

    public void setUserServerList(List<UserServer> userServerList) {
        this.userServerList = userServerList;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageHistory.addMessage(this.name);
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
