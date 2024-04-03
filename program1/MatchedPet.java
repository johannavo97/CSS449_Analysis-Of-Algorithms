import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MatchedPet {
    public static void main(String[] args) {
        // Read input from file
        List<String> people = new ArrayList<>();
        List<List<Integer>> peoplePreferences = new ArrayList<>();
        List<String> pets = new ArrayList<>();
        List<List<Integer>> petPreferences = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("program1data.txt"));
            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                people.add(scanner.nextLine());
            }

            for (int i = 0; i < n; i++) {
                List<Integer> preferences = new ArrayList<>();
                String[] preferencesStr = scanner.nextLine().split(" ");
                for (String preference : preferencesStr) {
                    preferences.add(Integer.parseInt(preference));
                }
                peoplePreferences.add(preferences);
            }

            for (int i = 0; i < n; i++) {
                pets.add(scanner.nextLine());
            }

            for (int i = 0; i < n; i++) {
                List<Integer> preferences = new ArrayList<>();
                String[] preferencesStr = scanner.nextLine().split(" ");
                for (String preference : preferencesStr) {
                    preferences.add(Integer.parseInt(preference));
                }
                petPreferences.add(preferences);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Implement Gale-Shapley algorithm

        // Initialize all people to be free
        List<Boolean> peopleFree = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            peopleFree.add(true);
        }

        // Initialize all pets to be free
        List<Boolean> petsFree = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) {
            petsFree.add(true);
        }

        // Initialize all people to have no pet
        List<Integer> peoplePet = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            peoplePet.add(-1);
        }

        // Initialize all pets to have no owner
        List<Integer> petOwner = new ArrayList<>();
        for (int i = 0; i < pets.size(); i++) {
            petOwner.add(-1);
        }

        // While there is a free person
        while (peopleFree.contains(true)) {
            // Get the first free person
            int person = peopleFree.indexOf(true);

            // Get the preferences of the person
            List<Integer> preferences = peoplePreferences.get(person);

            // For each preference
            for (int i = 0; i < preferences.size(); i++) {
                // Get the pet
                int pet = preferences.get(i);

                // If the pet is free
                if (petsFree.get(pet)) {
                    // Assign the pet to the person
                    peoplePet.set(person, pet);
                    petOwner.set(pet, person);

                    // Mark the person as not free
                    peopleFree.set(person, false);

                    // Mark the pet as not free
                    petsFree.set(pet, false);

                    break;
                } else {
                    // Get the current owner of the pet
                    int owner = petOwner.get(pet);

                    // Get the preferences of the pet
                    List<Integer> specificPetPreferences = petPreferences.get(pet);

                    // If the person is more preferred than the owner
                    if (specificPetPreferences.indexOf(person) < specificPetPreferences.indexOf(owner)) {
                        // Assign the pet to the person
                        peoplePet.set(person, pet);
                        petOwner.set(pet, person);

                        // Mark the person as not free
                        peopleFree.set(person, false);

                        // Mark the owner as free
                        peopleFree.set(owner, true);

                        break;
                    }
                }
            }
        }

        // Print the matched pairs
        for (int i = 0; i < people.size(); i++) {
            System.out.println(people.get(i) + " / " + pets.get(i));
        }
    }
}