import java.util.*;

class BookingRequest {
    String customerName;
    String roomType;

    BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 3);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.containsKey(roomType) && inventory.get(roomType) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

public class UseCase6RoomAllocationService {

    private static Queue<BookingRequest> requestQueue = new LinkedList<>();

    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    private static Set<String> usedRoomIds = new HashSet<>();

    private static int roomCounter = 1;

    private static String generateRoomId(String roomType) {
        String prefix = roomType.substring(0,1).toUpperCase();
        String roomId;

        do {
            roomId = prefix + String.format("%03d", roomCounter++);
        } while (usedRoomIds.contains(roomId));

        usedRoomIds.add(roomId);
        return roomId;
    }

    private static void processBookings(InventoryService inventoryService) {

        while (!requestQueue.isEmpty()) {

            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing booking for: " + request.customerName);

            if (inventoryService.isAvailable(request.roomType)) {

                String roomId = generateRoomId(request.roomType);

                allocatedRooms.putIfAbsent(request.roomType, new HashSet<>());
                allocatedRooms.get(request.roomType).add(roomId);

                inventoryService.decrement(request.roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Customer: " + request.customerName);
                System.out.println("Room Type: " + request.roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Sorry! No " + request.roomType + " rooms available.");
            }
        }
    }

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();

        requestQueue.add(new BookingRequest("Alice", "Single"));
        requestQueue.add(new BookingRequest("Bob", "Double"));
        requestQueue.add(new BookingRequest("Charlie", "Single"));
        requestQueue.add(new BookingRequest("David", "Suite"));
        requestQueue.add(new BookingRequest("Eva", "Single"));

        processBookings(inventoryService);

        System.out.println("\nAllocated Rooms:");
        System.out.println(allocatedRooms);

        System.out.println();
        inventoryService.displayInventory();
    }
}