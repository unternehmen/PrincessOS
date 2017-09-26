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
        
        /**
         * Constructs a result for an execution.
         * @param nextPC  The position of the process to resume when it is executed again.
         * @param state   The new state of the process.  Could be "wait" or "ready".
         */
        public ExecutionResult(int nextPC, PCB.ProcessState state) {
            this.nextPC = nextPC;
            this.state = state;
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
     * @return the result of the execution
     */
    public ExecutionResult execute(ProcessImage p) {
        int pc = p.processControlBlock.programCounter;
        int numTimes = p.getInstructionAt(pc);
        
        for (int i = 0; i < numTimes; i++) {
            BubbleSort.onRandomData(500);
        }
        
        if (pc == p.code.length() - 1)
            return new ExecutionResult(pc, PCB.ProcessState.TERMINATED);
        else if (numTimes >= timeSlice)
            return new ExecutionResult(pc + 1, PCB.ProcessState.READY);
        
        return new ExecutionResult(pc + 1, PCB.ProcessState.WAITING);
    }
    
    /**
     * @return whether the CPU is busy
     */
    public boolean isBusy() {
        return busyOrNot;
    }
}
