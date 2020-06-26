package com.huzong.mybatis.domain;

import com.sun.jdi.InconsistentDebugInfoException;

import java.io.Serializable;

public class Friend implements Serializable {
    private Integer id_f;
    private String user;
    private String friend_name;
    private String onlineState;
    private String head_pic;

    public Integer getId_f() {
        return id_f;
    }

    public void setId_f(Integer id_f) {
        this.id_f = id_f;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    @Override
    public String toString() {
        return "FriendDao{" +
                "id_f=" + id_f +
                ", user='" + user + '\'' +
                ", friend_name='" + friend_name + '\'' +
                ", onlineState='" + onlineState + '\'' +
                ", head_pic='" + head_pic + '\'' +
                '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
