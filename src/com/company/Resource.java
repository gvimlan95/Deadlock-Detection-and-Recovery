package com.company;

/**
 * Created by VIMLANG on 29/03/2016.
 */
public class Resource extends Object {

    int processID,id,currentlyAvailable,totalResource;

    public Resource(int rid, int currentlyAvailable, int totalResource){
        this.id = rid;
        this.currentlyAvailable = currentlyAvailable;
        this.totalResource = totalResource;
    }

    public int getProcessID(){
        return this.id;
    }

    public int getID(){
        return this.id;
    }

    public int getCurrentlyAvailable() {
        return currentlyAvailable;
    }

    public void setCurrentlyAvailable(int currentlyAvailable){
        this.currentlyAvailable = currentlyAvailable;
    }

    public int getTotalResource() {
        return totalResource;
    }
}