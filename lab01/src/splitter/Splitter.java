package splitter;

import java.util.Scanner;

public class Splitter {
    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        Scanner sc = new Scanner(System.in);
        String[] split_message = sc.nextLine().split("\\s");
        for (String x : split_message) {
            System.out.println(x);
        }
        sc.close();
    }
}
