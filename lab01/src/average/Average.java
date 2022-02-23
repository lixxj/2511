package average;

import java.util.Arrays;
import java.util.Scanner;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    
    public float computeAverage(int[] nums) {
        return Arrays.stream(nums).sum() / nums.length;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter thy numeros:");
        // int n = sc.nextInt();
        int[] array = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        // System.out.println("Enter the numbers: ");  

        // for (int i = 0; i < n; i++) {
        //     array[i] = sc.nextInt();
        // }
        
        Average a = new Average();
        System.out.format("The average is: %.3f", a.computeAverage(array));
        sc.close();
    }
}
