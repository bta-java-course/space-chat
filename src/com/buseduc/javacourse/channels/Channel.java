package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.messages.Message;
import com.buseduc.javacourse.messages.MessageHistory;
import com.buseduc.javacourse.users.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Channel {
    String name;
    MessageHistory messageHistory;
    List<User> userList;

    public Channel(String name) {
        this.name = name;
        messageHistory = new MessageHistory();
        userList = new ArrayList<>();
    }
}
