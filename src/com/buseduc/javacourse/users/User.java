package com.buseduc.javacourse.users;

import com.buseduc.javacourse.Planet;

import java.io.IOException;

public class User{

    private Planet planet;
    private String name;
    private int password;
    private boolean isActive;
    private TCPConnection tcpConnection;

    public User(Planet planet, String name, TCPConnection tcpConnection) throws IOException {
        this.planet = planet;
        this.name = name;
        this.tcpConnection = tcpConnection;
        this.isActive = true;
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

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
