import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProcessImpl extends UnicastRemoteObject implements ProcessInterface {
    private VectorClock clock;
    private MutexManager mutexManager;
    private boolean inCriticalSection = false;
    private int processId;

    public ProcessImpl(int processId, MutexManager mutexManager, int totalProcesses) throws RemoteException {
        this.processId = processId;
        this.mutexManager = mutexManager;
        this.clock = new VectorClockImpl(totalProcesses, processId);
    }

    @Override
    public synchronized void requestCriticalSection() throws RemoteException {
        clock.incrementClock();
        System.out.println("Process " + processId + " requesting critical section.");
        mutexManager.requestEntry(processId, clock.getClock());
    }

    @Override
    public synchronized void releaseCriticalSection() throws RemoteException {
        if (inCriticalSection) {
            System.out.println("Process " + processId + " releasing critical section.");
            inCriticalSection = false;
            mutexManager.releaseEntry(processId);
        }
    }

    @Override
    public VectorClock getClock() throws RemoteException {
        return clock;
    }

    // This method would be called when MutexManager grants access
    public synchronized void enterCriticalSection() throws RemoteException {
        inCriticalSection = true;
        System.out.println("Process " + processId + " entering critical section.");
        // Simulate work in critical section
        try {
            Thread.sleep(1000); // Simulates work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        releaseCriticalSection();
    }
}
