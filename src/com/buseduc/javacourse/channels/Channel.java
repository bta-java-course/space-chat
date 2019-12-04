package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.Message;
import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel {
    private String name;
    private MessageHistory messageHistory;
    private List<User> userList;

    public Channel(String name) {
        this.name = name;
        messageHistory = new MessageHistory();
        userList = new ArrayList<>();
    }

    public Channel() {
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
}
