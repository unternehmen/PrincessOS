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
        
        // Bubble sort!!!
        boolean goAgain = true;
        while (goAgain) {
            goAgain = false;
            
            for (int i = 0; i <= numItems - 2; i++) {
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
