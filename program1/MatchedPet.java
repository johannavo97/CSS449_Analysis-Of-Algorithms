import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MatchedPet {

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

        int[] petPartner = new int[n];
        boolean[] petMatched = new boolean[n];
        for (int i = 0; i < n; i++) {
            petPartner[i] = -1;
        }

        int freeCount = n;

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

        for (int i = 0; i < petPartner.length; i++) {
            System.out.println(people.get(i) + " / " + pets.get(petPartner[i]));
        }
    }
}
