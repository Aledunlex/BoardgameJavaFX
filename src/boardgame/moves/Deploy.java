package boardgame.moves;

import java.util.ArrayList;
import java.util.Arrays;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.MoveAdapter;
import boardgame.Player;
import boardgame.Unit;

/**
 * An extension of MoveAdapter, creates a Deploy Move to be added to a Game's
 * list of Moves.
 */

public abstract class Deploy extends MoveAdapter {

	/* The cell created in this execute method */
	protected Cell cell;
	/* For display purposes only */
	private static final ArrayList<Character> VOWELS = new ArrayList<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

	/**
	 * A move used to deploy a unit on the board
	 */
	public Deploy() {
	}

	/**
	 * See @MoveAdapter
	 */
	public void onExecution(Player player) {

		BoardGame board = player.getPlayingGame().getBoard();
		Cell newCell = player.getStrategy().chooseCell(board);
		Unit u = this.placeUnit(newCell, player);

		/* For display purposes only */
		String uDisplay = u.typeToString(false);
		String aOrAn = ((VOWELS.contains(uDisplay.charAt(0))) ? "an " : "a ");

		if ((!newCell.usableInThisGame() || newCell.isBusy()) && u.getSize() <= newCell.getMaxUnit()) {
			System.out.println("Can't deploy this " + uDisplay + " on " + newCell.toString() + " ! ");
			this.onExecution(player);
		} else {
			if (!player.addDeployedUnit(u)) {
				System.out.println(player.toString() + " can't deploy this unit ! Back to action selection.");
				u = null;
				cell = null;
				player.getStrategy().chooseMove(player.getPlayingGame().getTheMoves()).execute(player);
			} else {
				newCell.addUnit(u);
				cell = newCell;
				board.getAvailableCells().remove(newCell);
				System.out.println(
						player.toString() + " deployed " + aOrAn + uDisplay + " at " + newCell.toString() + ". ");
			}
		}
	}

	/**
	* Creates and places a unit on the board.
	*/
	public abstract Unit placeUnit(Cell cell, Player player);

}
