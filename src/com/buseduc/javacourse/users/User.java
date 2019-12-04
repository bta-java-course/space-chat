package com.buseduc.javacourse.users;

import com.buseduc.javacourse.Planet;
import com.buseduc.javacourse.SpaceChat;
import com.buseduc.javacourse.channels.Channel;

import java.util.Map;
import java.util.Scanner;

public class User implements Runnable {
    private Planet planet;
    private String name;
    private boolean isActive;

    private static final User bot = new User(null, "SpaceChat bot");

    public User(Planet planet, String name) {
        this.planet = planet;
        this.name = name;
        this.isActive = true;
    }

    public static User getBot() {
        return bot;
    }

    public void run() {
        System.out.println("User " + name + " started");
        String command = "0";
        while(!"q".equals(command)) {
            command = getCommand();
            if(command.startsWith("show ")) {
                String planetName = command.substring(5);
                Planet p = Planet.valueOf(planetName);
                SpaceChat chat = SpaceChat.getInstance();
                Map<Planet, Channel> channels = chat.getPlanetChannels();
                Channel channel = channels.get(p);
                UserChannel userChannel = new UserChannel(channel);
                userChannel.setDaemon(true);
                userChannel.start();
                System.out.println(channel.getMessageHistory());
            }
        }
        System.out.println("User exit");

    }



    private String getCommand() {
        System.out.println("Enter command: ");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNext()) {
            return sc.nextLine();
        }
        return "q";
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return name +
                " from " + planet;
    }
}
