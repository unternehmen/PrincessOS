/**
 * A ProcessImage represents a process and its executable code.
 */
public class ProcessImage
{
    /** The PCB linked to this process */
    public PCB processControlBlock;

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
    public String code;

    /**
     * Create a new ProcessImage.
     *
     * @param code  the code string (see {@link #code})
     * @return  the new ProcessImage
     */
    public ProcessImage(int id, int priority,
                        int arrivalTime,
                        String code)
    {
        this.processControlBlock = new PCB(id, priority, arrivalTime);
        this.code = code;
    }
}
