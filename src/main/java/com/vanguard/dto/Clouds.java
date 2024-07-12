package com.vanguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Clouds {

    @JsonProperty("all")
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}

