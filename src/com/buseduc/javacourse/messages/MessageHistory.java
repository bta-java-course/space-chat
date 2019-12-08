package com.buseduc.javacourse.messages;

import com.buseduc.javacourse.users.UserServer;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory {
    private List<Message> history;

    public MessageHistory() {
        this.history = new ArrayList<>();
    }

    public List<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = history;
    }

    public void addMessage(String messageStr) {
        Message message = new Message(messageStr, UserServer.getBot());
        history.add(message);
    }

    @Override
    public String toString() {

        final String[] ret = {""};
        history.stream().forEach(msg -> ret[0] += msg.toString() + "\n");
        return ret[0];
    }
}
