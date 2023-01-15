package com.example.tocare.BLL.Model;


import androidx.annotation.NonNull;

import java.util.Date;

public class Notification {

    private String notificationId;
    private String postId;
    private String created;
    private String msg;
    private boolean isPost;
    private String extra;
    private boolean isRead;
    private String fromUserId;
    private String toUserId;
    private String type;

    public Notification(String notificationId, String type, String postId, String msg, boolean isPost, String extra, String fromUserId, String toUserId) {
        setNotificationId(notificationId);
        setPostId(postId);
        setCreated(new Date().toString());
        setMsg(msg);
        setType(type);
        setPost(isPost);
        setExtra(extra);
        setRead(false);
        setFromUserId(fromUserId);
        setToUserId(toUserId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Notification() {
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + getNotificationId() + '\'' +
                ", postId='" + getPostId() + '\'' +
                ", created='" + getCreated() + '\'' +
                ", msg='" + getMsg() + '\'' +
                ", isPost=" + isPost() +
                ", extra='" + getExtra() + '\'' +
                ", isRead=" + isRead() +
                ", fromUserId='" + getFromUserId() + '\'' +
                ", toUserId='" + getToUserId() + '\'' +
                '}';
    }
}
