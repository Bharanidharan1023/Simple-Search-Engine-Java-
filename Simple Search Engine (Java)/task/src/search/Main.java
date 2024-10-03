package search;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InvertedIndex index = new InvertedIndex();  // Create instance of InvertedIndex

        while (true) {
            System.out.println("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit");
            int option = scanner.nextInt();
            scanner.nextLine();  // Clear the newline left by nextInt()

            switch (option) {
                case 0: {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                case 1: {
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    String strategy = scanner.nextLine();
                    System.out.println("Enter a name or email to search all suitable people:");
                    String query = scanner.nextLine();
                    index.searchWithStrategy(strategy, query);  // Search using the selected strategy
                    break;
                }
                case 2: {
                    index.printAllPeople();  // Print all people from the file
                    break;
                }
            }
        }
    }
}
