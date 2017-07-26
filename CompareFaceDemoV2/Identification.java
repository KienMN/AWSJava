/**
 * Created by kienmaingoc on 6/30/17.
 */
public class Identification {

    boolean status = false;

    public synchronized void waitStatusChange() {
        if (!status) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void changeStatus() {
        status = true;
        System.out.println("change status successfully!");
        notifyAll();
    }

    public synchronized void resetStatus() {
        status = false;
        notifyAll();
    }

}
