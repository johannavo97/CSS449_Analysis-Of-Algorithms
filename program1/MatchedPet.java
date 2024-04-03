import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class MatchedPet {
    private int n; // Number of pairs
    private String[] people;
    private String[] pets;
    private int[][] peoplePreferences;
    private int[][] petPreferences;
    private int[] petPartner;
    private boolean[] matchedPets;

    public MatchedPet(String filename) throws FileNotFoundException {
        readFile(filename);
    }

    private void readFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        n = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        people = new String[n];
        pets = new String[n];
        peoplePreferences = new int[n][n];
        petPreferences = new int[n][n];

        for (int i = 0; i < n; i++) {
            people[i] = scanner.nextLine();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                peoplePreferences[i][j] = scanner.nextInt() - 1; // Adjusting index to 0-based
            }
            if (scanner.hasNextLine()) scanner.nextLine(); // Consume newline
        }

        for (int i = 0; i < n; i++) {
            pets[i] = scanner.nextLine();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                petPreferences[i][j] = scanner.nextInt() - 1; // Adjusting index to 0-based
            }
            if (scanner.hasNextLine()) scanner.nextLine(); // Consume newline
        }
        scanner.close();
    }

    public void findMatches() {
        petPartner = new int[n];
        matchedPets = new boolean[n];
        Arrays.fill(petPartner, -1);
        Arrays.fill(matchedPets, false);

        for (int i = 0; i < n; i++) {
            while (petPartner[i] == -1) {
                for (int j = 0; j < n; j++) {
                    int preferredPet = peoplePreferences[i][j];
                    if (!matchedPets[preferredPet]) {
                        // If pet is free, make a match
                        petPartner[i] = preferredPet;
                        matchedPets[preferredPet] = true;
                        break;
                    } else {
                        // Check if the pet prefers this person over its current partner
                        int currentPartner = -1;
                        for (int k = 0; k < n; k++) {
                            if (petPartner[k] == preferredPet) {
                                currentPartner = k;
                                break;
                            }
                        }

                        boolean prefersNewPartner = false;
                        for (int rank : petPreferences[preferredPet]) {
                            if (rank == i) {
                                prefersNewPartner = true;
                                break;
                            } else if (rank == currentPartner) {
                                break;
                            }
                        }

                        if (prefersNewPartner) {
                            petPartner[currentPartner] = -1; // Unmatch the current partner
                            petPartner[i] = preferredPet; // Match with the new person
                            break;
                        }
                    }
                }
            }
        }
    }

    public void printMatches() {
        for (int i = 0; i < n; i++) {
            System.out.println(people[i] + " / " + pets[petPartner[i]]);
        }
    }

    public static void main(String[] args) {
        try {
            MatchedPet MatchedPet = new MatchedPet("program1data.txt");
            MatchedPet.findMatches();
            MatchedPet.printMatches();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
