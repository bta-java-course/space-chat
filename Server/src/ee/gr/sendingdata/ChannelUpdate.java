package ee.gr.sendingdata;

import ee.gr.channel.Channel;
import ee.gr.user.User;

import java.io.Serializable;
import java.util.List;

public class ChannelUpdate implements Serializable {

    public enum Action{INIT_CHANNELS, CHANNEL_ADD, CHANNEL_REMOVE, USER_LOG_OUT, USER_LOG_IN}

    private User user;
    private Channel channel;
    private List<Channel> channelList;
    private List<User> usersList;
    private Action action;

    //For remove or add user
    public ChannelUpdate(User user, Channel channel, Action action) {
        this.user = user;
        this.channel = channel;
        this.action = action;
    }

    //Init channels List From Server to client
    public ChannelUpdate(List<Channel> channels, Action action) {
        this.channelList = channels;
        this.action = action;
    }

    //Init channels List From Server to client
    public ChannelUpdate(Action action, List<User> usersList) {
        this.usersList = usersList;
        this.action = action;
    }

    //Update channels count in channelsList
    public ChannelUpdate(Channel channel, Action action) {
        this.channel = channel;
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

}
