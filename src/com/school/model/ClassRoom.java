package com.school.model;

public class ClassRoom {
    private int id;
    private String name;

    public ClassRoom() {}

    public ClassRoom(String name) {
        this.name = name;
    }

    public ClassRoom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}