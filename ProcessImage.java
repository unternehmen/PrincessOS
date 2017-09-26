/**
 * A ProcessImage represents a program running on the operating system.
 */
public class ProcessImage {
    /** The process control block (PCB) of this process */
    public PCB PCBData;
    /** The ID of the process */
    public int id;
    /** The priority of the process (0 = lowest, etc.) */
    public int priority;
    /** The CPU-I/O burst sequence */
    public String code;

    /**
     * Create a new ProcessImage.
     *
     * @param id        the process ID (see {@link #id})
     * @param priority  the process priority (see {@link #priority})
     * @param code      the CPU-I/O burst sequence (see {@link #code})
     * @return  the new ProcessImage
     * @throws IllegalArgumentException  if priority, or code < 0
     */
    public ProcessImage(int id, int priority, String code)
    {
        if (id < 0)
            throw new IllegalArgumentException("id < 0");
        else if (priority < 0)
            throw new IllegalArgumentException("priority < 0");

        this.id = id;
        this.priority = priority;
        this.code = code;
    }
}
