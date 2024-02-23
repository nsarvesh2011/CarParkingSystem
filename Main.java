// import java.time.Duration;
// import java.io.*;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.Scanner;

public class Main {
    

    public static void main(String[] args) {
        ParkingLotManager.loadParkedCars(); // Load parked cars data from file when the program starts

        Scanner sc = new Scanner(System.in);
        try{
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
                    ParkingLotManager.saveParkedCars(); // Save parked cars data to file when the program exits
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }finally{
            sc.close(); // Close Scanner object
        }
        
    }
}
