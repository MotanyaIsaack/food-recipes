package com.example.foodreciepes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors get(){
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    //An executor service that can schedule commands to run after a given delay
    //Executors are responsible for executing runnable tasks
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService networkIO(){
        return mNetworkIO;
    }


}
