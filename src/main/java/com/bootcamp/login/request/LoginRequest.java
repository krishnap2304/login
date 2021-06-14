package com.bootcamp.login.request;

public class LoginRequest {
    private String username;
    private String password;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private String newPassword;

    public String getCaptchaResp() {
        return captchaResp;
    }

    public void setCaptchaResp(String captchaResp) {
        this.captchaResp = captchaResp;
    }

    private String captchaResp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
