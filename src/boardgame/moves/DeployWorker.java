package boardgame.moves;

import boardgame.Cell;
import boardgame.Player;
import boardgame.units.Worker;

/**
 * An extension of MoveAdapter and sub-class of Deploy, creates a DeployWorker
 * Move to be added to a Game's list of Moves, specifically to deploy a Worker
 * unit in the EcoGame.
 */

public class DeployWorker extends Deploy {

	public DeployWorker() {
	}

	/**
	 * Creates and adds a worker to the board
	 *
	 * @param cell   Cell where worker need to be
	 * @param player Player deploying the worker
	 */
	@Override
	public Worker placeUnit(Cell cell, Player player) {
		Worker worker = new Worker(cell, player);
		return worker;
	}

	/**
	 * See @MoveAdapter
	 */
	public String display() {
		return "Assign a worker";
	}

}
