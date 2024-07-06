package com.zavierdev.hlcnotes;

import android.app.Application;

import com.zavierdev.hlcnotes.di.DatabaseInjector;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseInjector.init(this);
    }
}
