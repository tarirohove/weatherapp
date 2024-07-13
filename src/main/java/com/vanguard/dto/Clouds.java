package com.vanguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Clouds {
    @JsonProperty("all")
    private int all_all;

    public int getAll_all() {
        return all_all;
    }

    public void setAll_all(int all_all) {
        this.all_all = all_all;
    }
}

