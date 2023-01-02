package com.example.tocare.BLL.Model;

public class Comment {

    private String commentId;
    private String postId;
    private String author;
    private String comment;
    private String publish;


    public Comment(String commentId,String postId, String author,  String comment, String publish) {
        this.commentId = commentId;
        this.author = author;
        this.comment = comment;
        this.publish = publish;
        this.postId=postId;
    }

    public Comment(String author, String postId, String comment, String publish) {
        this.author = author;
        this.comment = comment;
        this.publish = publish;
        this.postId=postId;
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

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", publish='" + publish + '\'' +
                '}';
    }
}
