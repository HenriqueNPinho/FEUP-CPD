package Main;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import Membership.*;

public class Store {

    public static String mcastAddr;
    public static int mcastPort;
    public static String nodeId;
    public static int storePort;
    
    public static ScheduledThreadPoolExecutor executor;
    

    public static int counter;

    public static ArrayList<String> log;

    public static ArrayList<String> sentClusterInfo;

    public static void main(String[] args) {

        if(args.length != 4) {
            System.err.println("USAGE: java <mcast_addr> <mcast_port> <node_id> <store_port>");
            return;
        }

        mcastAddr = args[0];
        mcastPort = Integer.parseInt(args[1]);
        nodeId = args[2];
        storePort = Integer.parseInt(args[3]);

        
        log = new ArrayList<String>();

        sentClusterInfo = new ArrayList<String>();

        int cores = Runtime.getRuntime().availableProcessors();
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(cores);

        loadCounter();
        
        executor.execute(new ProtocolReceiver(storePort));
        System.out.println(" > TCP Potocol Channel Open on: " + Integer.toString(storePort));
        
        executor.execute(new MulticastChannel(mcastAddr, mcastPort));
        System.out.println(" > MultiCast Channel Open on: " + mcastAddr + ":" + Integer.toString(mcastPort));

        //executor.scheduleWithFixedDelay(new CastMembershipInfo(mcastAddr, mcastPort,"CASTING...."), 5, 1, TimeUnit.SECONDS);
       
        
        
        Runtime.getRuntime().addShutdownHook(new Thread(Store::saveCounter));

        

        //ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        //scheduled.scheduleAtFixedRate(new MulticastReceiver(), 0, 1, TimeUnit.SECONDS);
    }


    public static void addLogEntry(String nodeId, String counter) {
        sentClusterInfo.clear(); // Clears nodes sent membership info list
        
        for(int i = 0; i < log.size(); i++) {
            String[] nodeInfo = log.get(i).split(" ");

            if(nodeId.equals(nodeInfo[0])) {
                log.set(i, nodeId + " " + counter);
                return;
            }
        }

        log.add(nodeId + " " + counter);
        
    }


    public static synchronized void addToLog(ArrayList<String> logs) {
        boolean skipLine = false;

        for(int i = 0; i < logs.size(); i++) {
            skipLine = false;
            
            String[] newLogLine = logs.get(i).split(" ");
            
            for(int j = 0; j < log.size(); j++) {
                String[] logLine = log.get(j).split(" ");
                
                if(newLogLine[0].equals(logLine[0])) {
                    
                    if(Integer.parseInt(logLine[1]) < Integer.parseInt(newLogLine[1])) {
                        log.set(i, logs.get(j));
                    }
                    skipLine = true;
                    break;
                }
            }

            if(skipLine) continue;
            log.add(log.size(), logs.get(i));
            
        }


        
    }

    public static void printLog() {
        for(int i = 0; i < log.size(); i++) {
            String line = "#"+Integer.toString(i+1)+": " + log.get(i);
            System.out.println(line);
        }
        System.out.println();
    }


    public static ArrayList<String> getLogs() {
        ArrayList<String> logs = new ArrayList<String>();

        int logSize = log.size();

        int i = 0;

        if(logSize > 32) {
            i = logSize - 32;
        }

        int j = 0;

        for(; i < log.size(); i++) {
            logs.add(log.get(i));
            j++;
        }

        return logs;
    }


    public static ArrayList<String> getNodes() {
        ArrayList<String> nodes = new ArrayList<String>();
        int j = 0;
        
        for(int i = 0; i < log.size(); i++) {
            String[] nodeInfo = log.get(i).split(" ");
            int counter = Integer.parseInt(nodeInfo[1].trim());
            
            if(counter % 2 == 0) {
                
                nodes.add(j, nodeInfo[0].trim());
                j++;
            }
        }

        return nodes;
    }


    private static void saveCounter() {
        try {
            String filename = "Nodes/" + nodeId + "/counter";
            
            File file = new File(filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(counter);
            
            out.close();
            fileOut.close();

            Store.executor.shutdown();

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    //loads this peer storage from a file called storage.ser if it exists
    private static void loadCounter() {
        try {
            String filename = "Nodes/" + nodeId + "/counter";

            File file = new File(filename);
            if (!file.exists()) {
                counter = -1;
                return;
            }

            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            
            counter = (Integer) in.readObject();
            
            in.close();
            fileIn.close();

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}