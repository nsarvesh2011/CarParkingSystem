import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private static final HashMap<String, String> credentials = new HashMap<>();
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_USERNAME = "user";

    public static void main(String[] args) {
        loadCredentials();

        Scanner sc = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome to the Parking Lot Management System!");
            System.out.println("Choose an option:");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    login(sc, ADMIN_USERNAME);
                    break;
                case 2:
                    login(sc, USER_USERNAME);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login(Scanner sc, String username) {
        System.out.println("Please login as " + username + ":");
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (isValidCredentials(username, password)) {
            System.out.println("Login successful.");
            if (username.equals(ADMIN_USERNAME)) {
                // Call admin menu
                adminMenu(sc);
            } else if (username.equals(USER_USERNAME)) {
                // Call user menu
                userMenu(sc);
            }
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void loadCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    credentials.put(username, password);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading credentials: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidCredentials(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    // Admin menu
    private static void adminMenu(Scanner sc) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Park a car");
            System.out.println("2. Remove a car");
            System.out.println("3. View parked cars");
            System.out.println("4. Check parking availability");
            System.out.println("5. Generate parking entries report");
            System.out.println("6. Reserve Parking");
            System.out.println("7. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    ParkingLotManager.parkCar();
                    break;
                case 2:
                    ParkingLotManager.removeCar();
                    break;
                case 3:
                    ParkingLotManager.viewParkedCars();
                    break;
                case 4:
                    ParkingLotManager.checkAvailability();
                    break;
                case 5:
                    ParkingLotManager.generateReport();
                    break;
                case 6:
                    ParkingLotManager.reserveParking();
                    break;
                case 7:
                ParkingLotManager.saveParkedCars();
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // User menu
    private static void userMenu(Scanner sc) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Park a car");
            System.out.println("2. Remove a Car");
            System.out.println("3. Check parking availability");
            System.out.println("4. Reserve a Car");
            System.out.println("5. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline character

            switch (choice) {
                // Implement user menu functionalities here...
                case 1:
                    ParkingLotManager.parkCar();
                    break;
                case 2:
                    ParkingLotManager.removeCar();
                    break;
                case 3:
                    ParkingLotManager.checkAvailability();
                    break;
                case 4:
                    ParkingLotManager.reserveParking();
                    break;
                case 7:
                ParkingLotManager.saveParkedCars();
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
