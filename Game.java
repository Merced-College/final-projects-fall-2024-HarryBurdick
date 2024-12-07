import java.util.LinkedHashMap;

public class Game {
    private Player player;
    private Location currentLocation;
    private Location dungeonEntrance, darkHallway, lockedRoom, secretRoom, stairwell, tunnel, undergroundCityEntrance, tunnelAlongWall, tunnelAlongWallCavern, lostCity, intersection, stables, armory, castleKeep;
    private LinkedHashMap<String, Runnable> commands;
    private int riddleAttempts;
    private boolean hasWon;

    public Game() {
        player = new Player();
        commands = new LinkedHashMap<>();
        initializeLocations();
        setupCommandsForLocation();
        currentLocation = dungeonEntrance;
        riddleAttempts = 0;
        hasWon = false;
    }

    public void start() {
        System.out.println("Welcome to The Forgotten Keep!");
        while (true) {
            System.out.println("\nCurrent Location: " + currentLocation.getName());
            System.out.println(currentLocation.getDescription());
            System.out.print("\nWhat would you like to do? ");
            String input = GameInput.getInput().toLowerCase();

            if (commands.containsKey(input)) {
                commands.get(input).run();
            } else {
                System.out.println("Command not recognized. Type 'help' for available commands.");
            }
        }
    }

    public void handleInput(String input) {
        String command = input.toLowerCase(); // Convert to lowercase for case-insensitivity
        if (commands.containsKey(command)) {
            commands.get(command).run();
        } else {
            System.out.println("Command not recognized. Type 'help' for available commands.");
        }
    }

    private void setupCommandsForLocation() {
        commands.clear();
        commands.put("look", this::lookAround);
        commands.put("search", this::searchLocation);
        commands.put("pick up", this::pickUpItem);
        commands.put("use", this::useItem);
        commands.put("inspect", this::inspectItem);
        commands.put("check inventory", this::checkInventory);
        commands.put("check stats", this::checkStats);
        commands.put("move north", () -> move("north"));
        commands.put("move south", () -> move("south"));
        commands.put("move east", () -> move("east"));
        commands.put("move west", () -> move("west"));
        commands.put("help", this::showHelp);

        // Add the flip levers command if the player is in the locked room and has the required items
        if (currentLocation == lockedRoom && player.getItem("Torch") != null && player.getItem("Ancient Coin") != null) {
            commands.put("flip levers", this::flipLevers);
        }

        // Add the solve riddle command if the player is in the tunnelAlongWallCavern
        if (currentLocation == tunnelAlongWallCavern) {
            commands.put("solve riddle", this::solveRiddle);
        }

        // Add the find treasure command if the player is in the castleKeep
        if (currentLocation == castleKeep) {
            commands.put("find treasure", this::findTreasure);
        }
    }

    private void lookAround() {
        System.out.println("You look around.");
        currentLocation.displayExits();
        if (currentLocation == tunnelAlongWallCavern) {
            System.out.println("You see a talking statue in the cavern.");
        }
    }

    private void checkInventory() {
        player.showInventory();
    }

    private void checkStats() {
        System.out.println("Your current stats:");
        System.out.println("Health: " + player.getHealth());
        System.out.println("Mana: " + player.getMana());
    }

    private void searchLocation() {
        System.out.println("Searching...");
        if (currentLocation.getItems().isEmpty()) {
            System.out.println("You find nothing of interest here.");
        } else {
            System.out.println("You found the following items:");
            for (Item item : currentLocation.getItems()) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }

    private void pickUpItem() {
        System.out.println("What would you like to pick up?");
        String itemName = GameInput.getInput();
        Item item = currentLocation.removeItem(itemName);
        if (item != null) {
            player.addItem(item);
        } else {
            System.out.println("There's no item called '" + itemName + "' here.");
        }
    }

    private void useItem() {
        System.out.println("Which item would you like to use?");
        String itemName = GameInput.getInput();
        Item item = player.getItem(itemName);
        if (item != null) {
            System.out.println("You use the " + item.getName() + ".");
            item.use(); // Trigger the item's behavior
        } else {
            System.out.println("You don't have an item called '" + itemName + "' in your inventory.");
        }
    }

    private void inspectItem() {
        System.out.println("Which item would you like to inspect?");
        String itemName = GameInput.getInput();
        Item item = player.getItem(itemName);
        if (item != null) {
            System.out.println("You inspect the " + item.getName() + ":");
            System.out.println(item.getInspectionDetail());
        } else {
            System.out.println("You don't have an item called '" + itemName + "' in your inventory.");
        }
    }

    private void move(String direction) {
        if (currentLocation.getExits().containsKey(direction)) {
            currentLocation = currentLocation.getExits().get(direction);
            System.out.println("You move " + direction + " to " + currentLocation.getName());

            // Check if the player has entered the locked room and has the required items
            if (currentLocation == lockedRoom && player.getItem("Torch") != null && player.getItem("Ancient Coin") != null) {
                System.out.println("You notice three levers on the wall facing east.");
                commands.put("flip levers", this::flipLevers);
            }

            // Check if the player has entered the underground city entrance
            if (currentLocation == undergroundCityEntrance) {
                System.out.println("As you enter the underground city, the entrance collapses behind you!");
                // Remove the exit leading back to the previous location
                undergroundCityEntrance.getExits().remove("south");
            }

            // Check if the player has entered the tunnelAlongWallCavern
            if (currentLocation == tunnelAlongWallCavern) {
                System.out.println("You see a talking statue in the cavern.");
                commands.put("solve riddle", this::solveRiddle);
            }

            // Check if the player has entered the lost city
            if (currentLocation == lostCity) {
                System.out.println("As you enter the lost city, the gate closes behind you!");
                // Remove the exit leading back to the previous location
                lostCity.getExits().remove("south");
            }

            // Check if the player has entered the castle keep
            if (currentLocation == castleKeep) {
                System.out.println("You notice something shiny hidden in the corner.");
                commands.put("find treasure", this::findTreasure);
            }

            // Check if the player has won the game
            if (currentLocation == castleKeep && hasWon) {
                System.out.println("Congratulations! You have found the hidden treasure and won the game!");
                System.exit(0); // End the game
            }
        } else {
            System.out.println("You cannot move in that direction.");
        }
    }

    private void showHelp() {
        System.out.println("Available Commands:");
        for (String command : commands.keySet()) {
            System.out.println("- " + command);
        }
    }

    private void flipLevers() {
        if (currentLocation == lockedRoom) {
            if (player.getItem("Torch") != null && player.getItem("Ancient Coin") != null) {
                System.out.println("You flip the levers in the pattern shown on the coin: up, down, down.");
                System.out.println("A secret door opens behind you!");
                lockedRoom.addExit("west", secretRoom);
                commands.remove("flip levers"); // Remove the command after using it
            } else {
                System.out.println("I'm not sure what to do, maybe there's something I can find to help me.");
            }
        } else {
            System.out.println("There are no levers to flip here.");
        }
    }

    private void solveRiddle() {
        if (currentLocation == tunnelAlongWallCavern) {
            if (riddleAttempts < 3) {
                System.out.println("The statue speaks: 'I speak without a mouth and hear without ears. I have no body, but I come alive with the wind. What am I?'");
                System.out.println("You have " + (3 - riddleAttempts) + " free attempts left.");
            } else {
                System.out.println("The statue speaks: 'I speak without a mouth and hear without ears. I have no body, but I come alive with the wind. What am I?'");
                System.out.println("You must feed the statue blood to guess again.");
                player.setHealth(player.getHealth() - 10); // Take damage
                System.out.println("You feed the statue blood and take 10 damage. Your health is now " + player.getHealth() + ".");
            }

            System.out.print("Your answer: ");
            String answer = GameInput.getInput().toLowerCase();
            if (answer.equals("echo")) {
                System.out.println("The statue speaks: 'Correct! You have solved the riddle.'");
                // Open the front gate to the city
                undergroundCityEntrance.addExit("north", lostCity);
                undergroundCityEntrance.setDescription("An opening revealing a lost underground city with the front gate now open.");
                System.out.println("The front gate to the city opens!");
                // Remove the solve riddle command
                commands.remove("solve riddle");
                System.out.println("The statue becomes dormant.");
            } else {
                System.out.println("The statue speaks: 'Incorrect. Try again.'");
                riddleAttempts++;
            }
        } else {
            System.out.println("There is no riddle to solve here.");
        }
    }

    private void findTreasure() {
        if (currentLocation == castleKeep) {
            System.out.println("You find a hidden treasure chest in the corner!");
            System.out.println("Inside the chest, you find a shiny gem.");
            player.addItem(new Item(
                "Shiny Gem",
                "A valuable gem that sparkles with brilliance.",
                "This gem is worth a fortune.",
                "special",
                () -> {
                    System.out.println("You admire the shiny gem. It's truly magnificent.");
                    hasWon = true; // Set the win condition to true
                }
            ));
            commands.remove("find treasure"); // Remove the command after using it
        } else {
            System.out.println("There is no treasure to find here.");
        }
    }

    private void initializeLocations() {
        dungeonEntrance = new Location("Dungeon Entrance", "You are at the entrance of a dark, crumbling dungeon. You see an unlit torch laying on the ground outside the dungeon entrance.");
        darkHallway = new Location("Dark Hallway", "A narrow hallway lit by flickering torches. To the north, you see a locked door.");
        lockedRoom = new Location("Locked Room", "A room filled with cobwebs and old chests. The air smells of dust and decay.");
        secretRoom = new Location("Secret Room", "A hidden room with a stairwell leading down.");
        stairwell = new Location("Stairwell", "A long, winding stairwell leading down into darkness.");
        tunnel = new Location("Tunnel", "A narrow tunnel leading to an unknown destination.");
        undergroundCityEntrance = new Location("Underground City Entrance", "An opening revealing a lost underground city closed off by a large gate.");
        tunnelAlongWall = new Location("Tunnel along the wall of the city entrance", "You walk along the outside of the wall of the city into a tunnel that goes along it.");
        tunnelAlongWallCavern = new Location("Cavern at the end of the tunnel", "After walking into the tunnel you find yourself at a small cavern network.");
        lostCity = new Location("Lost City", "You have entered the lost underground city. The buildings have no visible doors.");
        intersection = new Location("Intersection", "You are at a four-way intersection. To the west are the stables, to the east is the armory, and to the north is the castle keep.");
        stables = new Location("Stables", "You are at the stables. Horses neigh softly in their stalls.");
        armory = new Location("Armory", "You are outside the armory. The building looks well-fortified.");
        castleKeep = new Location("Castle Keep", "You are at the entrance of the castle keep. The grand doors are firmly shut.");

        // Entrance to dungeon (only north)
        dungeonEntrance.addExit("north", darkHallway);

        // Hallway entering dungeon (exits: south, north after unlocking door)
        darkHallway.addExit("south", dungeonEntrance);

        // Locked room (exits: south, west after doing lever puzzle)
        lockedRoom.addExit("south", darkHallway);

        // Secret room (exits: east, north)
        secretRoom.addExit("north", stairwell);
        secretRoom.addExit("east", lockedRoom);

        // Stairwell (exits: south, north)
        stairwell.addExit("south", secretRoom);
        stairwell.addExit("north", tunnel);

        // Tunnel (exits: south, north)
        tunnel.addExit("south", stairwell);
        tunnel.addExit("north", undergroundCityEntrance);

        // Exit collapses
        undergroundCityEntrance.addExit("west", tunnelAlongWall);

        // Tunnel along wall (exits: east, west)
        tunnelAlongWall.addExit("west", tunnelAlongWallCavern);
        tunnelAlongWall.addExit("east", undergroundCityEntrance);

        // Tunnel along wall cavern (exits: east)
        tunnelAlongWallCavern.addExit("east", tunnelAlongWall);

        // Lost City (exits: north)
        lostCity.addExit("north", intersection);

        // Intersection (exits: south, west, east, north)
        intersection.addExit("south", lostCity);
        intersection.addExit("west", stables);
        intersection.addExit("east", armory);
        intersection.addExit("north", castleKeep);

        dungeonEntrance.addItem(new Item(
            "Torch",
            "A simple torch to light your way.",
            "It's a well-used torch with a charred tip.",
            "special",
            () -> System.out.println("You light the torch. The flames dance and provide warmth.")
        ));

        darkHallway.addItem(new Item(
            "Rusted Key",
            "An old rusted key",
            "The key is rusted and decayed, it's seen better days",
            "special",
            () -> {
                // Check if the player is at the dark hallway and the door is locked
                if (currentLocation == darkHallway) {
                    if (darkHallway.getExits().containsKey("north")) {
                        //System.out.println("The door is already unlocked.");
                    } else {
                        // Unlock the door
                        darkHallway.addExit("north", lockedRoom);
                        darkHallway.setDescription("A narrow hallway lit by flickering torches. To the north, the door has been unlocked.");
                        System.out.println("You use the key. The door is unlocked, but the key crumbles away.");

                        // Remove the key from the inventory
                        player.useItem("Rusted Key");
                    }
                } else {
                    System.out.println("There's no door to unlock here.");
                }
            }
        ));

        lockedRoom.addItem(new Item(
            "Ancient Coin",
            "An old coin with strange engravings.",
            "The coin depicts three arrows one facing up, one facing down, and the last facing down. Written along the bottom it says 'The truth lies within the keep.'",
            "special",
            () -> System.out.println("The coin feels oddly warm to the touch, as if it was left here recently.")
        ));
    }
}
