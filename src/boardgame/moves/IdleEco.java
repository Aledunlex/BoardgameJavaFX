package boardgame.moves;

import java.util.Collection;

import boardgame.Cell;
import boardgame.Player;

/**
 * An extension of MoveAdapter and sub-class of Idle, creates an IdleEco Move to
 * be added to a Game's list of Moves, specifically designed for the EcoGame.
 */

public class IdleEco extends Idle {

	public IdleEco() {
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public void onExecution(Player player) {
		super.onExecution(player);
		int gain = 0;
		Collection<Cell> allCells = player.allControlledCells();
		for (Cell cell : allCells) {
			gain += cell.getGold();
			player.setNeedQty(cell.getGold() + player.getNeedQty());
		}
		if (gain > 0) {
			System.out.println("...and wins " + gain + player.needToString(gain) + ".");
		}

	}
}
