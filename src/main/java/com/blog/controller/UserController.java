package com.blog.controller;

import com.blog.pojo.User;
import com.blog.service.BlogService;
import com.blog.service.UserRelationService;
import com.blog.service.UserService;
import com.blog.service.impl.BlogServiceImp;
import com.blog.service.impl.UserServiceImp;
import com.blog.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserRelationService userRelationService;


    @RequestMapping(value = "/login.html")
    public ModelAndView showLoginPage(Integer page) {
        if (page == null) {
            page = 0;
        }
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("page", page);
        return modelAndView;
    }


    @RequestMapping(value = "/logout.html")
    public String logout() {
        SessionUtil.removeUserSession(request);
        return "redirect:/login.html";
    }


    @RequestMapping(value = "/getUserHeadPic.html")
    @ResponseBody
    public String getUserHeadPic(String userName) {
        User user = userService.getUserByUserName(userName);
        return user.getHeadpic() + "";
    }


    @RequestMapping(value = "/checkLogin.html")
    public ModelAndView checkLogin(String userName, String password) {
        boolean isValidUser = userService.checkPassword(userName, password);
        if (!isValidUser) {
            return new ModelAndView("redirect:/login.html", "error", "用户名或密码错误。");
        }
        User user = userService.getUserByUserName(userName);
        SessionUtil.setUserSession(request, user);
        return new ModelAndView("redirect:/index.html");
    }


    @RequestMapping(value = "/checkRegister.html")
    public ModelAndView checkRegister(String userName, String password, String confirmPassword, Integer headpic) {
        System.out.println(userName);
        System.out.println(headpic);
        if (!password.equals(confirmPassword)) {
            ModelAndView modelAndView = new ModelAndView("redirect:/login.html");
            modelAndView.addObject("error", "两次输入的密码不一致");
            modelAndView.addObject("page", 1);
            return modelAndView;
        }
        boolean isValidUser = userService.checkUserNameIllegal(userName);
        if (!isValidUser) {
            ModelAndView modelAndView = new ModelAndView("redirect:/login.html");
            modelAndView.addObject("error", "用户名已经存在");
            modelAndView.addObject("page", 1);
            return modelAndView;
        }
        userService.addUser(userName, confirmPassword, headpic);
        User user = userService.getUserByUserName(userName);
        SessionUtil.setUserSession(request, user);
        return new ModelAndView("redirect:/index.html");
    }


    @RequestMapping(value = "/user.html")
    public ModelAndView showUserPage(Integer userId, Integer page) {
        ModelAndView modelAndView = new ModelAndView("user");
        User user = SessionUtil.getUserSession(request);
        modelAndView.addObject("user", user);
        User theUser;
        if (userId == null) {
            theUser = user;
        } else {
            theUser = userService.getUserByUserId(userId);
        }
        modelAndView.addObject("theUser", theUser);
        if (page == null) {
            page = 0;
        }
        if (page == 0) {
            modelAndView.addObject("userBlog", blogService.getAllBlogByUserId(theUser.getUserid()));
        } else if (page == 1) {
            modelAndView.addObject("follows", userRelationService.getFollowers(theUser.getUserid()));
        } else if (page == 2) {
            modelAndView.addObject("follows", userRelationService.getFollowings(theUser.getUserid()));
        }
        modelAndView.addObject("page", page);
        return modelAndView;
    }


    @RequestMapping(value = "/follow.html")
    public String follow(Integer userId) {
        User user = SessionUtil.getUserSession(request);
        userRelationService.follow(user.getUserid(), userId);
        return "redirect:/user.html?userId=" + userId;
    }
}
