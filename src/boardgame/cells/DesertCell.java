package boardgame.cells;

import boardgame.Cell;
import boardgame.Resource;
import boardgame.resources.Sand;

/**
 * A sub-class of Cell, creates a DesertCell object, which will then be used by
 * a Fabrice to create a BoardGame. Allows a Player to collect Sand.
 */

public class DesertCell extends Cell {

	/**
	 * Builds a desert cell of given x and y coordinates, cost and gold values, and
	 * maxUnitSize
	 *
	 **/
	public DesertCell(int xIndex, int yIndex, int cost, int gold, int maxUnit, int value) {
		super(xIndex, yIndex, cost, gold, maxUnit, value);
		this.Id = "DesertCell";
	}

	@Override
	public Resource getResource() {
		return new Sand(this.value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof DesertCell) {
			Cell other = (DesertCell) o;
			return this.maxUnitSize == other.getMaxUnit() && this.gold == other.getGold()
					&& this.cost == other.getCost() && this.xIndex == other.getX() && this.yIndex == other.getY()
					&& this.value == other.getValue();
		} else {
			return false;
		}
	}

	@Override
	public String display() {
		return "[D]";
	}

}
