package com.example.tocare.BLL.Model;

import androidx.annotation.NonNull;

import java.util.Date;

public class Comment {

    private String commentId;
    private String postId;
    private String author;
    private String comment;
    private String publish;
    private String created;


    public Comment(String commentId, String postId, String author, String comment, String publish, String create) {
        setCommentId(commentId);
        setAuthor(author);
        setComment(comment);
        setPublish(publish);
        setPostId(postId);
        setCreated(create);
    }

    public Comment(String author, String postId, String comment, String publish) {
        setAuthor(author);
        setComment(comment);
        setPublish(publish);
        setPostId(postId);
        setCreated(new Date().toString());
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    @NonNull
    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", postId='" + postId + '\'' +
                ", author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", publish='" + getPublish() + '\'' +
                ", created='" + getCreated() + '\'' +
                '}';
    }
}
