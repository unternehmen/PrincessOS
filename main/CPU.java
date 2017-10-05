package main;

/**
 * This class represents a simulated CPU.
 * @author Charlie Murphy
 */
public class CPU {
    public boolean busyOrNot;
    public int timeSlice;
    
    /**
     * Holds the result of a single execution.
     */
    public class ExecutionResult {
        public int nextPC;
        public PCB.ProcessState state;
        public int workNeeded;
        
        /**
         * Constructs a result for an execution.
         * @param nextPC  The position of the process to resume when it is executed again.
         * @param state   The new state of the process.  Could be "wait" or "ready".
         * @param workNeeded The amount of bubble sorts remaining on the current instruction.
         */
        public ExecutionResult(int nextPC, PCB.ProcessState state, int workNeeded) {
            this.nextPC = nextPC;
            this.state = state;
            this.workNeeded = workNeeded;
        }
    }
    
    /**
     * Constructs a CPU.
     * @param timeSlice the slice of time given to a burst
     */
    public CPU(int timeSlice) {
        this.timeSlice = timeSlice;
        this.busyOrNot = false;
    }
    
    /**
     * Execute a process until either it finishes or we reach the time slice limit.
     * @param p the process to execute
     * @param numQuanta the number of time quanta to give the process
     * @return the result of the execution
     */
    public ExecutionResult execute(ProcessImage p, int numQuanta) {
        int pc = p.getProgramCounter();
        int instruction = p.getInstructionAt(pc);
        int workProgress = p.getWorkProgress();
        
        if (numQuanta == -1) {
            // numQuanta is -1, that means we can take all the time quanta we need
            while (workProgress < instruction) {
                BubbleSort.onRandomData(500);
                workProgress++;
            }

            // We finished the instruction, so switch to I/O waiting state
            return new ExecutionResult(pc + 1, PCB.ProcessState.WAITING, 0);
        } else {
            // We're only allowed to work for a certain number of time quanta
            for (int i = 0; i < numQuanta && workProgress < instruction; i++) {
                // Do work
                if (workProgress < instruction) {
                    BubbleSort.onRandomData(500);
                    workProgress++;
                }
            }

            // Did we finish the instruction?
            if (workProgress == instruction) {
                // Did we finish the whole program?
                if (pc == p.getCodeLength()) {
                    return new ExecutionResult(pc + 1, PCB.ProcessState.TERMINATED, 0);
                } else {
                    return new ExecutionResult(pc + 1, PCB.ProcessState.WAITING, 0);
                }
            } else {
                return new ExecutionResult(pc, PCB.ProcessState.READY, workProgress);
            }
        }
    }
    
    /**
     * @return whether the CPU is busy
     */
    public boolean isBusy() {
        return busyOrNot;
    }
}
