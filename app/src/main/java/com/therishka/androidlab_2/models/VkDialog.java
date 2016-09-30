package com.therishka.androidlab_2.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rishad Mustafaev.
 */

public class VkDialog {

    private String username;

    private String title;

    @SerializedName("photo_50")
    private String photo;

    @SerializedName("body")
    private String message;

    private int is_read;

    public VkDialog() {
    }

    public VkDialog(String username, String title, String photo, String message, int is_read) {
        this.username = username;
        this.title = title;
        this.photo = photo;
        this.message = message;
        this.is_read = is_read;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean is_read() {
        return is_read == 1;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
