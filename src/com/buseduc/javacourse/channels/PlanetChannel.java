package com.buseduc.javacourse.channels;

import com.buseduc.javacourse.Planet;

public class PlanetChannel extends Channel {
    Planet planet;

    public PlanetChannel(Planet planet) {
        super(planet.name());
        this.planet = planet;
    }

}
