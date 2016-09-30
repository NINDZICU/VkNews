package com.therishka.androidlab_2.models;

import java.util.List;

/**
 * @author Rishad Mustafaev
 */

public class VkNewsItem {

    // filled with gson
    private long source_id;
    private long date;
    private String text;
    private List<VkAttachments> attachments;
    private VkLikes likes;

    // set manually
    private VkGroup publisher;

    public VkGroup getPublisher() {
        return publisher;
    }

    public void setPublisher(VkGroup publisher) {
        this.publisher = publisher;
    }

    public long getSource_id() {
        return source_id;
    }

    public void setSource_id(long source_id) {
        this.source_id = source_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<VkAttachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<VkAttachments> attachments) {
        this.attachments = attachments;
    }

    public VkLikes getLikes() {
        return likes;
    }

    public void setLikes(VkLikes likes) {
        this.likes = likes;
    }
}
