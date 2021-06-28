package boardgame;

import java.text.Normalizer;

import boardgame.fabrices.FabriceArmy;
import boardgame.game.GameWar;
import boardgame.players.WarPlayer;
/*import boardgame.util.*;  a utiliser avec les constantes de ConsoleColor.java */
import boardgame.strategy.RandomStrat;

/**
 * This main program allows to start a WarGame with any number of players. Valid
 * player names must be entered as parameter upon execution.
 */

public class WarGameMain {

	private static final int WAR_MAX_ROUNDS = 10;

	public static void main(String args[]) {
		System.out.println("# Beginning of war main #");

		Strategy strat = new RandomStrat();
		int nbPlayers = args.length;
		if (nbPlayers < 2)
			nbPlayers = 2;

		BoardGame board = createArmyBoard(nbPlayers);
		GameWar game = new GameWar(board, WAR_MAX_ROUNDS);
		boolean oneWrongArg = false;

		for (String arg : args) {
			String tempArg = Normalizer.normalize(arg, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			if (!tempArg.matches("^[a-zA-Z]*$")) {
				oneWrongArg = true;
			}
		}

		if (oneWrongArg || args.length < 2) {
			System.out.println(
					"You didn't enter enough player names as parameters (needing at least 2), or one of your inputs isn't correct text.");
			System.out.println("Defaulting to 2 players : Philippe and Musclor.");
			WarPlayer philippe = new WarPlayer("Philippe", strat);
			WarPlayer musclor = new WarPlayer("Musclor", strat);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
		} else {
			for (String name : args) {
				WarPlayer player = new WarPlayer(name, strat);
				game.addPlayer(player);
			}
		}

		game.play();

		System.out.println("\n # End of war main #");
	}

	/**
	 * Creates a new board until it has at least as many initial available Cells
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
