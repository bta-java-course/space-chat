
package ee.gr.user;

import ee.gr.Planet;
import ee.gr.channel.Channel;
import ee.gr.sendingdata.MessageHistory;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, Cloneable {

    private volatile boolean isRegistered;
    private volatile boolean isLoggedOut;
    private volatile String name;
    private volatile int id;
    private volatile String password;
    private volatile Planet planet;
    private volatile MessageHistory messageHistory;
    private volatile Channel currentChannel;
    private transient  Connection connection;

    //For normal user
    public User(boolean isRegistered, String name, int id, String password, Planet planet, Channel currentChannel, Connection connection) {
        this.isRegistered = isRegistered;
        this.isLoggedOut = false;
        this.name = name;
        this.id = id;
        this.password = password;
        this.planet = planet;
        this.messageHistory = new MessageHistory();
        this.currentChannel = currentChannel;
        this.connection = connection;
    }

    //For server
    public User(boolean isRegistered, String name, String password, Planet planet) {
        this.isRegistered = isRegistered;
        this.isLoggedOut = true;
        this.id = id;
        this.name = name;
        this.password = password;
        this.planet = planet;
        this.messageHistory = new MessageHistory();
    }

    //For server
    public User(String name) {
        this.name = name;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public MessageHistory getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(MessageHistory messageHistory) {
        this.messageHistory = messageHistory;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel currentChannel) {
        this.currentChannel = currentChannel;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isLoggedOut() {
        return isLoggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        isLoggedOut = loggedOut;
    }

    @Override
    public User clone() {
        User clone = new User(this.isRegistered, this.name, this.password, this.planet);
        clone.setId(this.id);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isRegistered() == user.isRegistered() &&
                id == user.id &&
                getName().equals(user.getName()) &&
                getPlanet() == user.getPlanet();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isRegistered(), getName(), id, getPlanet());
    }

}
