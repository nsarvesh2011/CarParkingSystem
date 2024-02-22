import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static int totalSlots = 10; // Increase total number of parking slots to 10
    static int availableSlots = totalSlots;
    static ArrayList<String> parkedCars = new ArrayList<>();
    static HashMap<String, String> carCategoryMap = new HashMap<>();
    static HashMap<String, LocalDateTime> parkedTimeMap = new HashMap<>();
    static final String FILENAME = "parked_cars.txt";

    public static void main(String[] args) {
        loadParkedCars(); // Load parked cars data from file when the program starts

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Park a car");
            System.out.println("2. Remove a car");
            System.out.println("3. View parked cars");
            System.out.println("4. Check parking availability");
            System.out.println("5. Generate parking entries report");
            System.out.println("6. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    parkCar();
                    break;
                case 2:
                    removeCar();
                    break;
                case 3:
                    viewParkedCars();
                    break;
                case 4:
                    checkAvailability();
                    break;
                case 5:
                    generateReport();
                    break;
                case 6:
                    saveParkedCars(); // Save parked cars data to file when the program exits
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void parkCar() {
        if (availableSlots == 0) {
            System.out.println("Sorry, there are no available parking slots.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the license plate number of the car:");
        String licensePlate = sc.next();

        System.out.println("Enter the category of the car:");
        System.out.println("1. Compact");
        System.out.println("2. Sedan");
        System.out.println("3. SUV");
        int categoryChoice = sc.nextInt();
        String category;
        switch (categoryChoice) {
            case 1:
                category = "Compact";
                break;
            case 2:
                category = "Sedan";
                break;
            case 3:
                category = "SUV";
                break;
            default:
                category = "Unknown";
                break;
        }

        parkedCars.add(licensePlate);
        carCategoryMap.put(licensePlate, category);
        parkedTimeMap.put(licensePlate, LocalDateTime.now());
        availableSlots--;

        System.out.println("Car parked successfully. Available slots: " + availableSlots);
    }

    public static void removeCar() {
        if (availableSlots == totalSlots) {
            System.out.println("There are no parked cars.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the license plate number of the car to be removed:");
        String licensePlate = sc.next();

        if (parkedCars.contains(licensePlate)) {
            parkedCars.remove(licensePlate);
            String category = carCategoryMap.get(licensePlate);
            carCategoryMap.remove(licensePlate);
            parkedTimeMap.remove(licensePlate);
            availableSlots++;

            System.out.println("Car removed successfully. Available slots: " + availableSlots);
        } else {
            System.out.println("The car is not parked here.");
        }
    }

    public static void viewParkedCars() {
        if (availableSlots == totalSlots) {
            System.out.println("There are no parked cars.");
            return;
        }

        System.out.println("Parked cars:");
        for (String licensePlate : parkedCars) {
            System.out.println("License Plate: " + licensePlate + ", Category: " + carCategoryMap.get(licensePlate) + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    public static void checkAvailability() {
        System.out.println("Available parking slots: " + availableSlots);
    }

    public static void generateReport() {
        System.out.println("***** Parking Entries Report *****");
        System.out.println("Total Parking Slots: " + totalSlots);
        System.out.println("Available Parking Slots: " + availableSlots);
        System.out.println("---------- Parked Cars ----------");
        if (parkedCars.isEmpty()) {
            System.out.println("No cars parked.");
        } else {
            for (String licensePlate : parkedCars) {
                System.out.println("License Plate: " + licensePlate + ", Category: " + carCategoryMap.get(licensePlate) + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        }
        System.out.println("*********************************");
    }

    public static void saveParkedCars() {
        try (PrintWriter writer = new PrintWriter(FILENAME)) {
            for (String licensePlate : parkedCars) {
                writer.println(licensePlate + "," + carCategoryMap.get(licensePlate) + "," + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadParkedCars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            int loadedCars = 0; // Initialize counter for loaded cars
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String licensePlate = parts[0];
                    String category = parts[1];
                    LocalDateTime parkedTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    parkedCars.add(licensePlate);
                    carCategoryMap.put(licensePlate, category);
                    parkedTimeMap.put(licensePlate, parkedTime);
                    loadedCars++; // Increment loaded cars counter
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
            // Update availableSlots based on loaded cars
            availableSlots = totalSlots - loadedCars;
        } catch (IOException e) {
            System.err.println("Error loading parked cars: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
