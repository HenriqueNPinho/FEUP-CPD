package Main;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import Membership.*;

public class Store {

    public static String mcastAddr;
    public static int mcastPort;
    public static String nodeId;
    public static int storePort;
    public static ScheduledThreadPoolExecutor executor;
    public static MulticastChannel mcChannel;

    public static int counter = -1;

    public static ArrayList<String> log;

    public static void main(String[] args) {

        mcastAddr = args[0];
        mcastPort = Integer.parseInt(args[1]);
        nodeId = args[2];
        storePort = Integer.parseInt(args[3]);

        mcChannel = new MulticastChannel(mcastAddr, mcastPort);

        log = new ArrayList<String>();

        int cores = Runtime.getRuntime().availableProcessors();

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(cores);

        
        executor.execute(new Receiver(storePort));
        
        executor.execute(mcChannel);

        

        //ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        //scheduled.scheduleAtFixedRate(new MulticastReceiver(), 0, 1, TimeUnit.SECONDS);
    }


    public void addLogEntry(String nodeId, int counter) {
        for(int i = 0; i < log.size(); i++) {
            String[] nodeInfo = log.get(i).split(" ");

            if(nodeId.equals(nodeInfo[0])) {
                log.set(i, nodeId + " " + Integer.toString(counter));
                return;
            }
        }

        log.add(nodeId + " " + Integer.toString(counter));
        return;
    }
}