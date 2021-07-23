package com.dream.quoton.Models;

public class User {
    String name;
    String uid;
    String pic;

    public User(String name, String uid, String pic) {
        this.name = name;
        this.uid = uid;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
