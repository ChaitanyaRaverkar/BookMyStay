import java.util.*;

class Reservation {
    String reservationId;
    String customerName;
    String roomType;

    Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType;
    }
}

class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    List<Reservation> getReservations() {
        return history;
    }
}

class BookingReportService {

    void displayAllBookings(List<Reservation> reservations) {
        System.out.println("Booking History:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    void generateSummary(List<Reservation> reservations) {
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(r.roomType, roomCount.getOrDefault(r.roomType, 0) + 1);
        }

        System.out.println("\nBooking Summary Report:");
        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }
    }
}

public class Main {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        Reservation r1 = new Reservation("RES101", "Alice", "Single");
        Reservation r2 = new Reservation("RES102", "Bob", "Double");
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        List<Reservation> storedBookings = history.getReservations();

        reportService.displayAllBookings(storedBookings);
        reportService.generateSummary(storedBookings);
    }
}