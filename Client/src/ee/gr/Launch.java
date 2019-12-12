package ee.gr;

import ee.gr.GUI.ClientWindow;
import ee.gr.channel.Channel;
import ee.gr.user.User;
import javafx.application.Application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Launch {

    public static final String PROPERTY = System.getProperty("user.dir");
    public static final String PATH = "/Client/src/ee/gr/";
    private static final Launch instance = new Launch();
    private List<Channel> channelList;
    private Channel currentChannel;
    private User activeUser;
    private Socket socket;
    private volatile ObjectInputStream inputStream;
    private volatile ObjectOutputStream outputStream;
    private DataListener listener;

    public static void main(String[] args) {
        Application.launch(ClientWindow.class, args);
    }

    public static Launch getInstance() {
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setCurrentChannel(Planet planet) {
        Channel channel = channelList.stream().filter(ch -> ch.getPlanet().equals(planet)).findAny().get();
        this.currentChannel = channel;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Channel getOwnChannel(Channel channel) {
        return channelList.stream()
                .filter(channel1 -> channel1.getPlanet().equals(channel.getPlanet()))
                .findAny().get();
    }

    public void removeChannelByName(String name) {
        Channel channel = channelList.stream().filter(c -> c.getChannelName().equals(name)).findAny().get();
        channelList.remove(channel);
    }

    public void startListener() {
        if (this.listener != null) this.listener.interrupt();
        this.listener = new DataListener();
    }

    public void stopListener() {
        this.listener.interrupt();
    }

}
