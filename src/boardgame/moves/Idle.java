package boardgame.moves;

import boardgame.MoveAdapter;
import boardgame.Player;

/**
 * An extension of MoveAdapter, creates an Idle Move to be added to a Game's
 * list of Moves.
 */

public class Idle extends MoveAdapter {

	public Idle() {
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public void onExecution(Player player) {
		System.out.println(player.toString() + " does nothing this round...");
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public String display() {
		return "Do nothing";
	}

}
