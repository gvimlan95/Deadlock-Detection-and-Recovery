package com.company;

import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class NewThread extends Thread {

    int pid;
    ArrayList<Lock> lockList;
    ArrayList<Resource> resourceList;
    Process process;
    int[] resourceSequence;

    NewThread(int processID,Process process, ArrayList<Lock> rLock,ArrayList<Resource> resourceList) {
        this.pid = processID;
        this.lockList = rLock;
        this.process = process;
        this.resourceSequence = process.randomSequence;
        this.resourceList = resourceList;
    }

    @Override
    public void run() {
        int totalResourceSize = this.resourceList.size();
        int count = 0;
        Random rand = new Random();
        boolean locked = false;


        while(count < totalResourceSize){
            if(!locked){
                try {
                    if(lockList.get(resourceSequence[count]).tryLock(500,TimeUnit.MILLISECONDS)){
                        lockList.get(resourceSequence[count]).lock();
                        System.out.println("PID["+pid+"] LOCKED on "+resourceSequence[count] +" lock id: "+lockList.get(resourceSequence[count]));
                        locked = true;
//                        lockList.get(resourceSequence[count]).unlock();
//                        System.out.println("PID["+pid+"] FREED THE PROCESS "+resourceSequence[count]+" lock id: "+lockList.get(resourceSequence[count]));
                    }else{
                        System.out.println("PID["+pid+"] CANT LOCK on "+resourceSequence[count] +" lock id: "+lockList.get(resourceSequence[count]));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(locked){
                if(process.neededResources[resourceSequence[count]]< resourceList.get(resourceSequence[count]).getCurrentlyAvailable()){
                    resourceList.get(resourceSequence[count]).setCurrentlyAvailable(process.neededResources[resourceSequence[count]] + process.allocatedResource[resourceSequence[count]]);
                    lockList.get(resourceSequence[count]).unlock();
                    System.out.println("PID["+pid+"] FREED THE PROCESS "+resourceSequence[count]+" lock id: "+lockList.get(resourceSequence[count]));
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    locked = false;
                    count++;
                }else{
                    System.out.println("PID["+pid+"] NOT ENOUGH RESOURCE for "+resourceSequence[count]);
                }
            }
        }





//        while(count < totalResourceSize) {
//            try {
//                if (lockList.get(resourceSequence[count]).tryLock(500,TimeUnit.MILLISECONDS)) {
//                    System.out.println("PID[" + pid + "]" + " locks thread on the resource : " + this.resourceSequence[count]);
//                    lockList.get(resourceSequence[count]).lock();
//
//                    if (process.neededResources[resourceSequence[count]] < resourceList.get(resourceSequence[count]).getCurrentlyAvailable()) {
//                        resourceList.get(resourceSequence[count]).setCurrentlyAvailable(process.neededResources[resourceSequence[count]] + process.allocatedResource[resourceSequence[count]]);
//                        lockList.get(resourceSequence[count]).unlock();
//                        System.out.println("PID[" + pid + "]" + " unlocks resource no: " + this.resourceSequence[count]);
//                        count++;
//                    } else {
//                        System.out.println("PID[" + pid + "] keeps lock on " + resourceSequence[count]);
//                        break;
//                    }
//                } else {
//                    System.out.println("PID[" + pid + "] attempting to get lock on " + resourceSequence[count]);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(lockList.get(resourceSequence[count]).tryLock()){
//            System.out.println("PID[" + pid + "]" + " locks thread on the resource : " + this.resourceSequence[count]);
//            lockList.get(resourceSequence[count]).lock();
//        }else{
//
//        }
    }

    public void printDetails(){
//        System.out.println("PID["+this.pid+"] locks ");
    }
}


