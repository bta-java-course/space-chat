package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.Planet;

public class PlanetChannel extends Channel {
    private   Planet planet;

    public PlanetChannel(Planet planet) {
        super();
        this.planet = planet;
    }
}
