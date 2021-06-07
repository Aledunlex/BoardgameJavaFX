package boardgame.moves;

import java.util.HashSet;
import java.util.Set;

import boardgame.Cell;
import boardgame.MoveAdapter;
import boardgame.Player;
import boardgame.Resource;

/**
 * An extension of MoveAdapter, creates an Harvest Move to be added to a Game's
 * list of Moves.
 */

public class Harvest extends MoveAdapter {

	/**
	 * Used to determine if the display of each owned Resources equals the amount of
	 * already displayed Resources in execute()
	 */
	static int iteration;

	public Harvest() {
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public void onExecution(Player player) {
		iteration = 0;

		for (Cell cell : player.allControlledCells()) {
			Resource res = cell.getResource();
			player.addResource(res);
		}

		this.resDisplay(player);
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public String display() {
		return "Harvesting resources";
	}

	/**
	 * Displays on one line how many of all resources is owned by the player
	 *
	 * @param player having his resources displayed
	 */
	private void resDisplay(Player player) {
		if (!player.allControlledCells().isEmpty()) {
			Set<String> allRes = new HashSet<>();
			allRes.addAll(player.allResources().keySet());
			for (String res : player.allResources().keySet()) {
				if (player.getResAmnt(res) == 0 && allRes.contains(res)) {
					allRes.remove(res);
				}
			}
			System.out.print(player.toString() + " now has ");
			for (String thisRes : allRes) {
				int setSize = allRes.size();
				int amount = player.getResAmnt(thisRes);
				String amntDisplay = (amount == 1 ? "unit of " + thisRes : "units of " + thisRes);
				if (amount > 0 && setSize > 1) {
					iteration++;
					if (iteration != setSize) {
						System.out.print("[" + amount + " " + amntDisplay + "], ");
					} else {
						System.out.print("and [" + amount + " " + amntDisplay + "].\n");
					}
				} else if (setSize == 1) {
					System.out.print("[" + amount + " " + amntDisplay + "].\n");
				}
			}
		} else
			System.out.println(player.toString() + " doesn't have anything to harvest this round.");
	}

}
