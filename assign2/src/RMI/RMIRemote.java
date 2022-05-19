package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIRemote extends Remote {
    void join() throws RemoteException;
    void leave() throws RemoteException;  
    void put(String key, String value) throws RemoteException;
    void get(String key) throws RemoteException;
    void delete(String key) throws RemoteException;  
}
