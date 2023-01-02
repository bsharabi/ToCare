package com.example.tocare.BLL.Listener;

import java.util.List;

public interface FollowingCallback {
    void result(boolean success, List<String> following);

}
