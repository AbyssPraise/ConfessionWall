package com.pl.confessionwall;

import com.pl.confessionwall.entity.Message;
import com.pl.confessionwall.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class ConfessionWallApplicationTests {

    @Autowired
    MessageService messageService;

    @Test
    void testRegisterAndLogin() {
        String username = "彦";
        String password = "tianren";

        User user = messageService.register(username, password);
        Assert.isTrue(user != null);
        Assert.isTrue(Objects.equals(user.getUsername(), username));
        Assert.isTrue(Objects.equals(user.getPassword(), password));

        User logonUser = messageService.login(username, password);
        Assert.isTrue(logonUser != null);
        Assert.isTrue(Objects.equals(logonUser.getUsername(), username));
        Assert.isTrue(Objects.equals(logonUser.getPassword(), password));

        User removedUser = messageService.removeUser(username, password);
        Assert.isTrue(removedUser.getPassword().equals(password));
        Assert.isTrue(removedUser.getUsername().equals(username));

    }

    @Test
    void testPublishMessage() {
        String username = "鹤熙";
        String whom = "sky";
        String what = "I love you";
        Message message = messageService.publishMessage(username, whom, what);
        User user = messageService.getUserByUsername(username);
        Assert.isTrue(message != null);
        Assert.isTrue(user != null);
        Assert.isTrue(message.getUid().equals(user.getUid()));
        Assert.isTrue(message.getWhat().equals(what));
        Assert.isTrue(message.getWhom().equals(whom));

        List<Message> messages = messageService.showMessages();
        Assert.isTrue(messages.size() == 1);
        for (Message m : messages) {
            Assert.isTrue(m != null);
            Assert.isTrue(m.getUid().equals(user.getUid()));
            Assert.isTrue(m.getWhat().equals(what));
            Assert.isTrue(m.getWhom().equals(whom));
        }

        int affectedRows = messageService.removeMessage(message.getMid());
        Assert.isTrue(affectedRows == 1);
    }

}
