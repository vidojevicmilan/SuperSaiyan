package com.nocomment.supersaiyan;

/**
 * Created by milanvidojevic on 04-Oct-16.
 */

public class User {

    private String username;
    private String phone;
    private String xp;
    private String image;

    public User(){

    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public User(String username, String phone, String xp, String image) {
        this.username = username;
        this.phone = phone;
        this.xp = xp;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getXP() {
        return xp;
    }

    public void setUsername(String username) {
        username = username;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public void setXP(String XP) {
        this.xp = XP;
    }
}
