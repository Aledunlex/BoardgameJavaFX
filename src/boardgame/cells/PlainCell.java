package boardgame.cells;

import boardgame.Cell;
import boardgame.Resource;
import boardgame.resources.Wheat;

/**
 * A sub-class of Cell, creates a PlainCell object, which will then be used by a
 * Fabrice to create a BoardGame. Allows a Player to collect Wheat.
 */

public class PlainCell extends Cell {

	/**
	 * Builds a plain cell of given x and y coordinates, cost and gold values, and
	 * maxUnitSize
	 *
	 **/
	public PlainCell(int xIndex, int yIndex, int cost, int gold, int maxUnit, int value) {
		super(xIndex, yIndex, cost, gold, maxUnit, value);
		this.Id = "PlainCell";
	}

	@Override
	public Resource getResource() {
		return new Wheat(this.value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PlainCell) {
			Cell other = (PlainCell) o;
			return this.maxUnitSize == other.getMaxUnit() && this.gold == other.getGold()
					&& this.cost == other.getCost() && this.xIndex == other.getX() && this.yIndex == other.getY()
					&& this.value == other.getValue();
		} else {
			return false;
		}
	}

	@Override
	public String display() {
		return "[P]";
	}
}
