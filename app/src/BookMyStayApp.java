import java.util.*;

// Abstract Room
abstract class Room {
    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

// Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 5000);
    }
}

// Central Inventory (still unchanged here)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// 🧾 Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// 📥 Booking Request Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // Add request
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Added booking request for " + reservation.getGuestName());
    }

    // View all requests
    public void showQueue() {
        System.out.println("\nBooking Queue (FIFO Order):\n");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Initialize inventory (still not modified)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 3);
        inventory.addRoom("Suite Room", 2);

        // Create booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");
        Reservation r4 = new Reservation("David", "Single Room");

        // Add to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);
        bookingQueue.addRequest(r4);

        // Display queue
        bookingQueue.showQueue();

        System.out.println("\nNo inventory updated yet. Requests are waiting.");
    }
}