package main;

/**
 * A PCB represents a program's state on the operating system.
 */
public class PCB {
    /** The ID of the process */
    public int id;
    /** The priority of the process (0 = lowest, etc.) */
    public int priority;
    /** The arrival time (0 = earliest, 1 = second earliest, etc.) */
    public int arrivalTime;
    /** The state of the process */
    public ProcessState state;
    /** The position of the next instruction */
    public int programCounter;
    /**
     * The amount of work completed on the current burst.
     *
     * This should be managed entirely by the OS via
     * {@link ProcessImage#setWorkProgress} and
     * {@link ProcessImage#getWorkProgress}.
     */
    public int workProgress;

    /** The possible states of the process */
    public enum ProcessState {
        /** The process is being created and is not yet running. */
        NEW,
        /** The process is waiting to be scheduled on a CPU. */
        READY,
        /** The process is running on a CPU. */
        RUNNING,
        /** The process is waiting for its I/O request to finish. */
        WAITING,
        /** The process has completed. */
        TERMINATED
    };


    /**
     * Create a new PCB.
     *
     * @param id           the process ID (see {@link #id})
     * @param priority     the process priority (see {@link #priority})
     * @param arrivalTime  the arrival time (see {@link #arrivalTime})
     * @return  the new PCB
     * @throws IllegalArgumentException  if id or priority < 0
     */
    public PCB(int id, int priority, int arrivalTime)
    {
        if (id < 0)
            throw new IllegalArgumentException("id < 0");
        else if (priority < 0)
            throw new IllegalArgumentException("priority < 0");

        this.id = id;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.state = ProcessState.NEW;
        this.workProgress = 0;
    }
}
