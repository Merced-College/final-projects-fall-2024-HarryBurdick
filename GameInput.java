import java.util.Scanner;

public class GameInput {
    private static final Scanner scanner = new Scanner(System.in);

    // Reads and returns the player's input
    public static String getInput() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }
}
