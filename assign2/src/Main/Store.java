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
    public static MulticastChannel mcChannel;

    public static int counter;

    public static ArrayList<String> log;

    public static ArrayList<String> sentClusterInfo;

    public static void main(String[] args) {

        if(args.length < 4) {
            System.err.println("Nr of arguments low");
            return;
        }

        mcastAddr = args[0];
        mcastPort = Integer.parseInt(args[1]);
        nodeId = args[2];
        storePort = Integer.parseInt(args[3]);

        mcChannel = new MulticastChannel(mcastAddr, mcastPort);

        log = new ArrayList<String>();
        sentClusterInfo = new ArrayList<>();

        int cores = Runtime.getRuntime().availableProcessors();

        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(cores);

        loadCounter();

        System.out.println(counter);
        
        executor.execute(new ProtocolReceiver(storePort));
        System.out.println("Potocol channel open");

        
        
        executor.execute(mcChannel);
        System.out.println("MCast Channel open");

        Runtime.getRuntime().addShutdownHook(new Thread(Store::saveCounter));

        

        //ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        //scheduled.scheduleAtFixedRate(new MulticastReceiver(), 0, 1, TimeUnit.SECONDS);
    }


    public static void addLogEntry(String nodeId, String counter) {
        
        for(int i = 0; i < log.size(); i++) {
            String[] nodeInfo = log.get(i).split(" ");

            if(nodeId.equals(nodeInfo[0])) {
                log.set(i, nodeId + " " + counter);
                return;
            }
        }

        
        log.add(nodeId + " " + counter);
        
    }


    public static String[] getLogs() {
        String[] logs = new String[32];

        int logSize = log.size();

        int i = 0;

        if(logSize > 32) {
            i = logSize - 32;
        }

        int j = 0;

        for(; i < log.size(); i++) {
            logs[j] = log.get(i);
            j++;
        }

        return logs;
    }


    public static String[] getNodes() {
        String[] nodes = new String[32];
        int j = 0;
        return nodes;
        /**for(int i = 0; i < log.size(); i++) {
            String[] nodeInfo = log.get(i).split(" ");
            int counter = Integer.parseInt(nodeInfo[1]);
            
            if(counter % 2 == 0) {
                nodes[j] = nodeInfo[0];
                j++;
            }
        }

        return nodes;*/
    }


    private static void saveCounter() {
        try {
            String filename = "Nodes/"+nodeId + "/counter";
            
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
            String filename = "Nodes/"+nodeId + "/counter";

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
            System.out.println("Integer class not found");
            c.printStackTrace();
        }
    }
}