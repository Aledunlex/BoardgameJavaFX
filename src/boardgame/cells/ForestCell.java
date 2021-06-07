package boardgame.cells;

import boardgame.Cell;
import boardgame.Resource;
import boardgame.resources.Wood;

/**
 * A sub-class of Cell, creates a ForestCell object, which will then be used by
 * a Fabrice to create a BoardGame. Allows a Player to collect Wood.
 */

public class ForestCell extends Cell {

	/**
	 * Builds a forest cell of given x and y coordinates, cost and gold values, and
	 * maxUnitSize
	 *
	 **/
	public ForestCell(int xIndex, int yIndex, int cost, int gold, int maxUnit, int value) {
		super(xIndex, yIndex, cost, gold, maxUnit, value);
		this.Id = "ForestCell";
	}

	@Override
	public Resource getResource() {
		return new Wood(this.value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ForestCell) {
			Cell other = (ForestCell) o;
			return this.maxUnitSize == other.getMaxUnit() && this.gold == other.getGold()
					&& this.cost == other.getCost() && this.xIndex == other.getX() && this.yIndex == other.getY()
					&& this.value == other.getValue();
		} else {
			return false;
		}
	}

	@Override
	public String display() {
		return "[F]";
	}

}
