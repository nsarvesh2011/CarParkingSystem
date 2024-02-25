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
    private static final int TOTAL_SLOTS = 10;
    private static int availableSlots = TOTAL_SLOTS;
    private static ArrayList<String> parkedCars = new ArrayList<>();
    private static HashMap<String, String> carCategoryMap = new HashMap<>();
    private static HashMap<String, LocalDateTime> parkedTimeMap = new HashMap<>();
    static final String FILENAME = "parked_cars.txt";

    private static HashMap<Character, ParkingSpace> parkingSpaces = new HashMap<>();
    //private static HashMap<String, Character> occupiedSpaces = new HashMap<>();

    static {
        // Initialize parking spaces as available
        int limit = TOTAL_SLOTS;
        for (char space = 'A'; limit > 0; space++, limit--) {
            parkingSpaces.put(space, new ParkingSpace(space, true, null));
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
        ParkingSpace parkSpace = ParkingSpace.getFirstAvailableSpace(parkingSpaces);
        parkSpace.reserve(licensePlate);
        //occupiedSpaces.put(licensePlate, parkSpace);
        carCategoryMap.put(licensePlate, category);
        parkedTimeMap.put(licensePlate, LocalDateTime.now());
        availableSlots--;

        System.out.println("Car parked successfully. Available slots: " + availableSlots);
    }

    public static void removeCar() {
        if (availableSlots == TOTAL_SLOTS) {
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
            //String category = carCategoryMap.get(licensePlate); // ???
            carCategoryMap.remove(licensePlate);
            parkedTimeMap.remove(licensePlate);
            ParkingSpace space = findParkingSpaceByLicensePlate(licensePlate);
            space.freeUp();
            //parkingSpaces.put(occupiedSpaces.get(licensePlate), true);
            //occupiedSpaces.remove(licensePlate);
            availableSlots++;

            System.out.println("Available slots: " + availableSlots);
        } else {
            System.out.println("The car is not parked here.");
        }
    }

    public static void viewParkedCars() {
        if (availableSlots == TOTAL_SLOTS) {
            System.out.println("There are no parked cars.");
            return;
        }

        System.out.println("Parked cars:");
        for (String licensePlate : parkedCars) {
            System.out.println("License Plate: " + licensePlate 
                            + ", Category: " + carCategoryMap.get(licensePlate) 
                            + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + ", Parking Space: " + findParkingSpaceByLicensePlate(licensePlate).getParkingId());
        }
    }

    public static void checkAvailability() {
        System.out.println("Available parking slots: " + availableSlots);
    }

    public static void generateReport() {
        System.out.println("***** Parking Entries Report *****");
        System.out.println("Total Parking Slots: " + TOTAL_SLOTS);
        System.out.println("Available Parking Slots: " + availableSlots);
        System.out.println("---------- Parked Cars ----------");
        if (parkedCars.isEmpty()) {
            System.out.println("No cars parked.");
        } else {
            for (String licensePlate : parkedCars) {
                System.out.println("License Plate: " + licensePlate 
                + ", Category: " + carCategoryMap.get(licensePlate) 
                + ", Parked Time: " + parkedTimeMap.get(licensePlate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + ", Parked Space: " + findParkingSpaceByLicensePlate(licensePlate).getParkingId());
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
                + "," + findParkingSpaceByLicensePlate(licensePlate).getParkingId());
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
                    parkingSpaces.put(space, new ParkingSpace(space, false, licensePlate));
                    //occupiedSpaces.put(licensePlate, space);
                    loadedCars++; // Increment loaded cars counter
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
            // Update availableSlots based on loaded cars
            availableSlots = TOTAL_SLOTS - loadedCars;
        } catch (IOException e) {
            System.err.println("Error loading parked cars: " + e.getMessage());
            e.printStackTrace();
        }
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

        showAvailableSpaces();

        System.out.println("\nEnter which space you would like to reserve:");
        char spaceId = sc.next().toUpperCase().charAt(0); // Convert to uppercase and get the first character
        // Check if the entered spaceId is valid (i.e., exists in the parkingSpaces HashMap)
        if (parkingSpaces.containsKey(spaceId)) {
            ParkingSpace parkingSpace = parkingSpaces.get(spaceId);

            // Check if the parking space is available
            if (parkingSpace.isAvailable()) {
                // The space is available, you can proceed with the reservation
                System.out.println("OK. Reserving space " + spaceId + " for license " + licensePlate);
            } 
            else {
                // The space is not available, print a message indicating that it's already occupied
                System.out.println("Space " + spaceId + " is already occupied.");
                return;
            }
        } 
        else {
            // The entered spaceId is not valid (i.e., it does not exist in the parkingSpaces HashMap)
            System.out.println("Invalid space ID. Please enter a valid space ID.");
            return;
        }

        setParkingSpace(spaceId, licensePlate);
        acceptDateEntry(licensePlate);
        long hoursReserved = acceptDuration();
        double parkingFee = hoursReserved * 5.0; // $5 per hour

        System.out.println("Parking fee for " + hoursReserved + " hours: $" + parkingFee);

        parkedCars.add(licensePlate);
        carCategoryMap.put(licensePlate, category);
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

    public static ParkingSpace findParkingSpaceByLicensePlate(String licensePlate) {
        for (ParkingSpace space : parkingSpaces.values()) {
            if (space.getReservedBy() != null && space.getReservedBy().equals(licensePlate)) {
                return space; // Found the parking space
            }
        }
        return null; // License plate not found in any parking space
    }
    
    private static void showAvailableSpaces(){
        System.out.println("Available spaces: ");
        for (ParkingSpace space : parkingSpaces.values()) {
            if (space.isAvailable()) {
                System.out.print(space.getParkingId() + ", ");
            }
        }
    }

    public static void setParkingSpace(Character parkingId, String licensePlate){
        for (ParkingSpace space : parkingSpaces.values()) {
            if (space.getParkingId() == parkingId) {
                space.reserve(licensePlate);
            }
        }
    }
}
