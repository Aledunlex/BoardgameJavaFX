package boardgame;

import java.text.Normalizer;

import boardgame.fabrices.FabriceArmy;
import boardgame.game.GameWar;
import boardgame.players.WarPlayer;
import boardgame.strategy.InputStrat;
/*import boardgame.util.*;  a utiliser avec les constantes de ConsoleColor.java */
import boardgame.strategy.RandomStrat;

/**
 * This main program allows to start a WarGame with any number of players. first
 * player is an input player all the other one are random player. Valid player
 * names must be entered as parameter upon execution.
 */

public class InputWarGameMain {

	public static void main(String args[]) {

		Strategy strat1 = new InputStrat();
		Strategy strat2 = new RandomStrat();
		int nbPlayers = args.length;
		if (nbPlayers < 2)
			nbPlayers = 2;

		BoardGame board = createArmyBoard(nbPlayers);
		GameWar game = new GameWar(board, 10);
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
			WarPlayer philippe = new WarPlayer("Gandhi", strat1);
			WarPlayer musclor = new WarPlayer("Odette", strat2);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
		} else {
			WarPlayer inputPlayer = new WarPlayer(args[0], strat1);
			game.addPlayer(inputPlayer);

			for (int i = 1; i < args.length; i++) {
				WarPlayer player = new WarPlayer(args[i], strat2);
				game.addPlayer(player);
			}
		}

		// System.out.println("# Beginning of war main #");

		game.play();
		// System.out.println("\n # End of war main #");
	}

	/**
	 * Creates a new board until it has at least as many available initial Cells
	 * than the amount of players
	 */
	private static BoardGame createArmyBoard(int args) {
		BoardGame board = new BoardGame(10, 10, new FabriceArmy());
		while (board.getAvailableCells().size() < args) {
			board = createArmyBoard(args);
		}
		return board;
	}
}
