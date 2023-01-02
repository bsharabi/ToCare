package com.example.tocare.BLL.Listener;

import java.util.List;

public interface FollowCallback {
    void onFollowing(boolean success);
    void onFollow(boolean success);
}

interface FollowersCallback {
    void result(boolean success, List<String> followers);

}