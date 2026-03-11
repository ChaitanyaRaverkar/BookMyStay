import java.util.*;

class Reservation {
    String reservationId;
    String customerName;
    String roomType;
    String roomId;
    boolean active;

    Reservation(String reservationId, String customerName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }

    public String toString() {
        return "ReservationID: " + reservationId +
                ", Customer: " + customerName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Status: " + (active ? "CONFIRMED" : "CANCELLED");
    }
}

class CancellationService {

    private Map<String, Reservation> reservations;
    private Map<String, Integer> inventory;
    private Stack<String> rollbackStack;

    CancellationService(Map<String, Reservation> reservations,
                        Map<String, Integer> inventory,
                        Stack<String> rollbackStack) {
        this.reservations = reservations;
        this.inventory = inventory;
        this.rollbackStack = rollbackStack;
    }

    void cancelBooking(String reservationId) {

        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation r = reservations.get(reservationId);

        if (!r.active) {
            System.out.println("Cancellation Failed: Reservation already cancelled.");
            return;
        }

        rollbackStack.push(r.roomId);

        inventory.put(r.roomType, inventory.get(r.roomType) + 1);

        r.active = false;

        System.out.println("Cancellation Successful for Reservation: " + reservationId);
    }
}

public class Main {

    public static void main(String[] args) {

        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        Map<String, Reservation> reservations = new HashMap<>();

        Stack<String> rollbackStack = new Stack<>();

        Reservation r1 = new Reservation("RES101", "Alice", "Single", "S001");
        Reservation r2 = new Reservation("RES102", "Bob", "Double", "D001");

        reservations.put(r1.reservationId, r1);
        reservations.put(r2.reservationId, r2);

        inventory.put("Single", inventory.get("Single") - 1);
        inventory.put("Double", inventory.get("Double") - 1);

        CancellationService service = new CancellationService(reservations, inventory, rollbackStack);

        service.cancelBooking("RES101");
        service.cancelBooking("RES999");
        service.cancelBooking("RES101");

        System.out.println("\nRollback Stack (Released Room IDs): " + rollbackStack);

        System.out.println("\nInventory State: " + inventory);

        System.out.println("\nBooking History:");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}