import java.io.*;
import java.util.*;

// ================= DATA WRAPPER =================
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<String> bookings;

    public SystemState(Map<String, Integer> inventory, List<String> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);
        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> data) {
        this.inventory = data;
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

// ================= PERSISTENCE =================
class PersistenceService {

    public static void save(SystemState state, String file) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            oos.writeObject(state);
            System.out.println("💾 Data saved successfully");

        } catch (IOException e) {
            System.out.println("❌ Save Error: " + e.getMessage());
        }
    }

    public static SystemState load(String file) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("📂 Data loaded successfully");
            return state;

        } catch (Exception e) {
            System.out.println("⚠️ No previous data found (first run)");
            return null;
        }
    }
}

// ================= MAIN =================
public class Main {

    public static void main(String[] args) {

        String FILE = "data.ser";

        RoomInventory inventory = new RoomInventory();
        List<String> bookings = new ArrayList<>();

        // ===== TRY LOADING EXISTING DATA =====
        SystemState loaded = PersistenceService.load(FILE);

        if (loaded != null) {
            inventory.setInventory(loaded.inventory);
            bookings = loaded.bookings;

            System.out.println("\n🔄 SYSTEM RECOVERED");
        } else {
            // First time setup
            inventory.addRoom("Single Room", 2);
            inventory.addRoom("Double Room", 1);

            System.out.println("\n🆕 NEW SYSTEM INITIALIZED");
        }

        // ===== CURRENT STATE =====
        System.out.println("\n=== CURRENT INVENTORY ===");
        inventory.display();

        // ===== BOOKING SIMULATION =====
        if (inventory.bookRoom("Single Room")) {
            bookings.add("Alice booked Single Room");
        }

        if (inventory.bookRoom("Double Room")) {
            bookings.add("Bob booked Double Room");
        }

        System.out.println("\n=== AFTER BOOKING ===");
        inventory.display();

        // ===== SAVE STATE =====
        SystemState state = new SystemState(inventory.getInventory(), bookings);
        PersistenceService.save(state, FILE);

        // ===== SHOW BOOKINGS =====
        System.out.println("\n=== BOOKING HISTORY ===");
        for (String b : bookings) {
            System.out.println(b);
        }

        System.out.println("\n✅ Program executed successfully");
    }
}