package com.huzong.mybatis.dao;

import com.huzong.mybatis.domain.Friend;
import com.huzong.mybatis.domain.Message;
import com.huzong.mybatis.domain.UserInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IFriendDao {

    @Select("select * from friendlist where user= #{user}")
    List<Friend> findFriendByUser(String user);

}
