package com.pl.confessionwall;

import com.pl.confessionwall.entity.Message;
import com.pl.confessionwall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class MessageService {

    private final UserMapper userMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(UserMapper userMapper, MessageMapper messageMapper) {
        this.userMapper = userMapper;
        this.messageMapper = messageMapper;
    }

    public User register(String username, String password) {
        User existUser = getUserByUsername(username);
        if (existUser != null) {
            log.debug("用户名：{}已存在", username);
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userMapper.insert(user);
        return user;
    }

    public User getUserByUsername(String username) {
        return userMapper.selectOneByUsername(username);
    }

    public User removeUser(String username, String password) {
        User userFromDB = getUser(username, password);
        if (userFromDB == null) {
            return null;
        }
        int affectedRows = userMapper.deleteOneByUsername(username);
        log.debug("{} lines affected", affectedRows);
        return userFromDB;
    }

    public User login(String username, String password) {
        return getUser(username, password);
    }

    private User getUser(String username, String password) {
        User existUser = getUserByUsername(username);
        if (existUser == null) {
            log.debug("用户{}不存在！", username);
            return null;
        }
        if (!existUser.getPassword().equals(password)) {
            log.debug("用户名或密码错误！");
            return null;
        }
        return existUser;
    }

    /**
     * @param username 只能在登录后发布消息，username必在users表中
     * @param whom     可以不在users表中，只是一个随意的String
     */
    public Message publishMessage(String username, String whom, String what) {
        User srcUser = getUserByUsername(username);
        if (srcUser == null) {
            log.debug("未查询到当前登录用户：{}！", username);
            return null;
        }
        Message message = new Message();
        message.setUid(srcUser.getUid());
        message.setUsername(srcUser.getUsername());
        message.setWhom(whom);
        message.setWhat(what);
        messageMapper.insert(message);
        return message;
    }

    public int removeMessage(int mid) {
        int affectedRows = messageMapper.remove(mid);
        log.debug("{} rows affected", affectedRows);
        return affectedRows;
    }

    public List<Message> showMessages() {
        return messageMapper.selectAll();
    }


}
