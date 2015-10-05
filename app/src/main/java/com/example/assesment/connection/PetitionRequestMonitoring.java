package com.example.assesment.connection;

/**
 * Created by dcastrillo on 03/10/2014.
 */
public interface PetitionRequestMonitoring {
    public void onServiceRequestStarted();
    public void onServiceRequestFinished(String result);
}