package com.pl.confessionwall;

import com.pl.confessionwall.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageMapper {
    int insert(Message message);

    int remove(@Param("mid")int mid);

    List<Message> selectAll();
}
