package com.huzong.mybatis.domain;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String City;
    private String phoneNumber;
    private String LogState;
    private String image_head;

    public UserInfo(){

    }

    public UserInfo(Integer id,
                    String userName,
                    String password,
                    String email,
                    String city,
                    String phoneNumber,
                    String logState,
                    String image_head) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        City = city;
        this.phoneNumber = phoneNumber;
        LogState = logState;
        this.image_head = image_head;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLogState() {
        return LogState;
    }

    public void setLogState(String logState) {
        LogState = logState;
    }

    public String getImage_head() {
        return image_head;
    }

    public void setImage_head(String image_head) {
        this.image_head = image_head;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", City='" + City + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", LogState='" + LogState + '\'' +
                ", image_head='" + image_head + '\'' +
                '}';
    }

}
