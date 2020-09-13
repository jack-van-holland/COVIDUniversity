package com.example.coviduniversity;

public class Brody_table_list {
    private String name;
    private String status;
    private int number;

    public Brody_table_list(String name, String status) {
        this.name = name;
        this.status = status;
        this.number = 0;

    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public void leave() {
        number -= 1;
    }
    public void add() {
        number += 1;
    }
}
