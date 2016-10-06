package com.nocomment.supersaiyan;

import android.graphics.Bitmap;

/**
 * Created by milanvidojevic on 06-Oct-16.
 */

public class Score {

    private String Username;
    private String XP;
    private Bitmap image;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public Score(String username, String XP, Bitmap image) {
        Username = username;
        this.XP = XP;
        this.image = image;
    }

    public Score() {
    }

    public String getUsername() {
        return Username;
    }

    public String getXP() {
        return XP;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setXP(String XP) {
        this.XP = XP;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
