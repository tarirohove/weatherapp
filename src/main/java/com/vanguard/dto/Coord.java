package com.vanguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coord {

    @JsonProperty("lon")
    private double lon;

    @JsonProperty("lat")
    private double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}

