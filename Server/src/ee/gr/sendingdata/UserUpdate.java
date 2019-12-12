package ee.gr.sendingdata;

import ee.gr.Planet;

import java.io.Serializable;

public class UserUpdate implements Serializable {

    private String newName;
    private String newPassword;
    private Planet newPlanet;

    public UserUpdate(String newName, String newPassword, Planet newPlanet) {
        this.newName = newName;
        this.newPassword = newPassword;
        this.newPlanet = newPlanet;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Planet getNewPlanet() {
        return newPlanet;
    }

    public void setNewPlanet(Planet newPlanet) {
        this.newPlanet = newPlanet;
    }

}
