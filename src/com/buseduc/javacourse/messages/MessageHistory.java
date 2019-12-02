package com.buseduc.javacourse.messages;

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

}
