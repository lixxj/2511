package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;
        
        // Use a for loop to traverse the list of numbers and compute the sum
        for (int i : nums) result += i;
        // Use nums.length to get the length of the array
        result /= nums.length;

        return result;
    }

    public static void main(String[] args) {
        // Initialise an array of the numbers 1 - 6 (integers)
        int[] nums = {1, 2, 3, 4, 5, 6};
        // Create an instance of the class Average
        Average a = new Average();
        // Invoke the method computeAverage() and assign the result to a variable
        float average = a.computeAverage(nums);
        // print the variable in the format: The average is {average}
        System.out.println("The average is " + average);
    }
}
