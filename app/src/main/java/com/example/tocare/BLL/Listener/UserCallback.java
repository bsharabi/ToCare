package com.example.tocare.BLL.Listener;

import com.example.tocare.BLL.Model.UserModel;

public interface UserCallback {
    void result(boolean success, UserModel userModel);
}
