package com.buseduc.javacourse;

import com.buseduc.javacourse.channels.Channel;
import com.buseduc.javacourse.channels.MainChannel;
import com.buseduc.javacourse.channels.PlanetChannel;
import com.buseduc.javacourse.users.UserRegistry;
import com.buseduc.javacourse.users.UserServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SpaceChat {
    private List<UserServer> userServerList;
    private MainChannel mainChannel;
    private Map<Planet, Channel> planetChannels;
    private List<Channel> customChannelList;
    private Socket socket;
    public static final String SERVER_IP = "10.0.0.33";
    public static final String SUBSCRIPTION_OK = "/subscribed";
    public static final int PORT = 5000;
//    private static final static String SERVER_IP = "10.0.0.39";
    private static SpaceChat chatInstance;
    public static void main(String[] args) {
        SpaceChat chat = SpaceChat.getInstance();
        System.out.println(chat);
    }

    public SpaceChat() {
        UserRegistry userRegistry = UserRegistry.getInstance();
        mainChannel = new MainChannel("main");
//        new Thread(mainChannel).start();
        planetChannels = new HashMap<>();
        for(Planet p : Planet.values()) {
            Channel planetChannel = new PlanetChannel(p);
            planetChannels.put(p, planetChannel);
//            new Thread(planetChannel).start();
        }
        customChannelList = new ArrayList<>();
        userServerList = new ArrayList<>();
        try {
        ServerSocket server = new ServerSocket(PORT); // создаем сокет
        System.out.println("Сервер запущен. Ждем клиента ...");
        while (true) { // сервер не останавливается никогда
            try {
                socket = server.accept(); // новый клиент - новое подключение
                System.out.println("Новый клиент соединился");
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            UserServer next = new UserServer(socket);
            System.out.println("NEW USER");
            new Thread(next).start(); // новый поток для клиента
        }
    } catch (Exception i) {
        System.out.println("Ошибка сервера: " + i); // выведем ошибку
    }

    }

    public List<UserServer> getUserServerList() {
        return userServerList;
    }

    public void setUserServerList(List<UserServer> userServerList) {
        this.userServerList = userServerList;
    }

    public MainChannel getMainChannel() {
        return mainChannel;
    }

    public void setMainChannel(MainChannel mainChannel) {
        this.mainChannel = mainChannel;
    }

    public Map<Planet, Channel> getPlanetChannels() {
        return planetChannels;
    }

    public void setPlanetChannels(Map<Planet, Channel> planetChannels) {
        this.planetChannels = planetChannels;
    }

    public List<Channel> getCustomChannelList() {
        return customChannelList;
    }

    public void setCustomChannelList(List<Channel> customChannelList) {
        this.customChannelList = customChannelList;
    }

    @Override
    public String toString() {
        return "SpaceChat{" +
                "userList=" + userServerList +
                ", \nmainChannel=" + mainChannel +
                ", \nplanetChannels=" + planetChannels +
                ", \ncustomChannelList=" + customChannelList +
                '}';
    }

    public static SpaceChat getInstance() {
        if(chatInstance == null) {
            chatInstance = new SpaceChat();
        }
        return chatInstance;
    }

}
