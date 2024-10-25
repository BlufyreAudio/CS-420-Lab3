import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class VectorClockImpl extends UnicastRemoteObject implements VectorClock {
    private int[] clock;
    private int processId;

    public VectorClockImpl(int totalProcesses, int processId) throws RemoteException {
        this.clock = new int[totalProcesses];
        this.processId = processId;
        Arrays.fill(this.clock, 0);
    }

    @Override
    public int[] getClock() throws RemoteException {
        return clock;
    }

    @Override
    public void incrementClock() throws RemoteException {
        clock[processId]++;
    }

    @Override
    public void updateClock(int[] newClock) throws RemoteException {
        for (int i = 0; i < clock.length; i++) {
            clock[i] = Math.max(clock[i], newClock[i]);
        }
    }
}
