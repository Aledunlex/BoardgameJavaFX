package boardgame;

import java.text.Normalizer;

import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
import boardgame.strategy.RandomStrat;

/**
 * This main program allows to start an EcoGame with any number of players.
 * Valid player names must be entered as parameter upon execution (at least 2).
 */

public class EcoGameMain {

	private static final int ECO_MAX_ROUNDS = 6;

	public static void main(String args[]) {
		System.out.println("# Beginning of eco main #");

		Strategy strat = new RandomStrat();
		int nbPlayers = args.length;
		if (nbPlayers < 2)
			nbPlayers = 2;

		BoardGame board = createEcoBoard(nbPlayers);
		GameEco game = new GameEco(board, ECO_MAX_ROUNDS);
		boolean oneWrongArg = false;

		for (String arg : args) {
			String tempArg = Normalizer.normalize(arg, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			if (!tempArg.matches("^[a-zA-Z]*$")) {
				oneWrongArg = true;
			}
		}

		if (oneWrongArg || args.length < 2) {
			System.out.println(
					"\nWARNING : You didn't enter enough player names as parameters (needing at least 2), or one of your inputs isn't correct text.");
			System.out.println("Defaulting to 2 players : Gandhi and Odette.");
			EcoPlayer philippe = new EcoPlayer("Gandhi", strat);
			EcoPlayer musclor = new EcoPlayer("Odette", strat);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
		} else {
			for (String name : args) {
				EcoPlayer player = new EcoPlayer(name, strat);
				game.addPlayer(player);
			}
		}

		game.play();

		System.out.println("\n # End of eco main #");
	}

	/**
	 * Creates a new board until it has at least as many available initial Cells
	 * than the amount of players
	 */
	private static BoardGame createEcoBoard(int args) {
		BoardGame board = new BoardGame(10, 10, new FabriceEcolo());
		while (board.getAvailableCells().size() < args) {
			board = createEcoBoard(args);
		}
		return board;
	}

}
