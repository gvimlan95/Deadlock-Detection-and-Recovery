package com.company;

/**
 * Created by VIMLANG on 29/03/2016.
 */
public class Process{


    int[] randomSequence;
    int[] maxResources;
    int[] allocatedResource;
    int[] neededResources;
    int id;


    public Process(int pid,int[] resourceSequence,int[] maxResource,int[] allocatedResources,int[] neededResources){
        this.id = pid;
        this.randomSequence = resourceSequence;
        this.maxResources = maxResource;
        this.allocatedResource = allocatedResources;
        this.neededResources = neededResources;
    }

    public int[] getRandomSequence() {
        return randomSequence;
    }


    public int[] getMaxResources() {
        return maxResources;
    }

    public int[] getAllocatedResource() {
        return allocatedResource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getNeededResources() {
        return neededResources;
    }

    public void setNeededResources(int rid,int resource) {
        this.neededResources[rid] = resource;
    }
}
