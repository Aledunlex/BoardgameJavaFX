package boardgame.units;

import boardgame.Cell;
import boardgame.Player;
import boardgame.Unit;
import boardgame.players.WarPlayer;

/**
 * A sub-class of Unit, creates an Army unit for the WarGame.
 */

public class Army extends Unit {

	private static final int TEAM_GAIN_AFTER_QUIT = 1;

	/**
	 * Deploys an Army on a specified cell for the given player, and of a chosen
	 * size
	 *
	 * @param cell on which the Army will first be deployed
	 * @param team whose team the Army is on, ergo which player owns it
	 * @param size of the Army
	 **/
	public Army(Cell cell, Player team, int size) {
		super(cell, team);
		this.size = size;
		this.unitType = "army";
	}

	/** see @Player */
	@Override
	public boolean needsSalary() {
		return false;
	}

	/**
	 * see @Player When an Army quits, their Player gains one gold coin.
	 */
	@Override
	public void quit() {
		WarPlayer player = (WarPlayer) team;
		int currGold = player.getGold();
		player.setGold(currGold + TEAM_GAIN_AFTER_QUIT);
		int newGold = player.getGold();
		System.out.println(team.toString() + " had " + currGold + " gold coin" + (currGold == 1 ? "" : "s")
				+ ", now has " + newGold + ".");
		super.quit();
	}

}
