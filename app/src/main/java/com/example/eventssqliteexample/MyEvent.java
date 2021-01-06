package com.example.eventssqliteexample;

public class MyEvent {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name, description, location;
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public MyEvent(){

    }
    public MyEvent(int id, String name, String description, String location, String date) {
        this.name = name;
        this.id=id;
        this.description = description;
        this.location = location;
        this.date = date;
    }
    public MyEvent( String name, String description, String location, String date) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
    }
}
