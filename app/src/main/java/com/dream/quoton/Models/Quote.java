package com.dream.quoton.Models;

public class Quote {
    String quote;
    String tag;
    String userPic;
    String userName;
    String userId;
    int stars;

    public Quote(String quote, String tag, String userPic, String userName, String userId, int stars) {
        this.quote = quote;
        this.tag = tag;
        this.userPic = userPic;
        this.userName = userName;
        this.userId = userId;
        this.stars = stars;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
