package com.buseduc.javacourse.messages;

import com.buseduc.javacourse.users.UserServer;

import java.time.LocalDateTime;

public class Message {
    String text;
    UserServer author;
    LocalDateTime created;

    public Message(String text, UserServer author) {
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

    public UserServer getAuthor() {
        return author;
    }

    public void setAuthor(UserServer author) {
        this.author = author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return  created +  "[" + author + "]: " + text + "\n";
    }
}
