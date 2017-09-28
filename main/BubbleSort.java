package main;

import java.util.Random;

/**
 * Implements a nifty bubble sort!
 * @author Charlie Murphy
 */
public class BubbleSort {
    /**
     * Bubble sort on random data.
     * Useful for intentionally slowing down execution.
     * 
     * @param numItems The number of data items to generate and sort.
     */
    public static void onRandomData(int numItems) {
        Random rand = new Random();
        
        // Fill an array with random integers.
        int[] data = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            data[i] = rand.nextInt();
        }
        
        // Sort it!
        sort(data);
    }
    
    /**
     * Sorts an array of integers using the bubble sort algorithm.
     * @param data the array to sort
     * @warning This method modifies the data in-place.
     */
    public static void sort(int[] data) {
        boolean goAgain = true;
        while (goAgain) {
            goAgain = false;
            
            for (int i = 0; i <= data.length - 2; i++) {
                if (data[i] > data[i+1]) {
                    int temp = data[i];
                    data[i] = data[i+1];
                    data[i+1] = temp;
                    goAgain = true;
                }
            }
        }
    }
}
