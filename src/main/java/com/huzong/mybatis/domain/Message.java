package com.huzong.mybatis.domain;

import java.util.Date;

public class Message {
    private Integer id;
    private String message_data;
    private String from_user_name;
    private String to_user;
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage_data() {
        return message_data;
    }

    public void setMessage_data(String message_data) {
        this.message_data = message_data;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user = to_user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message_data='" + message_data + '\'' +
                ", from_user_name='" + from_user_name + '\'' +
                ", to_user_name='" + to_user + '\'' +
                ", date=" + date +
                '}';
    }
}
