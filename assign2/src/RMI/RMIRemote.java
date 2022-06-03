package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIRemote extends Remote {
    void join() throws RemoteException;
    void leave() throws RemoteException;
    void printStorage() throws RemoteException;
    void printMembershipLog() throws RemoteException;
}
