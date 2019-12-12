package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel {
    private String name;
    private List<User> userList;
    private MessageHistory messageHistory;

    public Channel(String name) {
        this.name = name;
        this.userList = new ArrayList<>();
        this.messageHistory = new MessageHistory();

    }

}
