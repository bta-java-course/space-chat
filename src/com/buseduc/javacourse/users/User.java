package com.buseduc.javacourse.users;

import com.buseduc.javacourse.Planet;

public class User {
    private Planet planet;
    private String name;
    private boolean isActive;

    public User(Planet planet, String name) {
        this.planet = planet;
        this.name = name;
        this.isActive = true;
    }

    public Planet getPlanet(){
        return planet;
    }

    public void setPlanet(Planet planet){
       this.planet = planet;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public boolean isActive(){
       return isActive;
    }
    public void setActive(boolean active){
        isActive = active;
    }
}
