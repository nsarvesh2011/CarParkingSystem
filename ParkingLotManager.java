import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkingLotManager {
    private static int totalSlots = 10;
    private static int availableSlots = totalSlots;
    private static ArrayList<String> parkedCars = new ArrayList<>();
    private static HashMap<String, String> carCategoryMap = new HashMap<>();
    private static HashMap<String, LocalDateTime> parkedTimeMap = new HashMap<>();
    static final String FILENAME = "parked_cars.txt";

    private static HashMap<Character, Boolean> parkingSpaces = new HashMap<>();
    private static HashMap<String, Character> occupiedSpaces = new HashMap<>();

    static {
        // Initialize parking spaces as available
        for (char space = 'A'; space <= 'J'; space++) {
            parkingSpaces.put(space, true);
        }
    }

    // Add all the parking lot management functions here...
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
        char parkSpace = getFirstAvailableSpace();
        occupiedSpaces.put(licensePlate, parkSpace);
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
            LocalDateTime entryTime = parkedTimeMap.get(licensePlate);
            LocalDateTime exitTime = LocalDateTime.now();
            Duration duration = Duration.between(entryTime, exitTime);
            long hours = duration.toHours();

            double parkingFee = hours * 5.0; // $5 per hour

            System.out.println("Car removed successfully.");
            System.out.println("Parking fee for " + hours + " hours: $" + parkingFee);

            
            parkedCars.remove(licensePlate);
            String category = carCategoryMap.get(licensePlate); // ???
            carCategoryMap.remove(licensePlate);
            parkedTimeMap.remove(licensePlate);
            parkingSpaces.put(occupiedSpaces.get(licensePlate), true);
            occupiedSpaces.remove(licensePlate);
            availableSlots++;

            System.out.println("Available slots: " + availableSlots);
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
            System.out.println("License Plate: " + licensePlate 
                            + ", Category: " + carCategoryMap.get(licensePlate) 
                            + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + ", Parking Space: " + occupiedSpaces.get(licensePlate));
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
                System.out.println("License Plate: " + licensePlate 
                + ", Category: " + carCategoryMap.get(licensePlate) 
                + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + ", Parked Space: " + occupiedSpaces.get(licensePlate));
            }
        }
        System.out.println("*********************************");
    }

    public static void saveParkedCars() {
        try (PrintWriter writer = new PrintWriter(FILENAME)) {
            for (String licensePlate : parkedCars) {
                writer.println(licensePlate + "," 
                + carCategoryMap.get(licensePlate) 
                + "," + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + "," + occupiedSpaces.get(licensePlate));
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
                if (parts.length == 4) {
                    String licensePlate = parts[0];
                    String category = parts[1];
                    LocalDateTime parkedTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String spaceStr = parts[3];
                    char space = spaceStr.charAt(0);
                    parkedCars.add(licensePlate);
                    carCategoryMap.put(licensePlate, category);
                    parkedTimeMap.put(licensePlate, parkedTime);
                    parkingSpaces.put(space, false);
                    occupiedSpaces.put(licensePlate, space);
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
    private static char getFirstAvailableSpace() {
        for (HashMap.Entry<Character, Boolean> entry : parkingSpaces.entrySet()) {
            char space = entry.getKey();
            boolean value = entry.getValue();

            // Check the value and perform actions accordingly
            if (value) {
                System.out.println("Space: " + space + " is now " + !value);
                parkingSpaces.put(space, false); // make space as unavailable
                return space;
            }
        }
        return '0'; // No available space found
    }

    public static void reserveParking(){
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

        acceptDateEntry(licensePlate);
        long hoursReserved = acceptDuration();
        double parkingFee = hoursReserved * 5.0; // $5 per hour

        System.out.println("Parking fee for " + hoursReserved + " hours: $" + parkingFee);

        parkedCars.add(licensePlate);
        char parkSpace = getFirstAvailableSpace();
        occupiedSpaces.put(licensePlate, parkSpace);
        carCategoryMap.put(licensePlate, category);
        //parkedTimeMap.put(licensePlate, LocalDateTime.now());
        availableSlots--;

        System.out.println("Parking reserved successfully. Available slots: " + availableSlots);
    }

    private static void acceptDateEntry(String licensePlate){
        // Prompt the user to enter a date
        System.out.println("Enter a date and time (YYYY-MM-DD HH:MM:SS):");
        Scanner scn = new Scanner(System.in);
        String userInput = scn.nextLine();

        // Define the date format expected from the user
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            // Parse the user input into a LocalDate object
            LocalDateTime entrydate = LocalDateTime.parse(userInput, dateFormatter);
            parkedTimeMap.put(licensePlate, entrydate);
            // Output the parsed date
            System.out.println("Parsed date: " + entrydate);
        } catch (Exception e) {
            // Handle invalid input
            System.out.println("Invalid date format. Please enter a date and time in YYYY-MM-DD HH:MM:SS format.");
        }
    }

    private static long acceptDuration(){
        // Accept a duration input in a specific format (e.g., "2h30m" for 2 hours and 30 minutes)
        System.out.print("Enter a duration (e.g., 2h30m for 2 hours and 30 minutes): ");
        Scanner scd = new Scanner(System.in);
        String input = scd.nextLine();

        // Define a regular expression pattern to match the duration format
        Pattern pattern = Pattern.compile("(\\d+)h(\\d+)m");
        Matcher matcher = pattern.matcher(input);

        long hoursReserved = 0;
        

        if (matcher.matches()) {
            // Extract hours and minutes from the input string
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));

            // Calculate the total duration in seconds
            long totalSeconds = hours * 3600 + minutes * 60;

            // Create a Duration object from the total duration in seconds
            Duration duration = Duration.ofSeconds(totalSeconds);
            hoursReserved = duration.toHours();
            System.out.println("Parsed Duration: " + duration);
        } else {
            System.out.println("Invalid input format. Please enter duration in the format 'XhYm' (e.g., 2h30m).");
        }
        return hoursReserved;
    }
    
}
