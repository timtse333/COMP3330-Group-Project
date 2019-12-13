package tim.hku.comp3330.DataClass;

import android.graphics.Bitmap;

public class User {
    private String userID; // system generated, cannot be modified once user created
    private String userName;
    // private Bitmap userIcon;
    private String loginName;
    private String password;
    private String icon;
    public User() {}
    public User(String userID, String userName, String loginName, String password/*, Bitmap userIcon*/) {
        this.userID = userID;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        //this.userIcon = userIcon;
    }

    public User(String userID, String userName, String loginName, String password, String icon) {
        this.userID = userID;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        this.icon = icon;
    }

    // getters and setters
    public String getUserID() {
        return userID;
    }
    public String getUserName() {
        return userName;
    }
    /*public Bitmap getUserIcon() {
        return userIcon;
    }*/
    public String getLoginName() {
        return loginName;
    }
    public String getPassword() {
        return password;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /*public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }*/
    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
