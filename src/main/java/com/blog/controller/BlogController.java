package com.blog.controller;

import com.blog.pojo.User;
import com.blog.service.BlogService;
import com.blog.service.impl.BlogServiceImp;
import com.blog.util.SessionUtil;
import com.blog.util.XSSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BlogController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    BlogService blogService;

    @RequestMapping(value = "/publishBlog.html")
    public ModelAndView publishBlog(String blogContent) {
        User user = SessionUtil.getUserSession(request);
        int len = blogContent.length();
        final int MAX_LEN = 1000;
        if (len > 0 && len < MAX_LEN && user != null) {
            blogContent = XSSFilter.filterBrackets(blogContent);
            blogService.addBlog(user.getUserid(), blogContent);
        }
        return new ModelAndView("redirect:/index.html");
    }

    @RequestMapping(value = "/deleteBlog.html")
    @ResponseBody
    public String deleteBlog(Integer blogId) {
        User user = SessionUtil.getUserSession(request);
        if (user == null) return "error";
        blogService.deleteBlog(user.getUserid(), blogId);
        return "success";
    }
}
