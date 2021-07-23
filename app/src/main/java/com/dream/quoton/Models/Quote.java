package com.dream.quoton.Models;

public class Quote {
    String quote;
    String tag;
    int ups;
    int downs;
    String userPic;
    String userName;
    String userId;

    public Quote(String quote, String tag, int ups, int downs, String userPic, String userName, String userId) {
        this.quote = quote;
        this.tag = tag;
        this.ups = ups;
        this.downs = downs;
        this.userPic = userPic;
        this.userName = userName;
        this.userId = userId;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
