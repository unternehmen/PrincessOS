package scheduling;

import java.util.ArrayList;
import java.util.PriorityQueue;
import main.ProcessImage;

/**
 * Implements static priority scheduling.
 * CPU time is granted to processes with a high priority.
 * 
 * @author Charlie Murphy
 */
public class StaticPriority implements Scheduler {
    private ArrayList<ProcessImage> readyQueue;
    
    public StaticPriority(ArrayList<ProcessImage> readyQueue) {
        this.readyQueue = readyQueue;
    }
    
    /**
     * @return the next process that deserves CPU time.
     */
    @Override
    public ProcessImage nextProcess() {
        PriorityQueue<ProcessImage> heap = new PriorityQueue<ProcessImage>();
        
        for (ProcessImage p : readyQueue) {
            heap.add(p);
        }
        
        return heap.remove();
    }
}
