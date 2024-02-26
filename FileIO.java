import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FileIO {

    public static void loadCredentials(String CREDENTIALS_FILE, HashMap<String, String> credentials ) {
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

    public static void generateRemovedReport(String REMOVALS_FILE){
        try (BufferedReader reader = new BufferedReader(new FileReader(REMOVALS_FILE))) {
            String line;
            System.out.println("Removed Cars: \n");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String licensePlate = parts[0];
                    String category = parts[1];
                    LocalDateTime parkedTime = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime exitTime = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String duration = parts[4];
                    String parkingFee = parts[5];
                    System.out.println("LicensePlate: " + licensePlate
                                        + ", Category: " + category
                                        + ", Entry: " + parkedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                        + ", Exit: " + exitTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                        + ", Duration: " + duration
                                        + ", Parking Fee: " + parkingFee);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading credentials: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
