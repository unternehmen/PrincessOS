package main;

/**
 * This class represents an I/O device.
 * @author Charlie Murphy
 */
public class IODevice {
    private boolean busyOrNot;
    
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
            if (pc == p.getCodeLength() - 1) {
                return new ExecutionResult(pc + 1, PCB.ProcessState.TERMINATED, 0);
            } else {
                return new ExecutionResult(pc + 1, PCB.ProcessState.READY, 0);
            }
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
                if (pc == p.getCodeLength() - 1) {
                    return new ExecutionResult(pc + 1, PCB.ProcessState.TERMINATED, 0);
                } else {
                    return new ExecutionResult(pc + 1, PCB.ProcessState.READY, 0);
                }
            } else {
                return new ExecutionResult(pc, PCB.ProcessState.WAITING, workProgress);
            }
        }
    }
    
    /**
     * @return whether the I/O device is busy or not
     */
    public boolean isBusy() {
        return busyOrNot;
    }
}
