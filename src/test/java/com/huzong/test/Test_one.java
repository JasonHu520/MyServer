package com.huzong.test;

import com.alibaba.fastjson.JSON;
import com.huzong.mybatis.dao.IFriendDao;
import com.huzong.mybatis.dao.IUserInfoDao;
import com.huzong.mybatis.domain.Friend;
import com.huzong.mybatis.domain.UserInfo;
import com.huzong.server.server;
import com.huzong.utils.IImage;
import com.huzong.utils.ImageUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Test_one {
    private SqlSession sqlSession;
    private InputStream in;
    @Before
    public void test(){
        try {
            in = Resources.getResourceAsStream("mybatis_config.xml");
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
            sqlSession= factory.openSession();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test_findByID(){
        IUserInfoDao userDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo user = userDao.findUserByName("胡宗超");
        System.out.println(user);
    }

    @Test
    public void addUser(){
        UserInfo userInfo = new UserInfo();
        userInfo.setCity("重庆");
        userInfo.setEmail("9620793..");
        userInfo.setPassword("123");
        userInfo.setUserName("毛老师");
        userInfo.setPhoneNumber("17723947489");
        userInfo.setLogState("yes");
        IUserInfoDao infoDao = sqlSession.getMapper(IUserInfoDao.class);
        infoDao.addUser(userInfo);

    }
    @Test
    public void test_findFriend(){
        IFriendDao friendDao = sqlSession.getMapper(IFriendDao.class);
        for(Friend f :friendDao.findFriendByUser("胡宗超"))
            System.out.println(f);
    }
    @Test
    public void test_update(){

        IUserInfoDao infoDao = sqlSession.getMapper(IUserInfoDao.class);
        UserInfo user =infoDao.findUserByName("毛老师");
        user.setLogState("yes");
        infoDao.updateUserInfo(user);
    }

    @After
    public void destroy(){
        if(sqlSession!=null)
        {
            sqlSession.commit();
            sqlSession.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test_friend(){
        IFriendDao friendDao = sqlSession.getMapper(IFriendDao.class);
        List<Friend> friends =friendDao.findFriendByUser("胡宗超");
        for(Friend friend:friends){
            System.out.println(friend);
        }
        HashMap<String, List<Friend>> friendList =  new HashMap<>();
        friendList.put("friendlist", friends);
        System.out.println(JSON.toJSONString(friendList));
    }
    @Test
    public void test_File(){
        File file = new File("C:\\Users\\JasonHu\\IdeaProjects\\MyServer\\picture\\胡宗超\\2020-05-24-13-48-12.jpg");
        IImage iImage = new ImageUtil();
        System.out.println(Arrays.toString(iImage.getImgWidthHeight(file)));
        iImage.reduceImg(file,"C:\\Users\\JasonHu\\IdeaProjects\\MyServer\\picture\\胡宗超\\2020-05-24-13-48-13.jpg",0.5f);
    }

}
