package ee.gr.sendingdata;

import ee.gr.channel.Channel;
import ee.gr.user.User;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable, Cloneable {

    public enum Type{FROM_USER, FROM_USER_PRIVATE, SYSTEM_CHANNEL, SYSTEM_ALL, SYSTEM_FOR_USER}

    private String message;
    private Channel channel;
    private LocalDate date;
    private User user;
    private User receiver;
    private Type type;

    public Message() {
    }

    //Msg from user
    public Message(String message, User user, Channel channel, Type type) {
        this.message = message;
        this.date = LocalDate.now();
        this.user = user;
        this.channel = channel;
        this.type = type;
    }

    //Private msg from user to user
    public Message(String message, User sender, User receiver, Channel channel, Type type) {
        this.message = message;
        this.date = LocalDate.now();
        this.user = sender;
        this.receiver = receiver;
        this.channel = channel;
        this.type = type;
    }

    //System msg for all channels
    public Message(String message, Type type) {
        this.user = new User("Server");
        this.message = message;
        this.date = LocalDate.now();
        this.type = type;
    }

    //System msg for current channel
    public Message(String message, Channel channel, Type type) {
        this.user = new User("Server");
        this.message = message;
        this.date = LocalDate.now();
        this.channel = channel;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Message clone() {
        Message msg = new Message();
        msg.message = this.message;
        msg.date = this.date;
        msg.user = this.user;
        msg.receiver = this.receiver;
        msg.channel = this.channel;
        msg.type = this.type;
        return msg;
    }

}
