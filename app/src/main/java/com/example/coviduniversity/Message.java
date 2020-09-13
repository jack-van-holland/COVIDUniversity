package com.example.coviduniversity;

import java.security.InvalidParameterException;

public class Message implements Comparable {
    private String text;
    private String name;
    private String id;
    private long time;

    public Message() {
        text = "";
        name = "";
        id = "";
        time = 0;
    }

    public Message(String text, String name, String id, long time) {
        this.text = text;
        this.name = name;
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Message)) {
            throw new InvalidParameterException();
        } else {
            if (((Message) o).getTime() > this.time) {
                return -1;
            } else if (((Message) o).getTime() == this.time){
                return 0;
            } else {
                return 1;
            }
        }
    }
}
