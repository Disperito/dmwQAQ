package cn.dmwqaq.web.pojo.po;

public class User {

    private String username;
    private String hashedPassword;
    private String passwordSalt;
    private String nickname;

    public User(String username, String hashedPassword, String passwordSalt, String nickname) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.passwordSalt = passwordSalt;
        this.nickname = nickname;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
