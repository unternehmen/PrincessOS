package scheduling;

import common.Pair;
import main.ProcessImage;

/**
 * The interface that all schedulers follow.
 * @author Charlie Murphy
 */
public interface Scheduler {
    /**
     * Return which process to run next and how long it can run.
     * 
     * @return a Pair containing the next process to run
     *           and the duration (in bubble sorts) it may run;
     *           if the returned duration is -1, there is no limit
     *           to how long the process may run.
     */
    public Pair<ProcessImage, Integer> nextProcess();
}
