package com.huzong.serlet;


import com.alibaba.fastjson.JSON;
import com.huzong.mybatis.dao.IFriendDao;
import com.huzong.mybatis.dao.IMessageDao;
import com.huzong.mybatis.dao.IUserInfoDao;
import com.huzong.mybatis.domain.Friend;
import com.huzong.mybatis.domain.Message;
import com.huzong.mybatis.domain.UserInfo;
import com.huzong.utils.IImage;
import com.huzong.utils.ImageUtil;
import com.huzong.utils.mail.MailOperation;
import com.huzong.utils.mail.mail_utils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyServlet implements Servlet {

    private static MyServlet myServlet;
    private static SqlSession sqlSession;
    private static InputStream in;
    HttpPostRequestDecoder decoder;
    Map<String, Object> paramMap;
    FileUpload upload_file = null;

    private MyServlet(){};

    /**
     * 单例模式
     * @return
     */
    public static MyServlet getInstance(){

        if (myServlet==null){
            synchronized (MyServlet.class){
                if(myServlet==null){
                    init();
                    myServlet = new MyServlet();
                }
            }
        }
        return myServlet;

    }

    public void doGet(FullHttpRequest request, Response response) {
        try {
            response.write("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户信息
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void update_userInfo(FullHttpRequest request, Response response) throws Exception {
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        String type = paramMap.get("type").toString();
        String data = paramMap.get(type).toString();
        IUserInfoDao userInfoDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo userInfo = userInfoDao.findUserByName(userName);
        if(userInfo ==null){
            response.write("error");
        }else{
            /**
             * 使用反射机制
             */
            type =type.substring(0, 1).toUpperCase()+type.substring(1);
            Method method = userInfo.getClass().getMethod("set"+type,Class.forName("java.lang.String"));
            method.invoke(userInfo,data);
            userInfoDao.updateUserInfo(userInfo);
            sqlSession.commit();
            response.write("ok");
        }
    }

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void register(FullHttpRequest request, Response response) throws Exception {
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        IUserInfoDao userInfoDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo userInfo = userInfoDao.findUserByName(userName);
        if (userInfo != null) {
            response.write(Constant.getInstance().getREGISTER_ERRO());
        }else{
            String password = paramMap.get("password").toString();
            String email = paramMap.get("email").toString();
            String City = paramMap.get("City").toString();
            String phoneNumber = paramMap.get("phoneNumber").toString();
            String LogState = paramMap.get("LogState").toString();
            String image_head ;
            try{
                image_head = paramMap.get("image_head").toString();
            }catch (NullPointerException e){
                image_head = "";
            }
            userInfo = new UserInfo();
            userInfo.setLogState(LogState);
            userInfo.setCity(City);
            userInfo.setPassword(password);
            userInfo.setPhoneNumber(phoneNumber);
            userInfo.setUserName(userName);
            userInfo.setEmail(email);
            userInfo.setImage_head(image_head);
            userInfoDao.addUser(userInfo);
            response.write(Constant.getInstance().getREGISTER_OK());
            sqlSession.commit();
        }
    }

    /**
     * 返回图片给安卓
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void putPictoAndroid(FullHttpRequest request, Response response) throws Exception {
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        String pic_name = paramMap.get("pic_name").toString();
        String pic_quality = paramMap.get("pic_quality").toString();
        String base_path = "C:/users/JasonHu/IdeaProjects/MyServer/picture/"+
                userName + "/";
        File file = new File(base_path+pic_name);
        /**
         * 当前文件是否存在
         */
        if (file.exists()){
            if (pic_quality.equals("原图")) {
                response.write(file);
            }else{
                String des = "C:/users/JasonHu/IdeaProjects/MyServer/picture/"+
                        userName + "/"+pic_quality+"/";
                File file1 = new File(des);
                if(!file1.exists()){
                    file1.mkdir();
                }
                File file12 = new File(des+pic_name);
                if(!file12.exists()){
                    IImage iImage =new ImageUtil();
                    iImage.reduceImg(file,des+pic_name,0.5f);//压缩
                }
                response.write(file12);
            }
        }else{
            response.write("您还未设置过头像哦^v^");
        }
    }

    @Override
    public void get_message_from_(FullHttpRequest request, Response response) throws Exception {
        parse_form(request);
        String message = paramMap.get("Msg").toString();
        String current_user = paramMap.get("current_user").toString();
        String to_user = paramMap.get("to_user").toString();
        String date = paramMap.get("current_date").toString();
        IMessageDao messageDao = sqlSession.getMapper(IMessageDao.class);
        Message message_ = new Message();
        message_.setDate(date);
        message_.setFrom_user_name(current_user);
        message_.setMessage_data(message);
        message_.setTo_user_name(to_user);
        messageDao.add_message(message_);
        sqlSession.commit();
        response.write("ok");
    }

    /**
     * 获取朋友列表
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void get_friend_list(FullHttpRequest request, Response response) throws Exception {
        // TODO: 2020/5/22
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        IFriendDao friendDao = sqlSession.getMapper(IFriendDao.class);
        List<Friend> friends =friendDao.findFriendByUser(userName);
        if (friends.size()==0){
            response.write(Constant.getInstance().getNO_FRIEND());
        }else{
            HashMap<String, List<Friend>> friendListMap =  new HashMap<>();
            friendListMap.put("friendList", friends);
            response.write(JSON.toJSONString(friendListMap));
        }
    }

    // TODO: 2020/5/23 接受上传的文件
    @Override
    public void getPicFromAndroid(FullHttpRequest request, Response response) {
        parse_form(request);
        BufferedOutputStream bos = null;
        FileInputStream fileInputStream = null;
        String userName = paramMap.get("userName").toString();
        String base_path = "C:/users/JasonHu/IdeaProjects/MyServer/picture/"+
        userName + "/";
        File file = new File(base_path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        //文件写入
        try{
            bos = new BufferedOutputStream(new FileOutputStream(base_path+upload_file.getFilename()));

            fileInputStream = new FileInputStream(upload_file.getFile().toString());
            byte[] buf = new byte[4096];
            int length = fileInputStream.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = fileInputStream.read(buf);
            }
            response.write("上传成功");
            System.out.println("用户"+userName+"保存了一张图片，详见"+base_path+upload_file.getFilename());
        }catch (Exception e){
            try {
                response.write("上传失败");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos!=null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void email_captcha(FullHttpRequest r, Response response) throws Exception {
        parse_form(r);
        MailOperation operation = new MailOperation();
        String email = paramMap.get("email").toString();
        if(email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
        {
            System.out.println("OK");
            // TODO: 2020/5/23
            //邮箱内容
            StringBuffer sb = new StringBuffer();
            String yzm = mail_utils.getRandomString();
            sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; " +
                    "font-size:18px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;  " +
                    " padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>" +
                    "<div style='width:950px;font-family:arial;'>欢迎使用约fun吧软件，您的注册码为：" +
                    "<br/><h2 style='color:green'>").append(yzm).
                    append("</h2><br/>本邮件由系统自动发出，请勿回复。" +
                            "<br/>感谢您的使用。<br/>重庆超神有限公司</div>")
                    .append("</div>");

            String res = operation.sendMail( email, sb.toString());
            if(res.equals("success")){
                //邮件发送成功
                response.write(yzm);
            }else {
                response.write(Constant.getInstance().getGET_EMAIL_ERRO());
            }
            System.out.println(res);
        }else {
            response.write(Constant.getInstance().getGET_EMAIL_FORM_ERRO());
        }


    }

    public void doPost(FullHttpRequest request, Response response) {
        try {
            if("/favicon.ico".equals(new URI(request.uri()).getPath())){
                return;
            }
            String mothod = getParameter("method",request);
            switch (mothod) {
                case "login" -> login(request, response);
                case "get_user_info"->get_user_info(request, response);
                case "isOnline"->isOnline(request, response);
                case "update_userInfo"->update_userInfo(request, response);
                case  "get_friend_list"->get_friend_list(request, response);
                case "getPicFromAndroid"->getPicFromAndroid(request, response);
                case "email_captcha"->email_captcha(request, response);
                case "register"->register(request, response);
                case "putPictoAndroid"->putPictoAndroid(request, response);
                case "get_message_from_"->get_message_from_(request, response);
            }
        } catch (Exception e) {
            try {
                response.write("出了一些问题哦");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * 用户登陆
     * @param r
     * @param response
     * @throws Exception
     */
    public void login(FullHttpRequest r, Response response) throws Exception{
        /*
          解析表单数据
         */
        parse_form(r);
        String userName = paramMap.get("userName").toString();
        String password = paramMap.get("password").toString();
        IUserInfoDao userInfoDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo userInfo = userInfoDao.findUserByName(userName);
        if(null==userInfo){
            response.write(Constant.getInstance().getNO_USER_ERRO());
        }else{
            if (userInfo.getPassword().equals(password)){
                response.write(Constant.getInstance().getLOGIN_OK());
                System.out.println("登陆成功");
            }else{
                response.write(Constant.getInstance().getPASSWORD_ERRO());
            }
        }
    }

    /**
     * 获取用户信息
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void get_user_info(FullHttpRequest request, Response response) throws Exception {
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        String password = paramMap.get("password").toString();
        IUserInfoDao userInfoDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo userInfo = userInfoDao.findUserByName(userName);
        if(null==userInfo){
            response.write(Constant.getInstance().getNO_USER_ERRO());
        }else{
            if (userInfo.getPassword().equals(password)){
                System.out.println(JSON.toJSONString(userInfo));
                response.write(JSON.toJSONString(userInfo));
            }else{
                response.write(Constant.getInstance().getPASSWORD_ERRO());
            }
        }

    }

    /**
     * 刷新用户登陆状态
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void isOnline(FullHttpRequest request, Response response) throws Exception {
        // TODO: 2020/5/23  刷新用户登陆状态
        parse_form(request);
        String userName = paramMap.get("userName").toString();
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(userName +"  "+ df.format(day));
        response.write(Constant.getInstance().getLOGIN_OK());
    }


    public static void init(){
        try {
            in = Resources.getResourceAsStream("mybatis_config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
            sqlSession= factory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, List<String>> getParameters(@NotNull FullHttpRequest r) {
        QueryStringDecoder decoder = new QueryStringDecoder(r.uri());
        return decoder.parameters();
    }

    public String getParameter(String name,FullHttpRequest r) {
        Map<String, List<String>> params = getParameters(r);
        List<String> param = params.get(name);
        if (null == param) {
            return null;
        } else {
            return param.get(0);
        }
    }

    /**
     * 解析表单数据
     */
    private void parse_form(FullHttpRequest r){
        /**
         * 解析数据
         */
        decoder = new HttpPostRequestDecoder(r);
        paramMap = new HashMap<>();
        /**
         * 解析post表单数据
         */
        decoder.offer(r);//form
        List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
        for (InterfaceHttpData parm : parmList) {
            if(parm instanceof  Attribute){
                Attribute data = (Attribute) parm;
                try {
                    paramMap.put(data.getName(), data.getValue());
                } catch (IOException e) {
                    System.out.println("数据解析错误");
                }
            }else if(parm instanceof FileUpload){
                upload_file = (FileUpload)parm;
            }

        }
    }


}
