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

// ================= BOOKING QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request added: " + r.getGuestName());
    }

    public Reservation processRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ================= ADD-ON SERVICE =================
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

// ================= SERVICE MANAGER =================
class AddOnServiceManager {
    private Map<Integer, List<AddOnService>> map = new HashMap<>();

    public void addService(int resId, AddOnService service) {
        map.putIfAbsent(resId, new ArrayList<>());
        map.get(resId).add(service);
    }

    public double getTotal(int resId) {
        double total = 0;
        if (map.containsKey(resId)) {
            for (AddOnService s : map.get(resId)) {
                total += s.getPrice();
            }
        }
        return total;
    }

    public void showServices(int resId) {
        if (!map.containsKey(resId)) return;
        for (AddOnService s : map.get(resId)) {
            System.out.println("  + " + s.getName() + " ₹" + s.getPrice());
        }
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {

        // Rooms
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Double Room", 1);
        inventory.addRoom("Suite Room", 1);

        // Queue
        BookingQueue queue = new BookingQueue();

        // Add requests
        queue.addRequest(new Reservation("Alice", "Single Room", single.getPrice()));
        queue.addRequest(new Reservation("Bob", "Double Room", dbl.getPrice()));
        queue.addRequest(new Reservation("Charlie", "Suite Room", suite.getPrice()));
        queue.addRequest(new Reservation("David", "Single Room", single.getPrice()));

        // Services
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService food = new AddOnService("Food", 300);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        System.out.println("\n=== PROCESSING BOOKINGS ===\n");

        while (!queue.isEmpty()) {
            Reservation r = queue.processRequest();

            int available = inventory.getAvailability(r.getRoomType());

            if (available > 0) {
                inventory.reduceRoom(r.getRoomType());

                System.out.println("Booking CONFIRMED for " + r.getGuestName());
                System.out.println("Room: " + r.getRoomType());

                // Add services randomly (demo)
                serviceManager.addService(r.getId(), wifi);
                serviceManager.addService(r.getId(), food);

                double serviceCost = serviceManager.getTotal(r.getId());
                double total = r.getBasePrice() + serviceCost;

                System.out.println("Base Price: ₹" + r.getBasePrice());
                System.out.println("Add-ons:");
                serviceManager.showServices(r.getId());
                System.out.println("Total Bill: ₹" + total);

            } else {
                System.out.println("Booking FAILED for " + r.getGuestName() + " (No rooms)");
            }

            System.out.println("----------------------------");
        }

        System.out.println("\nAll bookings processed.");
    }
}