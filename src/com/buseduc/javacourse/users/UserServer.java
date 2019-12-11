package com.buseduc.javacourse.users;

import com.buseduc.javacourse.Planet;
import com.buseduc.javacourse.SpaceChat;
import com.buseduc.javacourse.channels.Channel;
import com.buseduc.javacourse.messages.Message;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class UserServer implements Runnable {
    private Planet planet;
    private String name;
    private boolean isActive;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Subscription subscription;


    private static final UserServer bot = new UserServer(null, "SpaceChat bot");

    public UserServer(Planet planet, String name) {
        this.planet = planet;
        this.name = name;
        this.isActive = true;
    }

    public UserServer(Socket socket) throws IOException {
        this.socket= socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public static UserServer getBot() {
        return bot;
    }

    public void run() {
        try {
            System.out.println("User " + name + " started");
            String command = "0";
            out.writeUTF("Input name");
            command = in.readUTF();
            this.name = command;
            out.writeUTF("Input your planet");
            boolean planetReady = false;
            while(!planetReady) {
                out.writeUTF("Input command or message");
                command = in.readUTF();
                Planet found;
                try {
                    found = Planet.valueOf(command.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Input your planet");
                    continue;
                }
                this.planet = found;
                planetReady = true;
            }
            UserRegistry registry = UserRegistry.getInstance();
            registry.addUser(this);
            System.out.println(this);

            while(!"/exit".equals(command)) {
                parseUserInput(command);

            }
            System.out.println("User exit");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendMessageToClient(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseUserInput(String command) throws IOException {
        command = in.readUTF();
        if (command.startsWith("/")) {
            if (command.startsWith("/join")) {
                String planetName = command.substring(6);
                Planet p = Planet.valueOf(planetName);
                SpaceChat chat = SpaceChat.getInstance();
                Map<Planet, Channel> channels = chat.getPlanetChannels();
                Channel channel = channels.get(p);
                Subscription subscription = new Subscription(channel, this);
                this.subscription = subscription;
                subscription.start();
            } else if ("/leave".equals(command)) {
                this.subscription.remove();
            }
            out.writeUTF("command done: " + command);

        } else if(this.subscription != null) {
            Message message = new Message(command, this);
            this.subscription.publishMessage(message);
            String channelName = subscription.getChannel().getName();

            System.out.println("CHANNEL " + channelName + ": " + message);
        }
        if(command.startsWith("/join ")) {
//                System.out.println(channel.getMessageHistory());
        }

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
