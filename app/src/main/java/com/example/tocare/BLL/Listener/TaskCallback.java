package com.example.tocare.BLL.Listener;

import com.example.tocare.BLL.Model.Task;


public interface TaskCallback {
    void result(boolean success, Task task);
}
