/**
 * This class represents an I/O device.
 * @author Charlie Murphy
 */
public class IODevice {
    private boolean busyOrNot;
    
    /**
     * Constructs an I/O device.
     */
    public IODevice() {
        busyOrNot = false;
    }
    
    /**
     * Simulates an expensive I/O operation.
     * @param IO_burst Roughly the amount of time to spend on the burst.
     * @return the string "ready"
     */
    public String execute(int IO_burst) {
        busyOrNot = true;
        for (int i = 0; i < IO_burst; i++) {
            BubbleSort.onRandomData(200);
        }
        return "ready";
    }
    
    /**
     * @return whether the I/O device is busy or not
     */
    public boolean isBusy() {
        return busyOrNot;
    }
}
