package com.buseduc.javacourse.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageHistory {

    private List<Message> messageList;

    public MessageHistory() {
        this.messageList = new ArrayList<>();
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

}
