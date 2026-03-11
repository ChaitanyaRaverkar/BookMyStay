import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Model
class Reservation {
    String customerName;
    String roomType;

    Reservation(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Customer: " + customerName + ", Room Type: " + roomType;
    }
}

// Validator Class
class InvalidBookingValidator {

    static void validate(String roomType, Map<String, Integer> inventory) throws InvalidBookingException {

        // Validate room type
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type selected: " + roomType);
        }

        // Check availability
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for room type: " + roomType);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Map<String, Integer> inventory = new HashMap<>();

        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 0);

        List<Reservation> confirmedBookings = new ArrayList<>();

        String[][] bookingInputs = {
                {"Alice", "Single"},
                {"Bob", "Suite"},     // invalid (no inventory)
                {"Charlie", "Deluxe"}, // invalid room type
                {"David", "Double"}
        };

        for (String[] input : bookingInputs) {

            String customer = input[0];
            String roomType = input[1];

            try {

                // Validate before booking
                InvalidBookingValidator.validate(roomType, inventory);

                // Process booking
                inventory.put(roomType, inventory.get(roomType) - 1);

                Reservation r = new Reservation(customer, roomType);
                confirmedBookings.add(r);

                System.out.println("Booking Confirmed -> " + r);

            } catch (InvalidBookingException e) {

                System.out.println("Booking Failed for " + customer + ": " + e.getMessage());

            }
        }

        System.out.println("\nFinal Inventory State: " + inventory);

        System.out.println("\nConfirmed Bookings:");
        for (Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }
}