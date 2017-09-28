package scheduling;

import main.ProcessImage;

/**
 * The interface that all schedulers follow.
 * @author Charlie Murphy
 */
public interface Scheduler {
    public ProcessImage nextProcess();
}
