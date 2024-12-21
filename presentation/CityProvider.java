package presentation;

import java.util.Scanner;

public class CityProvider {
    Scanner scanner = new Scanner(System.in);

    public String getCity() {
        String city = "";
        do {
            System.out.println("Enter the default city: ");
            city = scanner.nextLine().trim(); // Read and trim input
    
            // Validate the input
            if (city.isEmpty()) {
                System.out.println("City name cannot be empty. Please try again.");
            } else if (!city.matches("[a-zA-Z ]+")) {
                System.out.println("City name must contain only letters. Please try again.");
            }
        } while (city.isEmpty() || !city.matches("[a-zA-Z ]+")); // Keep looping until valid

        scanner.close();
    
        return city; // Return the valid city name
    }
    
}