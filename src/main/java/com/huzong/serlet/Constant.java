package com.huzong.serlet;

public class Constant {
    private static Constant constant;


    //登录注册配置
    private String LOGIN_OK = "1";
    private String REGISTER_OK  = "5";
    private String LOGIN_ERRO = "2";
    private String PASSWORD_ERRO ="3";
    private String REGISTER_ERRO = "4";
    private String GET_EMAIL_ERRO = "6";
    private String GET_EMAIL_FORM_ERRO = "7";
    private String NO_USER_ERRO = "8";
    private String NO_FRIEND = "9";

    private Constant(){}

    public static Constant getInstance(){

        if (constant==null){
            synchronized (MyServlet.class){
                if(constant==null){
                    constant = new Constant();
                }
            }
        }
        return constant;
    }

    public String getLOGIN_OK() {
        return LOGIN_OK;
    }

    public String getREGISTER_OK() {
        return REGISTER_OK;
    }

    public String getLOGIN_ERRO() {
        return LOGIN_ERRO;
    }

    public String getPASSWORD_ERRO() {
        return PASSWORD_ERRO;
    }

    public String getREGISTER_ERRO() {
        return REGISTER_ERRO;
    }

    public String getGET_EMAIL_ERRO() {
        return GET_EMAIL_ERRO;
    }

    public String getGET_EMAIL_FORM_ERRO() {
        return GET_EMAIL_FORM_ERRO;
    }

    public String getNO_USER_ERRO() {
        return NO_USER_ERRO;
    }

    public String getNO_FRIEND() {
        return NO_FRIEND;
    }
}
