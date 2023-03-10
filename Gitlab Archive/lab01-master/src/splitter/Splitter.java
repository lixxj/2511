package splitter;

import java.util.Scanner;

public class Splitter {

    public static void main(String[] args) {
        // Ask the user for a message
        System.out.println("Enter a message: ");
        
        // Read a line consisting of words separated by a space from System.in
        Scanner sc = new Scanner(System.in);

        // Print out the individual words in the string
        while (sc.hasNext()) System.out.println(sc.next());

        sc.close();
    }
}
