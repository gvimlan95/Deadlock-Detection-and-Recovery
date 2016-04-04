package com.company;

import sun.jvm.hotspot.debugger.ThreadAccess;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {

        Random rand = new Random();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numProcess = sc.nextInt();

        System.out.print("Enter number of resources: ");
        int numResource = sc.nextInt();

        System.out.print("Enter max number of each resources: ");
        int maxResource = sc.nextInt();

        int maxClaim[][] = new int[numProcess][numResource];
        int allocatedResources[][] = new int[numProcess][numResource];
        int randomResourceIDSequence[][] = new int[numProcess][numResource];
        int[] totalAllocatedResource = new int[numResource];
        int[] totalResourceAvailable = new int[numResource];
        int[] running = new int[numProcess];
        int[] maxRes = new int[numResource];
        int safe = 0,count = 0,exec;


        for(int i = 0;i<numResource;i++){
            maxRes[i] = sc.nextInt();
        }

        for(int i =0;i<numProcess;i++){
            running[i] = 1;
            count++;
        }

        System.out.println("Please enter the allocated resources for each process: ");
        //RANDOM ALLOCATION REQUEST
        for(int i=0;i<numProcess;i++){
            int tempTotal = 0;
            for(int j=0;j<numResource;j++){
//                allocatedResources[i][j] = rand.nextInt(maxClaim[i][j] - availableResource[i]);
                allocatedResources[i][j] = sc.nextInt();
            }
        }

        //RANDOM VALUE FOR RESOURCES
        System.out.println("Please enter the max resources for each process: ");
        for(int i =0;i<numProcess;i++){
            for(int j = 0;j<numResource;j++){
//                maxClaim[i][j] = rand.nextInt(maxResource)+1;
                maxClaim[i][j] = sc.nextInt();
            }
        }

        //TOTAL ALLOCATION FOR EACH RESOURCES
        for(int j=0;j<numResource;j++){
            for(int i =0;i<numProcess;i++){
                totalAllocatedResource[j] += allocatedResources[i][j];
            }
        }

        //RESOURCE AVAILABLE FOR EACH RESOURCES
        for(int i =0;i<numResource;i++){
            totalResourceAvailable[i] = maxRes[i] - totalAllocatedResource[i];
        }

        //RANDOM SEQUENCE FOR RESOURCES ID FOR EACH PROCESSES
        for(int i = 0;i< numProcess;i++){
            for(int j = 0;j < numResource;j++){
                randomResourceIDSequence[i][j] = j;
            }
        }
        int[] tempRandomResource = new int[numResource];
        for(int i = 0;i<numProcess;i++){
            for(int j = 0;j<numResource;j++){
                tempRandomResource[j] = randomResourceIDSequence[i][j];
            }

            tempRandomResource = ShuffleArray(tempRandomResource);

            for(int j = 0;j<numResource;j++){
                randomResourceIDSequence[i][j] = tempRandomResource[j];
            }
        }

        ArrayList<Object> objectList = new ArrayList<>();
        for(int i =0;i<5;i++){
            Object object = new Object();
            objectList.add(object);
        }

        ArrayList<Lock> lockList = new ArrayList<>();
        for(int i = 0;i<numResource;i++){
            Lock lock = new ReentrantLock();
            lockList.add(lock);
        }

        ArrayList<Resource> resourceList = new ArrayList<>();
        for(int i =0;i<numResource;i++){
            Resource resource = new Resource(i,totalResourceAvailable[i],maxRes[i]);
            resourceList.add(resource);
        }

        for(int i=0;i<numProcess;i++){

            int[] pidResourceSequence = new int[numResource];
            int[] pidMaxResources = new int[numResource];
            int[] pidAllocatedResources = new int[numResource];
            int[] pidNeededResource = new int[numResource];

            for(int j =0;j<numResource;j++){
                pidResourceSequence[j] = randomResourceIDSequence[i][j];
                pidAllocatedResources[j] = allocatedResources[i][j];
                pidMaxResources[j] = maxClaim[i][j];
                pidNeededResource[j] = pidMaxResources[j] - pidAllocatedResources[j];
            }

            Process process = new Process(i,pidResourceSequence,pidAllocatedResources,pidMaxResources,pidNeededResource);
            NewThread nt = new NewThread(i,process,lockList,resourceList);
            nt.start();
            try {
                detectDeadlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
//        Set<Thread> threads = map.keySet();//Get the keys of the map, in this case the key is the thread
//        for(Thread thread : threads){//iterate over all the threads
//            if(thread instanceof NewThread){//check to see if it is one of our custom threads
//                NewThread custom = (NewThread) thread;//cast it to a custom thread
//                custom.printDetails();//call your method
//            }
//        }
    }



    public static void detectDeadlock() throws InterruptedException {
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        long[] ids = tmx.findDeadlockedThreads();
        if (ids != null) {
            ThreadInfo[] infos = tmx.getThreadInfo(ids, true, true);
            System.out.println("The following threads are deadlocked:");
            for (ThreadInfo ti : infos) {
                System.out.println(ti);
            }
        }
    }

    public static int[] ShuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}
