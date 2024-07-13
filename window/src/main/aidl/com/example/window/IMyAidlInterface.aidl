package com.example.window;
import com.example.window.IActivtyShowCallback;

interface IMyAidlInterface {
    void monitorActivityShow(IActivtyShowCallback callback);
}