package com.buseduc.javacourse.messages;

import com.buseduc.javacourse.users.User;

import java.time.LocalDateTime;

public class Message {
    private  String text;
    private  User author;
    private LocalDateTime created;

    public Message(String text, User author) {
        this.text = text;
        this.author = author;
        this.created = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
