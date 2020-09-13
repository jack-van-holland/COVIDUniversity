package com.example.coviduniversity;

public class User {
    private String major;
    private String year;
    private String name;
    private String profilePicStorageName;
    private String id;

    public User(){
        major = "";
        year = "";
        name = "";
        profilePicStorageName = "";
        id = "";
    }
    public User(String major, String year, String name, String profilePicStorageName, String id) {
        this.major = major;
        this.year = year;
        this.name = name;
        this.profilePicStorageName = profilePicStorageName;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMajor() {
        return major;
    }

    public String getProfilePicStorageName() {
        return profilePicStorageName;
    }

    public String getYear() {
        return year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setProfilePicStorageName(String profilePicStorageName) {
        this.profilePicStorageName = profilePicStorageName;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
