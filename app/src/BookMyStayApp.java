import java.util.*;

// ================= ROOM =================
abstract class Room {
    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1000); }
}

// ================= INVENTORY (THREAD SAFE) =================
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public synchronized void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public synchronized boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }
}

// ================= RESERVATION =================
class Reservation {
    private static int counter = 1;
    private int id;
    private String name;
    private String roomType;

    public Reservation(String name, String roomType) {
        this.id = counter++;
        this.name = name;
        this.roomType = roomType;
    }

    public String getName() { return name; }
    public String getRoomType() { return roomType; }
    public int getId() { return id; }
}

// ================= THREAD (BOOKING PROCESSOR) =================
class BookingProcessor extends Thread {

    private Reservation reservation;
    private RoomInventory inventory;

    public BookingProcessor(Reservation r, RoomInventory inv) {
        this.reservation = r;
        this.inventory = inv;
    }

    @Override
    public void run() {
        System.out.println("Processing: " + reservation.getName());

        boolean success = inventory.bookRoom(reservation.getRoomType());

        if (success) {
            System.out.println("✅ SUCCESS: " + reservation.getName());
        } else {
            System.out.println("❌ FAILED: " + reservation.getName() + " (No rooms)");
        }
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2); // only 2 rooms

        // Multiple users trying to book at same time
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Single Room");
        Reservation r3 = new Reservation("Charlie", "Single Room");
        Reservation r4 = new Reservation("David", "Single Room");

        // Threads
        Thread t1 = new BookingProcessor(r1, inventory);
        Thread t2 = new BookingProcessor(r2, inventory);
        Thread t3 = new BookingProcessor(r3, inventory);
        Thread t4 = new BookingProcessor(r4, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}