import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

public class MutexManagerImpl extends UnicastRemoteObject implements MutexManager {
    private class Request {
        int processId;
        int[] clock;

        public Request(int processId, int[] clock) {
            this.processId = processId;
            this.clock = clock;
        }
    }

    private Queue<Request> requestQueue;
    private int currentProcessInCriticalSection = -1;

    public MutexManagerImpl() throws RemoteException {
        requestQueue = new PriorityQueue<>(new Comparator<Request>() {
            @Override
            public int compare(Request r1, Request r2) {
                // Compare based on Lamport's algorithm
                for (int i = 0; i < r1.clock.length; i++) {
                    if (r1.clock[i] != r2.clock[i]) {
                        return r1.clock[i] - r2.clock[i];
                    }
                }
                return r1.processId - r2.processId;
            }
        });
    }

    @Override
    public synchronized void requestEntry(int processId, int[] clock) throws RemoteException {
        requestQueue.add(new Request(processId, clock));
        checkQueue();
    }

    @Override
    public synchronized void releaseEntry(int processId) throws RemoteException {
        if (currentProcessInCriticalSection == processId) {
            currentProcessInCriticalSection = -1;
            checkQueue();
        }
    }

    private void checkQueue() throws RemoteException {
        if (currentProcessInCriticalSection == -1 && !requestQueue.isEmpty()) {
            Request nextRequest = requestQueue.poll();
            currentProcessInCriticalSection = nextRequest.processId;
            // Send REPLY message to the process, granting it access to critical section
            System.out.println("Granting access to process: " + currentProcessInCriticalSection);
        }
    }
}
