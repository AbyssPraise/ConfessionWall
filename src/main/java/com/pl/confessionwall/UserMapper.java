package com.pl.confessionwall;

import com.pl.confessionwall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    int insert(User user);

    int deleteOneByUsername(@Param("username") String username);

    User selectOneByUsername(@Param("username") String username);
}
