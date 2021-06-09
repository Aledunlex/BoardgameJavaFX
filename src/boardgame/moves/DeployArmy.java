package boardgame.moves;

import java.util.List;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.Player;
import boardgame.Unit;
import boardgame.players.WarPlayer;
import boardgame.units.Army;

/**
 * An extension of MoveAdapter and sub-class of Deploy, creates a DeployArmy
 * Move to be added to a Game's list of Moves, specifically to deploy an Army
 * unit in the WarGame. Entirely specific of the WarGame rules (casted types,
 * called methods, and display generated).
 */

public class DeployArmy extends Deploy {

	public static final int WAR_ARMY_GAINS_AFTER_WON_BATTLE = 2;
	public static final int WAR_ARMY_GAINS_AFTER_DEPLOYED_NEAR_ALLIES = 1;

	public DeployArmy() {
	}

	/**
	 * See @Move
	 */
	@Override
	public void execute(Player player) {
		super.execute(player);

		if (cell != null) {
			newCount(player);
			theFight(cell, player.getPlayingGame().getBoard());
		}
	}

	/**
	 * Creates and adds an army to the board.
	 *
	 * @param cell   Cell where army need to be
	 * @param player Player deploying the army
	 */
	@Override
	public Army placeUnit(Cell cell, Player player) {
		int size = player.getStrategy().chooseSize(1, cell.getMaxUnit() + 1);
		Army army = new Army(cell, player, size);
		return army;
	}

	/**
	 * Scans the surrounding and takes care of the fight between army on the
	 * surrounding cell, or resolves the situation if the other team
	 * is friendly.
	 *
	 * @param cell  where the army is placed
	 * @param board used for the game
	 */
	public void theFight(Cell cell, BoardGame board) {
		Army u = (Army) cell.getUnit();

		WarPlayer currentTeam = (WarPlayer) u.getTeam();
		List<Cell> surroundings = board.getAdjacentCell(cell);

		System.out.println("\n" + u.toString() + " is next to :");

		boolean allCleared = true;

		for (Cell around : surroundings) {
			if (around.usableInThisGame() && !around.isBusy())
				System.out.println(" → " + around.toString() + " (unoccupied)");
			else if (around.isBusy())
				System.out.println(" → " + around.getUnit().toString());

			int unitSize = u.getSize();
			int tacticalSize = u.getSize() + cell.getAdvantage();
			int unitGold = u.getGold();
			Army otherUnit = (Army) around.getUnit();

			if (otherUnit != null) {
				allCleared = false;
				WarPlayer otherTeam = (WarPlayer) otherUnit.getTeam();
				int otherSize = otherUnit.getSize();
				int otherTacticalSize = otherUnit.getSize() + around.getAdvantage();

				if (!otherTeam.equals(currentTeam)) {
					unitSize = tacticalSize;
					otherSize = otherTacticalSize;
				}

				if (otherSize < unitSize) {
					if (!otherTeam.equals(currentTeam)) {
						this.enemyEncounter(u, currentTeam, unitSize, unitGold, otherUnit, otherTeam, otherSize,
								around);
					} else {
						this.alliesEncounter(otherSize, around, otherUnit, unitGold, u, currentTeam);
					}
				}
			}
		}
		if (allCleared)
			System.out.println("...no one.");
	}

	/**
	 * Handles the situation in which the unit next to the placed unit is an enemy
	 *
	 * @param u           Army that just got placed
	 * @param currentTeam of the placed unit
	 * @param unitSize    size of the unit placed
	 * @param unitGold    gold of the unit
	 * @param otherUnit   one of the unit next to the unit that got placed
	 * @param otherTeam   of the unit next to the unit that got placed
	 * @param otherSize   size of the unit next to the unit that got placed
	 * @param around      cell next to the cell of o the unit that got placed
	 */
	private void enemyEncounter(Army u, WarPlayer currentTeam, int unitSize, int unitGold, Army otherUnit,
			WarPlayer otherTeam, int otherSize, Cell around) {
		System.out.println(" * NOUS SOMMES EN GUERRE!" + "\n --- Deployed army's size : " + u.getSize()
				+ ((unitSize == u.getSize()) ? "" : ", tactical Size : " + unitSize) + ".");
		System.out.println(
				" --- " + otherTeam.toString() + "'s unit is in a tough spot... (current size : " + otherUnit.getSize()
						+ ((otherSize == otherUnit.getSize()) ? "" : ", tactical Size : " + otherSize) + ")");

		if (otherUnit.getSize() == 1) {
			otherTeam.traitorousUnit(otherUnit, currentTeam);
			u.setGold(unitGold + WAR_ARMY_GAINS_AFTER_WON_BATTLE);

			System.out.println(" --- " + otherTeam.toString() + "'s army has been lost to " + currentTeam.getName()
					+ "! " + currentTeam.toString() + " gains control of " + around.toString() + ".");
			System.out.println(" --- " + currentTeam.toString() + "'s deployed army gained 2 coins of gold!\n");

		} else {
			otherUnit.setSize((int) Math.floor(otherUnit.getSize() / 2));

			System.out.println(" --- " + otherTeam.toString() + "'s army's size have been halved! Size is now : "
					+ otherUnit.getSize() + ".\n");
		}
	}

	/**
	 * Handles the situation in which the unit next to the placed unit is an ally
	 *
	 * @param otherSize   size of the unit next to the unit that got placed
	 * @param around      cell next to the cell of o the unit that got placed
	 * @param otherUnit   one of the unit next to the unit that got placed
	 * @param unitGold    gold of the unit
	 * @param u           Army that just got placed
	 * @param currentTeam Team that just place the unit
	 */
	private void alliesEncounter(int otherSize, Cell around, Army otherUnit, int unitGold, Army u,
			WarPlayer currentTeam) {
		System.out.print("\n" + otherUnit.toString() + " are allies...");
		if (otherSize < around.getMaxUnit()) {
			otherUnit.setSize(otherSize + 1);
			System.out.print(" but they're weakened! They grew by one soldier.\n");
		}
		u.setGold(unitGold + WAR_ARMY_GAINS_AFTER_DEPLOYED_NEAR_ALLIES);
		System.out.print("\n" + currentTeam.toString() + "'s deployed army gained 1 coin of gold ");
		System.out.print("(old gold : " + unitGold + ", new gold : " + u.getGold() + ").\n");
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public String display() {
		return "Deploy an army";
	}

	/**
	 * Displays on standard output the new number of available soldiers after a
	 * successful deployment.
	 *
	 * @param player for whom the new number of available soldiers must be displayed
	 */
	private void newCount(Player player) {
		WarPlayer realPlayer = (WarPlayer) player;
		int count = 0;
		String soldierDisplay = "soldier";
		for (Unit theseUnits : realPlayer.allDeployedUnits()) {
			count += theseUnits.getSize();
		}
		System.out.println(
				realPlayer.toString() + " now has " + count + " " + (count == 1 ? soldierDisplay : soldierDisplay + "s")
						+ " and " + realPlayer.getAvailableUnits() + " available.");
	}

}
