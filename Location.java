import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private String description;
    private Map<String, Location> exits;
    private List<Item> items; // Items present in this location

    // Constructor
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Set a new description
    public void setDescription(String description) {
        this.description = description;
    }

    // Add an exit
    public void addExit(String direction, Location location) {
        exits.put(direction.toLowerCase(), location);
    }

    // Get an exit
    public Location getExit(String direction) {
        return exits.get(direction);
    }

    // Display exits
    public void displayExits() {
        if (exits.isEmpty()) {
            System.out.println("There are no visible exits.");
        } else {
            System.out.println("Exits:");
            for (String direction : exits.keySet()) {
                System.out.println("- " + direction);
            }
        }
    }

    // Add an item to the location
    public void addItem(Item item) {
        items.add(item);
    }

    // Remove an item from the location
    public Item removeItem(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                return items.remove(i);
            }
        }
        return null;
    }

    // Display items in the location
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("There are no items to be found here.");
        } else {
            System.out.println("You found the following items:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }
    public HashMap<String, Location> getExits() {
        return (HashMap<String, Location>) exits;
    }
    public List<Item> getItems() {
        return items;
    }
}
