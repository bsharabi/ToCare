package com.example.tocare.BLL.Listener;



public interface UploadCallback {
    void onSuccessUpload(boolean success, Exception e, String myUri,String postId);
    void onSuccess(boolean success, Exception e);


}
