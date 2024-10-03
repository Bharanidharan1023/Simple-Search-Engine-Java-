package search;

import java.io.*;
import java.util.*;

public class InvertedIndex {
    private final Map<String, List<Integer>> index = new HashMap<>();
    private final List<String> peopleList = new ArrayList<>();
    private static final String FILE_NAME = "text.txt";

    // Constructor to build the inverted index at the start
    public InvertedIndex() {
        loadPeopleFromFile();
        buildInvertedIndex();
    }

    // Method to load people data from file
    private void loadPeopleFromFile() {
        File file = new File(FILE_NAME);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("The file text.txt does not exist. Please create the file and add people data.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                peopleList.add(line);  // Add each line to the list
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }

    // Method to build the inverted index
    private void buildInvertedIndex() {
        for (int i = 0; i < peopleList.size(); i++) {
            String[] words = peopleList.get(i).split("\\s+");
            for (String word : words) {
                word = word.toLowerCase();
                index.putIfAbsent(word, new ArrayList<>());
                index.get(word).add(i);
            }
        }
    }

    // Method to print all people
    public void printAllPeople() {
        if (peopleList.isEmpty()) {
            System.out.println("No data available.");
        } else {
            System.out.println("=== List of people ===");
            for (String person : peopleList) {
                System.out.println(person);
            }
        }
    }

    // Method to search with strategy: ALL, ANY, NONE
    public void searchWithStrategy(String strategy, String query) {
        String[] queryWords = query.toLowerCase().split("\\s+");
        Set<Integer> resultIndexes = new HashSet<>();

        switch (strategy.toUpperCase()) {
            case "ALL":
                resultIndexes = searchAll(queryWords);
                break;
            case "ANY":
                resultIndexes = searchAny(queryWords);
                break;
            case "NONE":
                resultIndexes = searchNone(queryWords);
                break;
            default:
                System.out.println("Invalid strategy!");
                return;
        }

        if (resultIndexes.isEmpty()) {
            System.out.println("No matching people found.");
        } else {
            System.out.println(resultIndexes.size() + " persons found:");
            for (int index : resultIndexes) {
                System.out.println(peopleList.get(index));
            }
        }
    }

    // Search for lines containing ALL words
    private Set<Integer> searchAll(String[] queryWords) {
        Set<Integer> resultSet = new HashSet<>();
        boolean firstWord = true;

        for (String word : queryWords) {
            List<Integer> indexes = index.getOrDefault(word, Collections.emptyList());
            if (firstWord) {
                resultSet.addAll(indexes);  // Initialize the set with the first word's result
                firstWord = false;
            } else {
                resultSet.retainAll(indexes);  // Keep only common indexes
            }
        }

        return resultSet;
    }

    // Search for lines containing ANY word
    private Set<Integer> searchAny(String[] queryWords) {
        Set<Integer> resultSet = new HashSet<>();

        for (String word : queryWords) {
            List<Integer> indexes = index.getOrDefault(word, Collections.emptyList());
            resultSet.addAll(indexes);  // Add all indexes where the word occurs
        }

        return resultSet;
    }

    // Search for lines containing NONE of the words
    private Set<Integer> searchNone(String[] queryWords) {
        Set<Integer> resultSet = new HashSet<>();
        for (int i = 0; i < peopleList.size(); i++) {
            resultSet.add(i);  // Start by assuming all indexes are valid
        }

        for (String word : queryWords) {
            List<Integer> indexes = index.getOrDefault(word, Collections.emptyList());
            resultSet.removeAll(indexes);  // Remove indexes where the word occurs
        }

        return resultSet;
    }
}
