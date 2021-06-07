package boardgame.moves;

import java.util.ArrayList;
import java.util.Collections;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.MoveAdapter;
import boardgame.Player;
import boardgame.Unit;

/**
 * An extension of MoveAdapter, creates a FixNeed Move to be added to a Game's
 * list of Moves.
 */

public class FixNeed extends MoveAdapter {

	private int satisfiableNeed;
	private int rqdNeed;
	private int minimumNeed;
	/** Only used for display purposes */
	private int iteration;

	public FixNeed() {
	}

	/** Added for testing */
	public int getRqdNeed() {
		return this.rqdNeed;
	}

	/** Added for testing */
	public void setRqdNeed(int set) {
		this.rqdNeed = set;
	}

	/**
	 * See @MoveAdapter
	 */
	public void onExecution(Player player) {

		BoardGame board = player.getPlayingGame().getBoard();
		satisfiableNeed = player.getNeedQty();
		rqdNeed = 0;
		ArrayList<Integer> allCosts = new ArrayList<Integer>();

		iteration = 0;
		if (player.allDeployedUnits().isEmpty())
			System.out.println(player.toString() + " doesn't have any unit.");

		for (Cell cell : player.allControlledCells()) {
			iteration++;
			boolean firstUnit = (iteration == 1);

			Unit unit = cell.getUnit();
			int cost = cell.getCost() * unit.getSize();
			allCosts.add(cost);
			rqdNeed += cost;
			System.out.print(unit.typeToString(firstUnit) + " at " + cell.toString() + " needs " + cost
					+ (firstUnit ? player.needToString(cost) : "") + "; ");
		}
		if (rqdNeed != 0)
			System.out.println(player.toString() + " has " + player.getNeedQty()
					+ player.needToString(player.getNeedQty()) + ".\n");

		if (!allCosts.isEmpty())
			minimumNeed = Collections.min(allCosts);

		int diff = satisfiableNeed - rqdNeed;
		if (diff >= 0) {
			for (Unit unit : player.allDeployedUnits()) {
				this.satisfying(player, unit);
			}
			diff = satisfiableNeed - rqdNeed;
		} else {
			while (diff < 0 && player.allDeployedUnits().size() > 0) {
				System.out.println("Needing " + rqdNeed + ", but owning " + satisfiableNeed + ".\n");
				this.sacrificeSelection(player, board);
				diff = satisfiableNeed - rqdNeed;
			}
			for (Unit unit : player.allDeployedUnits()) {
				this.satisfying(player, unit);
			}
		}
	}

	/**
	 * Satisfies the need of one unit and displays it; counts the need owned amount
	 * of necessities for the player, and the unit if applicable, then removes the
	 * unit from the list of units to be satisfied This method depends on the size
	 * of the unit
	 *
	 * @param player satisfying the need of its unit
	 * @param unit   to be satisfied
	 */
	public void satisfying(Player player, Unit unit) {
		satisfiableNeed = player.getNeedQty();
		Cell cell = unit.getCell();
		int cost = cell.getCost() * unit.getSize();
		rqdNeed -= cost;
		player.setNeedQty(satisfiableNeed - cost);
		satisfiableNeed = player.getNeedQty();
		System.out.print("\n" + unit.toString() + " is given " + cost + player.needToString(cost) + ".");
		if (unit.needsSalary()) {
			int currentNecessities = unit.getGold();
			unit.setGold(currentNecessities + cost);
			System.out.print(".. and now has " + unit.getGold() + player.needToString(unit.getGold()) + ".\n");
		}
		System.out.println("\n" + player.toString() + " now has " + satisfiableNeed
				+ player.needToString(satisfiableNeed) + " left.");

	}

	/**
	 * Lets the player select the coordinates of a unit to be sacrificed if
	 * necessary
	 *
	 * @param player sacrificing its unit
	 * @param board  on which the game takes place
	 */
	public void sacrificeSelection(Player player, BoardGame board) {
		if (satisfiableNeed < minimumNeed)
			this.everybodyIsGone(player, board);
		else {
			System.out.println("Selecting coordinates to choose which unit to let go...\n");
			System.out.println("Coordinates of units available : ");
			for (Cell cell : player.allControlledCells()) {
				System.out.println(" => " + cell.toString() + "\n");
			}
			Unit selected = player.getStrategy().chooseUnit(player.allDeployedUnits());

			if (player.allDeployedUnits().contains(selected)) {
				this.sacrificeOccuring(player, selected, board);
			} else {
				System.out.println(player.toString() + " doesn't own a unit on this cell ! One more try...");
				this.sacrificeSelection(player, board);
			}
		}
	}

	/**
	 * The unit on the selected cell will be applied the quit() method
	 *
	 * @param player sacrificing its unit
	 * @param unit   to be sacrificed
	 * @param board  on which the game takes place
	 */
	public void sacrificeOccuring(Player player, Unit unit, BoardGame board) {
		Cell unitCell = unit.getCell();
		System.out.println(unit.toString() + " quit!\n");
		rqdNeed -= (unitCell.getCost() * unit.getSize());
		player.removeUnit(unit);
		board.getAvailableCells().add(unitCell);
	}

	/**
	 * If this method is called, the player loses all their units and the
	 * corresponding cells. All units call the quit method and their cells are added
	 * to the availableCells list of the Board, and the cells and units are removed
	 * from the player's lists
	 *
	 * @param player about to lose every unit
	 * @param board  on which the game takes place
	 */
	public void everybodyIsGone(Player player, BoardGame board) {
		for (Unit unit : player.allDeployedUnits()) {
			board.getAvailableCells().add(unit.getCell());
			unit.quit();
		}
		player.allDeployedUnits().clear();
		player.allControlledCells().clear();
		rqdNeed = 0;
		System.out.println(player.toString() + " just lost all their units!");
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public String display() {
		return "Addressing Needs";
	}

}
