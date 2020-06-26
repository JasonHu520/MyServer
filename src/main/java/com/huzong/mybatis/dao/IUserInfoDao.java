package com.huzong.mybatis.dao;

import com.huzong.mybatis.domain.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface IUserInfoDao {
    @Insert("insert into userinfo(userName, password, email, phoneNumber, LogState, City, image_head) " +
            "values(#{userName},#{password},#{email},#{phoneNumber},#{LogState},#{City},#{image_head})")
    void addUser(UserInfo userInfo);

    /**
     * 根据用户名查找
     * @param userName
     */
    @Select("select * from userinfo where userName= #{userName}")
    UserInfo findUserByName(String userName);

    @Update("update userinfo set userName = #{userName},password = #{password}," +
            "email = #{email},phoneNumber = #{phoneNumber},LogState = #{LogState},City = #{City}," +
            "image_head = #{image_head}" +
            " where id = #{id}")
    void updateUserInfo(UserInfo userInfo);


}
