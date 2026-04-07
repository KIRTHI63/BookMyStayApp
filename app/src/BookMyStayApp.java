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

// Inventory (unchanged)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Reservation
class Reservation {
    private static int counter = 1;
    private int id;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.id = counter++;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public int getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + id + " | Guest: " + guestName + " | Room: " + roomType);
    }
}

// Add-On Service
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<Integer, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(int reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
        System.out.println(service.getName() + " added to Reservation ID " + reservationId);
    }

    // Show services
    public void showServices(int reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for this reservation.");
            return;
        }

        System.out.println("\nServices for Reservation ID " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " (₹" + s.getPrice() + ")");
        }
    }

    // Calculate total cost
    public double calculateTotal(int reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getPrice();
            }
        }

        return total;
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Create reservation
        Reservation r1 = new Reservation("Alice", "Single Room");
        r1.display();

        // Create services
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService spa = new AddOnService("Spa", 500);

        // Service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(r1.getId(), wifi);
        manager.addService(r1.getId(), breakfast);
        manager.addService(r1.getId(), spa);

        // Display services
        manager.showServices(r1.getId());

        // Calculate cost
        double total = manager.calculateTotal(r1.getId());
        System.out.println("\nTotal Add-On Cost: ₹" + total);

        System.out.println("\nCore booking & inventory remain unchanged.");
    }
}