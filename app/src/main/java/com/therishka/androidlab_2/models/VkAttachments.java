package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev
 */

public class VkAttachments {
    private String type;
    private VkPhoto photo;

    public VkAttachments() {
    }

    public VkAttachments(String type, VkPhoto photo) {
        this.type = type;
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VkPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(VkPhoto photo) {
        this.photo = photo;
    }
}
