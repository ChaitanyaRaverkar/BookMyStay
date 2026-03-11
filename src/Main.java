import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingProcessor implements Runnable {

    private Queue<BookingRequest> bookingQueue;
    private Map<String, Integer> inventory;

    BookingProcessor(Queue<BookingRequest> bookingQueue, Map<String, Integer> inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {

            BookingRequest request;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    return;
                }
                request = bookingQueue.poll();
            }

            processBooking(request);
        }
    }

    private void processBooking(BookingRequest request) {

        synchronized (inventory) {

            if (!inventory.containsKey(request.roomType)) {
                System.out.println(Thread.currentThread().getName() +
                        " -> Invalid Room Type for " + request.guestName);
                return;
            }

            int available = inventory.get(request.roomType);

            if (available > 0) {

                inventory.put(request.roomType, available - 1);

                System.out.println(Thread.currentThread().getName() +
                        " -> Booking Confirmed for " + request.guestName +
                        " | Room Type: " + request.roomType);

            } else {

                System.out.println(Thread.currentThread().getName() +
                        " -> No rooms available for " + request.roomType +
                        " | Guest: " + request.guestName);
            }
        }
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        bookingQueue.add(new BookingRequest("Alice", "Single"));
        bookingQueue.add(new BookingRequest("Bob", "Single"));
        bookingQueue.add(new BookingRequest("Charlie", "Double"));
        bookingQueue.add(new BookingRequest("David", "Single"));
        bookingQueue.add(new BookingRequest("Eva", "Double"));

        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);

        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nFinal Inventory State: " + inventory);
    }
}