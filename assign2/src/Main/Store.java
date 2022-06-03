package Main;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;

import Membership.*;
import RMI.RMIRemote;
import Storage.*;
import Utils.Util;

public class Store implements RMIRemote {

    public static String mcastAddr;
    public static int mcastPort;
    public static String nodeId;
    public static int storePort;
    
    public static ScheduledThreadPoolExecutor executor;
    

    public static int counter;

    public static ArrayList<String> log;

    public static ArrayList<String> sentClusterInfo;

    public static Bucket bucket;

    public static ArrayList<String> currentNodes;

    private Store(String mcastA, int mcastP, String node, int port) {
        mcastAddr = mcastA;
        mcastPort = mcastP;
        nodeId = node;
        storePort = port;
    }

    public static void main(String[] args) {

        if(args.length != 4) {
            System.err.println("USAGE: java <mcast_addr> <mcast_port> <node_id> <access_point>");
            return;
        }

        Store obj = new Store(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        String accessPoint = args[3];

        try {
            RMIRemote stub = (RMIRemote) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind(accessPoint, stub);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        sentClusterInfo = new ArrayList<>();
        currentNodes = new ArrayList<>();
        bucket =  new Bucket();

        int cores = Runtime.getRuntime().availableProcessors();
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(cores);

        loadCounter();
        loadLog();

        if(isMember()) {

            executor.execute(new ProtocolReceiver(storePort));
            System.out.println(" > TCP Potocol Channel Open on: " + Integer.toString(storePort));

            executor.execute(new MulticastChannel(mcastAddr, mcastPort));
            System.out.println(" > MultiCast Channel Open on: " + mcastAddr + ":" + Integer.toString(mcastPort));

            executor.scheduleWithFixedDelay(new SetCurrentNodes(), 1, 10, TimeUnit.SECONDS);
            
            Store.executor.scheduleWithFixedDelay(new CastMembershipInfo(Store.mcastAddr, Store.mcastPort, Store.getLogs()), 3, 1, TimeUnit.SECONDS);

            Store.executor.scheduleWithFixedDelay(new Stabilizer(), 10, 5, TimeUnit.SECONDS);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(Store::saveCounter));
        Runtime.getRuntime().addShutdownHook(new Thread(Store::saveLog));
    }

    public static boolean isMember() {
        if(Store.counter % 2 == 0)
            return true;

        return false;
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

        for(; i < log.size(); i++) {
            logs.add(log.get(i));
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

    private static void saveLog() {
        try {
            String filename = "Nodes/" + nodeId + "/log.txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            PrintWriter out = new PrintWriter(filename);

            for(String logLine : log) {
                out.println(logLine);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadLog() {
        try {
            String filename = "Nodes/" + nodeId + "/log.txt";
            File file = new File(filename);
            Store.log = new ArrayList<String>();
            if (!file.exists()) {
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                while((line = br.readLine()) != null) {
                    Store.log.add(line);
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }


    private static void saveCounter() {
        try {
            String filename = "Nodes/" + nodeId + "/counter.txt";
            
            File file = new File(filename);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(filename);
            out.println(Integer.toString(Store.counter));
            out.close();

            for(String node:Store.currentNodes) {
                System.out.println(node);
            }
            
            Store.executor.shutdown();

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private static void loadCounter() {
        try {
            String filename = "Nodes/" + nodeId + "/counter.txt";

            File file = new File(filename);
            if (!file.exists()) {
                counter = -1;
                return;
            }
            
            Scanner sc = new Scanner(file);

            Store.counter = Integer.parseInt(sc.nextLine());

            sc.close();

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void join() throws RemoteException {
        int counterAux = Store.counter+1;

        if(counterAux % 2 == 0) {
            Store.counter += 1;

            Store.executor.execute(new MulticastChannel(mcastAddr, mcastPort));
            System.out.println("> MultiCast Channel Open on: " + mcastAddr + ":" + Integer.toString(mcastPort));
            
            Store.executor.execute(new Membership.TCPChannel(Store.mcastPort));
            
            System.out.println("> TCP Membership Channel Open on: " + Integer.toString(Store.mcastPort));
            
            String message = "JOIN " + Store.nodeId + " " + Integer.toString(Store.mcastPort) + " " + Integer.toString(Store.counter) + "\r\n\r\n";
            Store.executor.execute(new SendMessage(message));
            
        }
        
    }

    @Override
    public void leave() throws RemoteException {
        int counterAux2 = Store.counter+1;
                
        if(counterAux2 % 2 != 0) {
            Store.counter += 1;

            String successor = Util.getSuccesor();
            System.out.println("> Successor: " + successor);

            ProtocolReceiver.sendMessage(successor, Util.getNodePort(successor), Store.bucket.getKeysValues());
            Store.bucket.deleteAll();

            String message = "LEAVE " + Store.nodeId + " " + Integer.toString(Store.mcastPort) + " " + Integer.toString(Store.counter) + "\r\n\r\n";

            Store.executor.execute(new SendMessage(message));
        }
        
    }

    @Override
    public void printStorage() {
        Store.bucket.printBucket();
        
    }

    @Override
    public void printMembershipLog() throws RemoteException {
        printLog();
        
    }
}