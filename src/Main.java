import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    String reservationId;
    String customerName;
    String roomType;

    Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + customerName + " | " + roomType;
    }
}

class SystemState implements Serializable {
    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

class PersistenceService {

    static void saveState(SystemState state, String fileName) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(state);
            oos.close();
            System.out.println("System state saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    static SystemState loadState(String fileName) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            SystemState state = (SystemState) ois.readObject();
            ois.close();
            System.out.println("System state restored successfully.");
            return state;
        } catch (Exception e) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }
    }
}

public class Main {

    public static void main(String[] args) {

        String fileName = "system_state.dat";

        SystemState loadedState = PersistenceService.loadState(fileName);

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        if (loadedState != null) {
            inventory = loadedState.inventory;
            bookingHistory = loadedState.bookingHistory;
        } else {
            inventory = new HashMap<>();
            bookingHistory = new ArrayList<>();

            inventory.put("Single", 3);
            inventory.put("Double", 2);
            inventory.put("Suite", 1);
        }

        bookingHistory.add(new Reservation("RES101", "Alice", "Single"));
        inventory.put("Single", inventory.get("Single") - 1);

        bookingHistory.add(new Reservation("RES102", "Bob", "Double"));
        inventory.put("Double", inventory.get("Double") - 1);

        System.out.println("\nCurrent Inventory:");
        System.out.println(inventory);

        System.out.println("\nBooking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        SystemState state = new SystemState(inventory, bookingHistory);
        PersistenceService.saveState(state, fileName);
    }
}