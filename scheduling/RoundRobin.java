package scheduling;

import main.ProcessImage;
import common.Pair;
import java.util.ArrayList;


public class RoundRobin
{
    private int quantumLength;
    private ArrayList<ProcessImage> readyQueue;
    
    public RoundRobin(ArrayList<ProcessImage> readyQueue, int quantumLength)
    {
        this.readyQueue = readyQueue;
        this.quantumLength = quantumLength;
    }
    
    public Pair<ProcessImage, Integer> nextProcess()
    {
        return new Pair<>(readyQueue.get(0), quantumLength);
    }
}
