import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
		try (Scanner scanner = new Scanner(System.in)) {
			while(true) {
				System.out.print("> ");
				String input = scanner.nextLine();
				game.handleInput(input);
			}
		}

	}

}
