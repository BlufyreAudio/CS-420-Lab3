import java.rmi.Naming;

public class Main {
    public static void main(String[] args) {
        try {
            // Set up the RMI registry and bind MutexManager
            MutexManagerImpl mutexManager = new MutexManagerImpl();
            Naming.rebind("rmi://localhost/MutexManager", mutexManager);

            // Create 3 processes as an example
            for (int i = 0; i < 3; i++) {
                ProcessImpl process = new ProcessImpl(i, mutexManager, 3);
                Naming.rebind("rmi://localhost/Process" + i, process);
            }

            // Example: process 0 requesting the critical section
            ProcessInterface process0 = (ProcessInterface) Naming.lookup("rmi://localhost/Process0");
            process0.requestCriticalSection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
