/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.*;

/**
 *
 * @author Brody
 */
public class Reporter {
    public Reporter(){
    }
    
    //Didn't want to type all of this for both the latency and
    //response lists 
    //This handles report for the mean, min, max, and std dev of the lists
    public void handleAll(ArrayList<Long> myList){
        double mean = 0, stdDev = 0; 
        long min, max;
        
        min = myList.get(0);
        max = myList.get(0);
        
        for(long l : myList){
            if(l < min){
                min = l;
            }
            else if(l > max){
                max = l;
            }
            mean += (double)l;
        }
        
        mean /= (double)myList.size();
        
        for(long l : myList){
		stdDev += (double)Math.pow((l - mean), 2);
	}

	stdDev /= (double)myList.size();
	stdDev = Math.sqrt(stdDev);
        
        System.out.println("Mean: " + mean + "\nMin: " + min 
                + "\nMax: " + max + "\nStandard Deviation: " + stdDev + "\n");
        
    }
}
