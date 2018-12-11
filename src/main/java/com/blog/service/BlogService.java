package com.blog.service;

import com.blog.pojo.Blog;
import com.blog.pojo.BlogDetail;

import java.util.List;

public interface BlogService {

    List<BlogDetail> getAllBlogOfHome(int userId);

    List<BlogDetail> getAllBlogByUserId(int userId);

    Blog getBlogByBlogId(int id);

    void addBlog(int userId, String content);

    void editBlog(int blogId, String content);

    void deleteBlog(int userId, int blogId);
}
