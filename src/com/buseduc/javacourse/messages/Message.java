package com.buseduc.javacourse.messages;

import com.buseduc.javacourse.users.User;

import java.time.LocalDate;

public class Message {
    private String text;
    private User user;
    private LocalDate created;

    public Message(String text, User user, LocalDate created) {
        this.text = text;
        this.user = user;
        this.created = LocalDate.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

}
