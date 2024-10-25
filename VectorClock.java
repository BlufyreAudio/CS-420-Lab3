import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VectorClock extends Remote {
    int[] getClock() throws RemoteException;
    void incrementClock() throws RemoteException;
    void updateClock(int[] newClock) throws RemoteException;
}