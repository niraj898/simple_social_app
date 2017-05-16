package com.niraj.social;

import android.app.Application;

import com.parse.Parse;

public class Starter extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("VJIEqUSuP7wPToRELf5iwIJW7HIlq7pi2paUDMiR")
                .clientKey("JFAK3T7yqhjGSZB8kmTDHtOR1G1vQCYxfMcSWI9D")
                .server("https://parseapi.back4app.com/").build()
        );
    }
}
