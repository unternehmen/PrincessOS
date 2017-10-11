package main;

/**
 * This class represents a simulated CPU. This class's thread must be started
 * with start().
 *
 * @author Charlie Murphy
 */
public class CPU implements Runnable {

    /**
     * Holds the result of a single execution.
     */
    public class ExecutionResult {

        public ProcessImage process;
        public int nextPC;
        public PCB.ProcessState state;
        public int workNeeded;
        public boolean flagged;

        /**
         * Constructs a result for an execution.
         *
         * @param nextPC The position of the process to resume when it is
         * executed again.
         * @param state The new state of the process. Could be "wait" or
         * "ready".
         * @param workNeeded The amount of bubble sorts remaining on the current
         * instruction.
         */
        public ExecutionResult(ProcessImage process, int nextPC, PCB.ProcessState state, int workNeeded) {
            this.process = process;
            this.nextPC = nextPC;
            this.state = state;
            this.workNeeded = workNeeded;
            this.flagged = false;
        }

        public void setFlagged() {
            this.flagged = true;
        }

    }

    private boolean busyOrNot;
    public int timeSlice;
    private boolean shouldEnd;

    private ProcessImage currentProcess;
    private int numQuanta;
    private ExecutionResult executionResult;

    /**
     * Constructs a CPU.
     *
     * @param timeSlice the slice of time given to a burst
     */
    public CPU(int timeSlice) {
        this.timeSlice = timeSlice;
        this.busyOrNot = false;
        this.shouldEnd = false;
        this.currentProcess = null;
        this.numQuanta = -1;
        this.executionResult = null;
    }

    /**
     * Starts the CPU thread.
     */
    public void start() {
        new Thread(this).start();
    }

    /*
     Return executionResult process state for use in OS class
     with regard to determining proper queue placement
     -Brody
     */
    public PCB.ProcessState getProcessState() {
        return executionResult.state;
    }

    /*
     Return processImage ID for updating process_Table in OS
     -Brody
     */
    public int getProcessID() {
        return currentProcess.getPCB_ID();
    }

    /*
     Return process image for updating appropriate queues in OS
     */
    public ProcessImage getProcessImage() {
        return currentProcess;
    }

    /**
     * Execute a process until either it finishes or we reach the time slice
     * limit.
     *
     * @param p the process to execute
     * @param numQuanta the number of time quanta to give the process
     * @return the result of the execution
     */
    public synchronized boolean execute(ProcessImage p, int numQuanta) {
        if (!isBusy()) {
            this.currentProcess = p;
            this.numQuanta = numQuanta;
            setBusy(true);
            return true;
        }
        return false;
    }

    /**
     * @return whether the CPU is busy
     */
    public synchronized boolean isBusy() {
        return busyOrNot;
    }

    /**
     * Sets whether the CPU is busy.
     */
    private synchronized void setBusy(boolean busy) {
        busyOrNot = busy;
    }

    /**
     * Tells the CPU thread to end.
     */
    public synchronized void end() {
        shouldEnd = true;
    }

    /**
     * @return whether the CPU thread should end.
     */
    private synchronized boolean getShouldEnd() {
        return shouldEnd;
    }

    /**
     * Sets the execution result of the most recently performed execution.
     */
    private synchronized void setExecutionResult(ExecutionResult result) {
        this.executionResult = result;
    }

    /**
     * @return the result of the last execution.
     */
    public synchronized ExecutionResult getExecutionResult() {
        return this.executionResult;
    }

    /**
     * The thread's execution body.
     */
    @Override
    public void run() {
        // Should we stop?
        while (!getShouldEnd()) {
            // Has the OS given us a process?
            if (isBusy()) {
                // Yes, we have been given a process.
                // So let's execute it
                int pc = currentProcess.getProgramCounter();
                int instruction = currentProcess.getInstructionAt(pc);
                int workProgress = currentProcess.getWorkProgress();

                if (numQuanta == -1) {
                    // numQuanta is -1, that means we can take all the time quanta we need
                    while (workProgress < instruction) {
                        BubbleSort.onRandomData(1000);
                        workProgress++;
                    }

                    // We finished the instruction, so switch to I/O waiting state
                    if (pc == currentProcess.getCodeLength() - 1) {
                        setExecutionResult(new ExecutionResult(currentProcess, pc + 1, PCB.ProcessState.TERMINATED, 0));
                    } else {
                        setExecutionResult(new ExecutionResult(currentProcess, pc + 1, PCB.ProcessState.WAITING, 0));
                    }
                } else {
                    // We're only allowed to work for a certain number of time quanta
                    for (int i = 0; i < numQuanta && workProgress < instruction; i++) {
                        // Do work
                        if (workProgress < instruction) {
                            BubbleSort.onRandomData(1000);
                            workProgress++;
                        }
                    }

                    // Did we finish the instruction?
                    if (workProgress == instruction) {
                        // Did we finish the whole program?
                        if (pc == currentProcess.getCodeLength() - 1) {
                            setExecutionResult(new ExecutionResult(currentProcess, pc + 1, PCB.ProcessState.TERMINATED, 0));
                        } else {
                            setExecutionResult(new ExecutionResult(currentProcess, pc + 1, PCB.ProcessState.WAITING, 0));
                        }
                    } else {
                        setExecutionResult(new ExecutionResult(currentProcess, pc, PCB.ProcessState.READY, workProgress));
                    }
                }
                currentProcess = null;
                setBusy(false);
            }
        }
    }
}
