package boardgame.cells;

import boardgame.Cell;
import boardgame.Resource;
import boardgame.resources.Rock;

/**
 * A sub-class of Cell, creates a MountainCell object, which will then be used
 * by a Fabrice to create a BoardGame. Allows a Player to collect Rock.
 */

public class MountainCell extends Cell {

	/**
	 * Builds a moutain cell of given x and y coordinates, cost and gold values, and
	 * maxUnitSize
	 * 
	 **/
	public MountainCell(int xIndex, int yIndex, int cost, int gold, int maxUnit, int value) {
		super(xIndex, yIndex, cost, gold, maxUnit, value);
		this.Id = "MountainCell";
	}

	@Override
	public Resource getResource() {
		return new Rock(this.value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MountainCell) {
			Cell other = (MountainCell) o;
			return this.maxUnitSize == other.getMaxUnit() && this.gold == other.getGold()
					&& this.cost == other.getCost() && this.xIndex == other.getX() && this.yIndex == other.getY()
					&& this.value == other.getValue();
		} else {
			return false;
		}
	}

	@Override
	public String display() {
		return "[M]";
	}

}
