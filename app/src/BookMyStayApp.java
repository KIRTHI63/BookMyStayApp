import java.util.*;

// ================= EXCEPTION =================
class InvalidBookingException extends Exception {
    public InvalidBookingException(String msg) {
        super(msg);
    }
}

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

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2000); }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increaseRoom(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public boolean exists(String type) {
        return inventory.containsKey(type);
    }
}

// ================= VALIDATOR =================
class BookingValidator {
    public static void validate(String name, String room, RoomInventory inv)
            throws InvalidBookingException {

        if (name == null || name.trim().isEmpty())
            throw new InvalidBookingException("Invalid Name");

        if (!inv.exists(room))
            throw new InvalidBookingException("Invalid Room Type");

        if (inv.getAvailability(room) <= 0)
            throw new InvalidBookingException("No Rooms Available");
    }
}

// ================= RESERVATION =================
class Reservation {
    private static int counter = 1;
    private int id;
    private String name;
    private String roomType;
    private double price;
    private boolean active = true;

    public Reservation(String name, String roomType, double price) {
        this.id = counter++;
        this.name = name;
        this.roomType = roomType;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRoomType() { return roomType; }
    public double getPrice() { return price; }

    public boolean isActive() { return active; }
    public void cancel() { active = false; }
}

// ================= QUEUE =================
class BookingQueue {
    private Queue<Reservation> q = new LinkedList<>();

    public void add(Reservation r) { q.add(r); }
    public Reservation process() { return q.poll(); }
    public boolean isEmpty() { return q.isEmpty(); }
}

// ================= HISTORY =================
class BookingHistory {
    private Map<Integer, Reservation> records = new HashMap<>();

    public void add(Reservation r) {
        records.put(r.getId(), r);
    }

    public Reservation get(int id) {
        return records.get(id);
    }

    public Collection<Reservation> getAll() {
        return records.values();
    }
}

// ================= CANCELLATION =================
class CancellationService {

    public static void cancelBooking(int id, BookingHistory history, RoomInventory inv)
            throws InvalidBookingException {

        Reservation r = history.get(id);

        if (r == null)
            throw new InvalidBookingException("Reservation not found");

        if (!r.isActive())
            throw new InvalidBookingException("Already cancelled");

        // Rollback inventory
        inv.increaseRoom(r.getRoomType());

        // Mark cancelled
        r.cancel();

        System.out.println("🔁 Booking Cancelled for " + r.getName());
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        RoomInventory inv = new RoomInventory();
        inv.addRoom("Single Room", 1);
        inv.addRoom("Double Room", 1);

        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        System.out.println("=== BOOKING PHASE ===\n");

        try {
            BookingValidator.validate("Alice", "Single Room", inv);
            queue.add(new Reservation("Alice", "Single Room", 1000));

            BookingValidator.validate("Bob", "Double Room", inv);
            queue.add(new Reservation("Bob", "Double Room", 2000));

        } catch (InvalidBookingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Process bookings
        while (!queue.isEmpty()) {
            Reservation r = queue.process();

            inv.reduceRoom(r.getRoomType());
            history.add(r);

            System.out.println("✅ Confirmed: " + r.getName() + " (ID: " + r.getId() + ")");
        }

        // Cancellation
        System.out.println("\n=== CANCELLATION PHASE ===\n");

        try {
            CancellationService.cancelBooking(1, history, inv); // valid
            CancellationService.cancelBooking(1, history, inv); // error
        } catch (InvalidBookingException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }

        // Final state
        System.out.println("\n=== FINAL STATUS ===");
        for (Reservation r : history.getAll()) {
            System.out.println("ID: " + r.getId() +
                    " | " + r.getName() +
                    " | Active: " + r.isActive());
        }
    }
}