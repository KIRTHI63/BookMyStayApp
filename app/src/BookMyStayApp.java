abstract class Room {
    protected String type;
    protected double price;
    protected int availableRooms;

    public Room(String type, double price, int availableRooms) {
        this.type = type;
        this.price = price;
        this.availableRooms = availableRooms;
    }

    public abstract void displayDetails();
}

// Single Room Class
class SingleRoom extends Room {

    public SingleRoom(int availableRooms) {
        super("Single Room", 1000, availableRooms);
    }

    @Override
    public void displayDetails() {
        System.out.println("Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Available: " + availableRooms);
        System.out.println("------------------------");
    }
}

// Double Room Class
class DoubleRoom extends Room {

    public DoubleRoom(int availableRooms) {
        super("Double Room", 2000, availableRooms);
    }

    @Override
    public void displayDetails() {
        System.out.println("Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Available: " + availableRooms);
        System.out.println("------------------------");
    }
}

// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom(int availableRooms) {
        super("Suite Room", 5000, availableRooms);
    }

    @Override
    public void displayDetails() {
        System.out.println("Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Available: " + availableRooms);
        System.out.println("------------------------");
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        System.out.println("Room Availability:\n");

        Room single = new SingleRoom(5);
        Room doubleRoom = new DoubleRoom(3);
        Room suite = new SuiteRoom(2);

        single.displayDetails();
        doubleRoom.displayDetails();
        suite.displayDetails();

        System.out.println("Application Terminated.");
    }
}
