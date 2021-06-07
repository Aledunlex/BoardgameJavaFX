package boardgame;

import java.text.Normalizer;

import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
import boardgame.strategy.InputStrat;
import boardgame.strategy.RandomStrat;

/**
 * This main program allows to start an EcoGame with any number of players first
 * player is an input player all the other one are random player. Valid player
 * names must be entered as parameter upon execution.
 */

public class InputEcoGameMain {

	public static void main(String args[]) {

		Strategy strat1 = new InputStrat();
		Strategy strat2 = new RandomStrat();
		int nbPlayers = args.length;
		if (nbPlayers < 2)
			nbPlayers = 2;

		BoardGame board = createEcoBoard(nbPlayers);
		GameEco game = new GameEco(board, 6);

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
			EcoPlayer philippe = new EcoPlayer("Gandhi", strat1);
			EcoPlayer musclor = new EcoPlayer("Odette", strat2);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
		} else {
			EcoPlayer inputPlayer = new EcoPlayer(args[0], strat1);
			game.addPlayer(inputPlayer);

			for (int i = 1; i < args.length; i++) {
				EcoPlayer player = new EcoPlayer(args[i], strat2);
				game.addPlayer(player);
			}
		}

		// System.out.println("# Beginning of eco main #");

		game.play();
		// System.out.println("\n # End of eco main #");
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
