package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev
 */

public class VkFriend {

    private String name;
    private String surname;
    private String smallPhotoUrl;
    private boolean isOnline;

    public VkFriend() {
    }

    public VkFriend(String name, String surname, String smallPhotoUrl, boolean isOnline) {
        this.name = name;
        this.surname = surname;
        this.smallPhotoUrl = smallPhotoUrl;
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
