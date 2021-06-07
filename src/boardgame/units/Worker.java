package boardgame.units;

import boardgame.Cell;
import boardgame.Player;
import boardgame.Unit;

/**
 * A sub-class of Unit, creates a Worker unit for the EcoGame.
 */

public class Worker extends Unit {

	private static final int WORKER_SIZE = 1;

	/**
	 * Deploys a Worker on a specified cell for the given player, and of a chosen
	 * size
	 *
	 * @param cell on which the Worker will first be deployed
	 * @param team whose team the Worker is on, ergo which player owns it
	 **/
	public Worker(Cell cell, Player team) {
		super(cell, team);
		this.size = WORKER_SIZE;
		this.unitType = "worker";
	}

	@Override
	public boolean needsSalary() {
		return true;
	}

}
