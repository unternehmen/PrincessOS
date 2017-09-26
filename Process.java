/**
 * A Process represents a program running on the operating system.
 */
public class Process {
    /** The ID of the process */
    public int id;
    /** The priority of the process (0 = lowest, etc.) */
    public int priority;
    /** The CPU-I/O burst sequence */
    public String code;
    /** The state of the process */
    public ProcessState state;

    /** The possible states of a Process */
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
     * Create a new Process.
     *
     * @param id        the process ID (see {@link #id})
     * @param priority  the process priority (see {@link #priority})
     * @param code      the CPU-I/O burst sequence (see {@link #code})
     * @return  the new Process
     * @throws IllegalArgumentException  if priority or code < 0
     */
    public Process(int id, int priority, String code)
    {
        if (id < 0)
            throw new IllegalArgumentException("id < 0");
        else if (priority < 0)
            throw new IllegalArgumentException("priority < 0");

        this.id = id;
        this.priority = priority;
        this.code = code;
        this.state = ProcessState.NEW;
    }
}
