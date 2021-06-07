package boardgame.cells;

import boardgame.Cell;
import boardgame.Resource;

/**
 * A sub-class of Cell, creates a OceanCell object, which will then be used by a
 * Fabrice to create a BoardGame. Units can not be placed on this type of Cell,
 * and no resource can be harvested either.
 */

public class OceanCell extends Cell {

	/**
	 * Builds an ocean cell of given x and y coordinates, cost and gold values, and
	 * maxUnitSize
	 * 
	 **/
	public OceanCell(int xIndex, int yIndex, int cost, int gold, int maxUnit, int value) {
		super(xIndex, yIndex, cost, gold, maxUnit, value);
		this.Id = "OceanCell";
	}

	@Override
	/**
	 * An Ocean Cell will not produce any resource in our games.
	 */
	public Resource getResource() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof OceanCell) {
			Cell other = (OceanCell) o;
			return this.maxUnitSize == other.getMaxUnit() && this.gold == other.getGold()
					&& this.cost == other.getCost() && this.xIndex == other.getX() && this.yIndex == other.getY()
					&& this.value == other.getValue();
		} else {
			return false;
		}
	}

	@Override
	public String display() {
		return "~~~";
	}
}
