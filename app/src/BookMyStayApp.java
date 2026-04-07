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

    public abstract void displayDetails(int available);
}

// Room प्रकार
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1000);
    }

    public void displayDetails(int available) {
        System.out.println(type + " | Price: " + price + " | Available: " + available);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2000);
    }

    public void displayDetails(int available) {
        System.out.println(type + " | Price: " + price + " | Available: " + available);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 5000);
    }

    public void displayDetails(int available) {
        System.out.println(type + " | Price: " + price + " | Available: " + available);
    }
}

// Central Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllRooms() {
        return inventory;
    }
}

// 🔍 Search Service (READ-ONLY)
class SearchService {

    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {
        System.out.println("\nAvailable Rooms:\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            // Filter unavailable rooms
            if (available > 0) {
                room.displayDetails(available);
            }
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Store rooms in list
        List<Room> rooms = new ArrayList<>();
        rooms.add(single);
        rooms.add(doubleRoom);
        rooms.add(suite);

        // Register availability
        inventory.addRoom("Single Room", 5);
        inventory.addRoom("Double Room", 0); // unavailable
        inventory.addRoom("Suite Room", 2);

        // Search (read-only)
        SearchService search = new SearchService();
        search.searchAvailableRooms(rooms, inventory);

        System.out.println("\nSystem state unchanged. Application Terminated.");
    }
}