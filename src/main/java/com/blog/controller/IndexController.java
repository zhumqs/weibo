package com.blog.controller;

import com.blog.pojo.BlogDetail;
import com.blog.pojo.User;
import com.blog.service.BlogService;
import com.blog.service.impl.BlogServiceImp;
import com.blog.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = {"/", "/index.html"})
    public ModelAndView showIndexPage() {
        User user = SessionUtil.getUserSession(request);
        if (user == null) {
            return new ModelAndView("redirect:/login.html");
        }
        ModelAndView modelAndView = new ModelAndView("index");
        List<BlogDetail> allBlog = blogService.getAllBlogOfHome(user.getUserid());
        modelAndView.addObject("allBlog", allBlog);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
