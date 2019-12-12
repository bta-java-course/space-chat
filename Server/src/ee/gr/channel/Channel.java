package ee.gr.channel;

import ee.gr.Planet;
import ee.gr.sendingdata.Message;
import ee.gr.sendingdata.MessageHistory;
import ee.gr.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Channel implements Serializable {

    private String channelName;
    private Planet planet;
    private boolean isActive;
    private volatile List<User> usersList;
    private boolean userListIsChanged;
    private MessageHistory messageHistory;
    private Thread channelsThread;

    public Channel(String channelName) {
        this.channelName = channelName;
        this.planet = Planet.OTHER;
        this.isActive = true;
        this.usersList = new ArrayList<>();
        this.userListIsChanged = true;
        this.messageHistory = new MessageHistory();
    }

    public Channel(Planet planet) {
        this.channelName = planet.name();
        this.planet = planet;
        this.isActive = true;
        this.usersList = new ArrayList<>();
        this.userListIsChanged = true;
        this.messageHistory = new MessageHistory();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public MessageHistory getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(MessageHistory messageHistory) {
        this.messageHistory = messageHistory;
    }

    public Thread getChannelsThread() {
        return channelsThread;
    }

    public void setChannelsThread(Thread channelsThread) {
        this.channelsThread = channelsThread;
    }

    public boolean isUserListIsChanged() {
        return userListIsChanged;
    }

    public void setUserListIsChanged(boolean userListIsChanged) {
        this.userListIsChanged = userListIsChanged;
    }

    public User getUserByName(String name) {
        return usersList.stream().filter(user -> user.getName().equals(name)).findAny().get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;
        Channel channel = (Channel) o;
        return getChannelName().equals(channel.getChannelName()) &&
                getPlanet() == channel.getPlanet();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannelName(), getPlanet());
    }

}
