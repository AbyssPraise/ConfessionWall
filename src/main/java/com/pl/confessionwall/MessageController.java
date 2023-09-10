package com.pl.confessionwall;

import com.pl.confessionwall.entity.Message;
import com.pl.confessionwall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/register.do")
    public String register(String username, String password, HttpServletRequest request) {
        if (username == null || password == null) {
            return "redirect:/register.html";
        }
        username = username.trim();
        password = password.trim();
        if (username.isEmpty() || password.isEmpty()) {
            return "redirect:/register.html";
        }

        User user = messageService.register(username, password);
        if (user == null) {
            log.debug("用户{}已被注册，注册失败", username);
            return "redirect:/register.html";
        }
        HttpSession session = request.getSession();
        session.setAttribute("curUser",user);
        log.debug("注册成功：{}",user);
        return "redirect:/index.html";
    }

    @PostMapping("/login.do")
    public String login(String username, String password, HttpServletRequest request) {
        if (username == null || password == null) {
            return "redirect:/login.html";
        }
        username = username.trim();
        password = password.trim();
        if (username.isEmpty() || password.isEmpty()) {
            return "redirect:/login.html";
        }

        User user = messageService.login(username, password);
        if (user == null) {
            User queriedUser = messageService.getUserByUsername(username);
            if (queriedUser == null) {
                log.debug("用户：{}未注册", username);
                return "redirect:/register.html";
            } else {
                log.debug("用户名或密码错误！");
                return "redirect:/index.html";
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("curUser",user);
            log.debug("登录成功：{}", user);
            return "redirect:/index.html";
        }
    }

    @GetMapping("/api/show-messages.json")
    @ResponseBody
    public List<Message> showMessageList() {
        List<Message> messageList = messageService.showMessages();
        log.debug("共有{}条消息", messageList.size());
        return messageList;
    }

    @PostMapping("/publish-message.do")
    public String publishMessage(String whom, String what, HttpServletRequest request) {
        User curUser = (User) getSession(request, "curUser");
        if (curUser == null) {
            return "redirect:/login.html";
        }
        Message message = messageService.publishMessage(curUser.getUsername(), whom, what);
        if (message != null) {
            log.debug("消息发布成功: {}", message);
            return "redirect:/";
        } else {
            log.debug("发布消息失败");
            return null;
        }
    }

    private Object getSession (HttpServletRequest request, String sessionKey) {
        HttpSession session = request.getSession(false);
        return session == null ? null : session.getAttribute(sessionKey);
    }

}
