import java.util.*;

// ================= CUSTOM EXCEPTION =================
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
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

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 5000); }
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
        if (inventory.get(type) > 0) {
            inventory.put(type, inventory.get(type) - 1);
        }
    }

    public boolean roomExists(String type) {
        return inventory.containsKey(type);
    }
}

// ================= VALIDATOR =================
class BookingValidator {

    public static void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (!inventory.roomExists(roomType)) {
            throw new InvalidBookingException("Invalid room type selected");
        }

        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }
    }
}

// ================= RESERVATION =================
class Reservation {
    private static int counter = 1;
    private int id;
    private String guestName;
    private String roomType;
    private double basePrice;

    public Reservation(String guestName, String roomType, double basePrice) {
        this.id = counter++;
        this.guestName = guestName;
        this.roomType = roomType;
        this.basePrice = basePrice;
    }

    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public double getBasePrice() { return basePrice; }
}

// ================= QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation processRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ================= SERVICES =================
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() { return price; }
    public String getName() { return name; }
}

class AddOnServiceManager {
    private Map<Integer, List<AddOnService>> map = new HashMap<>();

    public void addService(int id, AddOnService s) {
        map.putIfAbsent(id, new ArrayList<>());
        map.get(id).add(s);
    }

    public double getTotal(int id) {
        double total = 0;
        if (map.containsKey(id)) {
            for (AddOnService s : map.get(id)) {
                total += s.getPrice();
            }
        }
        return total;
    }
}

// ================= HISTORY =================
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();
    private Map<Integer, Double> bills = new HashMap<>();

    public void add(Reservation r, double bill) {
        history.add(r);
        bills.put(r.getId(), bill);
    }

    public List<Reservation> getAll() { return history; }

    public double getBill(int id) {
        return bills.getOrDefault(id, 0.0);
    }
}

// ================= REPORT =================
class ReportService {
    public void generate(BookingHistory history) {

        System.out.println("\n===== REPORT =====");

        double revenue = 0;

        for (Reservation r : history.getAll()) {
            revenue += history.getBill(r.getId());
        }

        System.out.println("Total Bookings: " + history.getAll().size());
        System.out.println("Total Revenue: ₹" + revenue);
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);
        inventory.addRoom("Double Room", 1);

        BookingQueue queue = new BookingQueue();

        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        BookingHistory history = new BookingHistory();

        // Simulated inputs (including invalid ones)
        String[][] inputs = {
                {"Alice", "Single Room"},
                {"", "Double Room"},              // invalid name
                {"Bob", "Luxury Room"},           // invalid room
                {"Charlie", "Single Room"}        // may fail (no availability)
        };

        System.out.println("=== PROCESSING BOOKINGS WITH VALIDATION ===\n");

        for (String[] input : inputs) {
            try {
                String name = input[0];
                String room = input[1];

                // VALIDATE
                BookingValidator.validate(name, room, inventory);

                // CREATE RESERVATION
                Reservation r = new Reservation(name, room, 1000);
                queue.addRequest(r);

            } catch (InvalidBookingException e) {
                System.out.println("❌ ERROR: " + e.getMessage());
            }
        }

        // PROCESS VALID BOOKINGS
        while (!queue.isEmpty()) {
            Reservation r = queue.processRequest();

            inventory.reduceRoom(r.getRoomType());

            serviceManager.addService(r.getId(), wifi);

            double total = r.getBasePrice() + serviceManager.getTotal(r.getId());

            history.add(r, total);

            System.out.println("✅ CONFIRMED: " + r.getGuestName() + " | ₹" + total);
        }

        // REPORT
        new ReportService().generate(history);
    }
}