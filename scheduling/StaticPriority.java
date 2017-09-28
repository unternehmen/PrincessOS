package scheduling;

import main.ProcessImage;

/**
 * Implements static priority scheduling.
 * CPU time is granted to processes with a high priority.
 * 
 * @author Charlie Murphy
 */
public class StaticPriority implements Scheduler {
    /**
     * @return the next process that deserves CPU time. 
     */
    @Override
    public ProcessImage nextProcess() {
        return null;
    }
}
