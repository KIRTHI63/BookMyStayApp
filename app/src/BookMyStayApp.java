import java.util.HashMap;
import java.util.Map;

// Room class (base abstraction)
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

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1000);
    }

    @Override
    public void displayDetails(int available) {
        System.out.println("Type: " + type + ", Price: " + price + ", Available: " + available);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2000);
    }

    @Override
    public void displayDetails(int available) {
        System.out.println("Type: " + type + ", Price: " + price + ", Available: " + available);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 5000);
    }

    @Override
    public void displayDetails(int available) {
        System.out.println("Type: " + type + ", Price: " + price + ", Available: " + available);
    }
}

// Centralized Inventory using HashMap
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    // Add room type with count
    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    // Get availability
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Update availability
    public void updateRoom(String type, int newCount) {
        if (inventory.containsKey(type)) {
            inventory.put(type, newCount);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → Available: " + entry.getValue());
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Register rooms in inventory
        inventory.addRoom(single.getType(), 5);
        inventory.addRoom(doubleRoom.getType(), 3);
        inventory.addRoom(suite.getType(), 2);

        // Display details using centralized data
        System.out.println("Room Details:\n");
        single.displayDetails(inventory.getAvailability(single.getType()));
        doubleRoom.displayDetails(inventory.getAvailability(doubleRoom.getType()));
        suite.displayDetails(inventory.getAvailability(suite.getType()));

        // Show full inventory
        inventory.displayInventory();

        // Update availability
        System.out.println("\nUpdating Single Room availability to 4...\n");
        inventory.updateRoom("Single Room", 4);

        // Show updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}