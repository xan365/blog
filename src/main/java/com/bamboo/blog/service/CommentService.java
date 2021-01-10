package com.bamboo.blog.service;

import com.bamboo.blog.entity.Comment;

import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
