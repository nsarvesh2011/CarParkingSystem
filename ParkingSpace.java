import java.util.HashMap;

public class ParkingSpace {
    private char parkingID; // Identifier of the parking space (A-Z)
    private boolean available; // Availability status of the parking space
    private String reservedBy; // License plate of the vehicle that reserved the space (null if not reserved)

    public ParkingSpace(char id, boolean available, String licensePlate) {
        this.parkingID = id;
        this.available = available; // Initially, all spaces are available
        this.reservedBy = licensePlate; // Initially, no space is reserved
    }

    // Getter methods
    public char getParkingId() {
        return parkingID;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public String getReservedBy() {
        return this.reservedBy;
    }

    // Method to reserve the parking space
    public void reserve(String licensePlate) {
        this.available = false;
        this.reservedBy = licensePlate;
    }

    // Method to free up the parking space
    public void freeUp() {
        this.available = true;
        this.reservedBy = null;
    }

    // Static method to find the first available space
    public static ParkingSpace getFirstAvailableSpace(HashMap<Character, ParkingSpace> parkingSpaces) {
        for (ParkingSpace space : parkingSpaces.values()) {
            if (space.isAvailable()) {
                space.available = false;
                return space;
            }
        }
        return null; // No available space
    }
}
