package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev
 */

public class VkLikes {

    private int count;
    private int user_likes;
    private int can_like;
    private int can_publish;

    public VkLikes() {
    }

    public VkLikes(int count, int user_likes, int can_like, int can_publish) {
        this.count = count;
        this.user_likes = user_likes;
        this.can_like = can_like;
        this.can_publish = can_publish;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isUser_likes() {
        return user_likes == 1;
    }

    public void setUser_likes(int user_likes) {
        this.user_likes = user_likes;
    }

    public boolean isCan_like() {
        return can_like == 1;
    }

    public void setCan_like(int can_like) {
        this.can_like = can_like;
    }

    public boolean isCan_publish() {
        return can_publish == 1;
    }

    public void setCan_publish(int can_publish) {
        this.can_publish = can_publish;
    }
}
