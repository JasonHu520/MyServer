package com.huzong.mybatis.dao;

import com.huzong.mybatis.domain.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface IMessageDao {
    @Insert("insert into message(message_data, to_user, from_user_name)values(#{message_data},#{to_user},#{from_user_name})")
    void add_message(Message message);

    @Select("select * from message where to_user=#{to_user}")
    void findMessageByName(String to_user);
}
