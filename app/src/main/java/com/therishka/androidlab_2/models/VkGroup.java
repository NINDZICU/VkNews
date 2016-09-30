package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev
 */

public class VkGroup {

    private long id;
    private String name;
    private String photo_50;
    private String photo_100;
    private String photo_200;

    public VkGroup() {
    }

    public VkGroup(long id, String name, String photo_50, String photo_100, String photo_200) {
        this.id = id;
        this.name = name;
        this.photo_50 = photo_50;
        this.photo_100 = photo_100;
        this.photo_200 = photo_200;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public void setPhoto_50(String photo_50) {
        this.photo_50 = photo_50;
    }

    public String getPhoto_100() {
        return photo_100;
    }

    public void setPhoto_100(String photo_100) {
        this.photo_100 = photo_100;
    }

    public String getPhoto_200() {
        return photo_200;
    }

    public void setPhoto_200(String photo_200) {
        this.photo_200 = photo_200;
    }
}
