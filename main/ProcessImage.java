package main;

/**
 * A ProcessImage represents a process and its executable code.
 */
public class ProcessImage
{
    /** The PCB linked to this process */
    private PCB processControlBlock;

    /**
     * The code string of the process.
     *
     * Each character of the string is a number representing how many
     * times bubble sort must be executed to finish a CPU or I/O burst.
     * The first number represents a CPU burst, the second represents
     * an I/O burst, the third represents a CPU burst, and so on.
     *
     * For example, the following string:
     *
     *     "15298"
     *
     * This means the first CPU burst requires one bubble sort,
     * the first I/O burst requires five bubble sorts, the second
     * CPU burst requires two bubble sorts, and so on.
     */
    private String code;

    /** The time when the process was created */
    private long creationTime;

    /** The time the process took to reach its first I/O wait */
    private long responseTime;

    /** The time the process took to terminate */
    private long latency;

    /** Whether the process has waited yet */
    private boolean hasWaited;

    /**
     * Create a new ProcessImage.
     *
     * @param id           the process ID (see {@link PCB#id})
     * @param priority     the process priority
     *                       (see {@link PCB#priority})
     * @param arrivalTime  the arrival time
     *                       (see {@link PCB#arrivalTime})
     * @param code         the code string (see {@link #code})
     * @return  the new ProcessImage
     */
    public ProcessImage(int id, int priority,
                        int arrivalTime,
                        String code)
    {
        creationTime = System.currentTimeMillis();
        this.processControlBlock = new PCB(id, priority, arrivalTime);
        this.code = code;
        hasWaited = false;
    }

    /**
     * Get the instruction at the given index in the process' code.
     *
     * @param index  the index
     * @return  the instruction
     */
    public int getInstructionAt(int index)
    {
        if (index <= -1 || index > code.length()) {
            throw new IllegalArgumentException("index out of range");
        }

        return (int)(code.charAt(index) - '0');
    }

    /**
     * Return the number of instructions in the process' code.
     *
     * @return the number of instructions
     */
    public int getCodeLength()
    {
        return code.length();
    }

    /**
     * Return the program counter.
     *
     * @return  the program counter
     */
    public int getProgramCounter()
    {
        return processControlBlock.programCounter;
    }

    /**
     * Set the program counter.
     *
     * @param programCounter  the new program counter
     */
    public void setProgramCounter(int programCounter)
    {
        processControlBlock.programCounter = programCounter;
    }

    /**
     * Set the state of the process.
     *
     * You should run this whenever the state changes so that the
     * process can record latency, response time, and other statistics
     * related to its life cycle.
     * 
     * @param state the new state of the process
     */
    public void setState(PCB.ProcessState state)
    {
        if (state == PCB.ProcessState.WAITING) {
            if (!hasWaited) {
                long currentTime = System.currentTimeMillis();
                responseTime = currentTime - creationTime;
                hasWaited = true;
            }
        } else if (state == PCB.ProcessState.TERMINATED) {
            latency = System.currentTimeMillis() - creationTime;
        }

        processControlBlock.state = state;
    }

    /**
     * Set the amount of work needed to process the current burst.
     *
     * @param amount  the amount of work
     */
    public void setAmountOfWorkNeeded(int amount)
    {
        processControlBlock.amountOfWorkNeeded = amount;
    }

    /**
     * Return the amount of work needed to process the current burst.
     *
     * The amount of work must have previously been set with
     * {@link #setAmountOfWorkNeeded}.
     *
     * @return  the amount of work
     */
    public int getAmountOfWorkNeeded()
    {
        return processControlBlock.amountOfWorkNeeded;
    }
    
    /**
     * Sets the priority of the process.
     * @param priority the new priority of the process
     */
    public void setPriority(int priority)
    {
        processControlBlock.priority = priority;
    }
    
    /**
     * Returns the priority of the process.
     * @return the priority of the process
     */
    public int getPriority()
    {
        return processControlBlock.priority;
    }
}
