package com.huzong.serlet;

import io.netty.handler.codec.http.FullHttpRequest;

public interface Servlet {
    void doGet(FullHttpRequest request, Response response);
    void doPost(FullHttpRequest request, Response response);
    void login(FullHttpRequest request, Response response) throws Exception;
    void get_user_info(FullHttpRequest request, Response response) throws Exception;
    void isOnline(FullHttpRequest request, Response response) throws Exception;
    void update_userInfo(FullHttpRequest request, Response response)throws Exception;
    void get_friend_list(FullHttpRequest request, Response response)throws Exception;
    void getPicFromAndroid(FullHttpRequest request, Response response)throws Exception;
    void email_captcha(FullHttpRequest request, Response response)throws Exception;
    void register(FullHttpRequest request, Response response)throws Exception;
    void putPictoAndroid(FullHttpRequest request, Response response)throws Exception;
    void get_message_from_(FullHttpRequest request, Response response)throws Exception;
}
