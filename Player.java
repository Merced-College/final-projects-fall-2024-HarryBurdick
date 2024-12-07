import java.util.ArrayList;
import java.util.List;

public class Player {
    private int health;
    private int mana;
    private List<Item> inventory;

    // Constructor
    public Player() {
        this.health = 100;
        this.mana = 50;
        this.inventory = new ArrayList<>();
    }

    // Getter for health
    public int getHealth() {
        return health;
    }

    // Setter for health
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(100, health)); // Ensure health is between 0 and 100
        System.out.println("Your health is now " + this.health + ".");
    }

    // Getter for mana
    public int getMana() {
        return mana;
    }

    // Setter for mana
    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(100, mana)); // Ensure mana is between 0 and 100
        System.out.println("Your mana is now " + this.mana + ".");
    }

    // Add an item to the inventory
    public void addItem(Item item) {
        inventory.add(item);
        System.out.println(item.getName() + " has been added to your inventory.");
    }

    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null; // Return null if the item is not found
    }

    // Display inventory contents
    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Inventory:");
            for (Item item : inventory) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }

    // Use an item by name
    public void useItem(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getName().equalsIgnoreCase(itemName)) {
                // Check if the item has a special action
                if (item.getType().equalsIgnoreCase("special") && item.getSpecialAction() != null) {
                    item.getSpecialAction().run(); // Execute the special action
                } else {
                    item.use(); // Default use behavior
                    System.out.println(item.getName() + " has been consumed.");
                }
                inventory.remove(i); // Remove the item after using it
                return;
            }
        }
        System.out.println("You don't have an item called '" + itemName + "' in your inventory.");
    }
}
