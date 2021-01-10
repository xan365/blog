package com.bamboo.blog.web.admin;


import com.bamboo.blog.entity.User;
import com.bamboo.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage() {
        //返回后台管理登录页面
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        //System.out.println(user.getPassword());
        //System.out.println(user.getUsername());
        if(user != null) {
            user.setPassword(null); //不要把密码传到前面，在页面里拿到密码很不安全
            session.setAttribute("user", user);
            //获取成功，返回登录后的首页面
            return "admin/index";
        } else {
            //登录失败就重定向到登录页面，并返回错误信息
            attributes.addFlashAttribute("message", "用户名或密码不正确");
            return "redirect:/admin";
        }
    }

    //注销当前登录的用户
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
