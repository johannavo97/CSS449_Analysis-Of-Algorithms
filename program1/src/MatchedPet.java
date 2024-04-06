import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The MatchedPet class is responsible for matching people with pets based on their preferences.
 * The data is read from a file named "program1data.txt".
 * Modified by: Johanna Vo
 * Date: 2024-04-01
 * Assignment: Program 1 - Gale-Shapley algorithm
 */
public class MatchedPet {

    /**
     * The main method of the MatchedPet class.
     * It reads the data from the file, performs the matching and prints the results.
     *
     * @param args command line arguments (not used)
     * @throws FileNotFoundException if the data file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("program1data.txt"));
        int n = Integer.parseInt(sc.nextLine());
        ArrayList<String> people = new ArrayList<>();
        ArrayList<String> pets = new ArrayList<>();
        int[][] peoplePreferences = new int[n][n];
        int[][] petsPreferences = new int[n][n];

        // Reading people names
        for (int i = 0; i < n; i++) {
            people.add(sc.nextLine());
        }

        // Reading people preferences
        for (int i = 0; i < n; i++) {
            String[] preferences = sc.nextLine().split(" ");
            for (int j = 0; j < n; j++) {
                peoplePreferences[i][j] = Integer.parseInt(preferences[j]) - 1;
            }
        }

        // Reading pets names
        for (int i = 0; i < n; i++) {
            pets.add(sc.nextLine());
        }

        // Reading pets preferences
        for (int i = 0; i < n; i++) {
            String[] preferences = sc.nextLine().split(" ");
            for (int j = 0; j < n; j++) {
                petsPreferences[i][j] = Integer.parseInt(preferences[j]) - 1;
            }
        }

        sc.close();

        // Initialize the petPartner array with -1 and petMatched array with false
        int[] petPartner = new int[n];
        boolean[] petMatched = new boolean[n];
        Arrays.fill(petPartner, -1);

        int freeCount = n;

        // Main loop for the Stable Marriage Problem algorithm
        while (freeCount > 0) {
            int freePerson;
            for (freePerson = 0; freePerson < n; freePerson++) {
                if (petPartner[freePerson] == -1) {
                    break;
                }
            }

            for (int i = 0; i < n && petPartner[freePerson] == -1; i++) {
                int pet = peoplePreferences[freePerson][i];

                if (!petMatched[pet]) {
                    petPartner[freePerson] = pet;
                    petMatched[pet] = true;
                    freeCount--;
                } else {
                    int currentPartner = -1;
                    for (int j = 0; j < n; j++) {
                        if (petPartner[j] == pet) {
                            currentPartner = j;
                            break;
                        }
                    }

                    int freePersonRank = -1, currentPartnerRank = -1;
                    for (int j = 0; j < n; j++) {
                        if (petsPreferences[pet][j] == freePerson) freePersonRank = j;
                        if (petsPreferences[pet][j] == currentPartner) currentPartnerRank = j;
                    }

                    if (freePersonRank < currentPartnerRank) {
                        petPartner[freePerson] = pet;
                        petPartner[currentPartner] = -1;
                    }
                }
            }
        }

        // Print the results
        for (int i = 0; i < petPartner.length; i++) {
            System.out.println(people.get(i) + " / " + pets.get(petPartner[i]));
        }
    }
}