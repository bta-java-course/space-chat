package com.buseduc.javacourse.users;

import com.buseduc.javacourse.Planet;
import com.buseduc.javacourse.SpaceChat;
import com.buseduc.javacourse.channels.Channel;

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


    private static final UserServer bot = new UserServer(null, "SpaceChat bot");

    public UserServer(Planet planet, String name) {
        this.planet = planet;
        this.name = name;
        this.isActive = true;
    }

    public UserServer(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
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
            command = in.readUTF();
            boolean planetReady = false;
            while (!planetReady) {
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
            System.out.println(this);
            while (command != "/exit") {
                command = in.readUTF();
                if (command.startsWith("/join ")) {
                    String planetName = command.substring(5);
                    Planet p = Planet.valueOf(planetName);
                    SpaceChat chat = SpaceChat.getInstance();
                    Map<Planet, Channel> channels = chat.getPlanetChannels();
                    Channel channel = channels.get(p);
                    Subscription subscription = new Subscription(channel);
                    subscription.start();
                }
            }
            System.out.println("User exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readClientInput() {
        String line = "";
        try {
            while (!line.equals("s")) { // пока клиент не ввел "s"
                line = in.readUTF();
                System.out.println("Сервер отвечает: " + line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка сервера: " + e); // выведем ошибку
        }
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
