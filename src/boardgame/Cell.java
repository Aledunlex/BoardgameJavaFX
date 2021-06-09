package boardgame;

/**
 * This abstract class defines the basics of what a Cell is going to be in most
 * games.
 */

public abstract class Cell {

	protected int gold;
	protected int maxUnitSize;
	protected Unit currentUnit;
	protected int xIndex;
	protected int yIndex;
	protected int cost;
	protected int value;
	protected int bonus;
	protected int tacticalAdvantage;
	protected String Id;
	protected BoardGame inBoard;

	/**
	 * Builds, on a board, a cell of given x and y coordinates, cost for a Unit to
	 * stay, gold values for staying on it, a maximum size for its Unit, and the
	 * value of every resource object harvested on it
	 *
	 * @param xIndex      the cell's horizontal coordinate
	 * @param yIndex      the cell's vertical coordinate
	 * @param cost        the cell's cost for the Unit that stands there at the end
	 *                    of a turn
	 * @param gold        the cell give when player do nothing
	 * @param value       the cell's resource value in gold
	 * @param maxUnitSize the cell's maxUnitSize value
	 **/
	public Cell(int xIndex, int yIndex, int cost, int gold, int maxUnitSize, int value) {
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		this.maxUnitSize = maxUnitSize;
		this.cost = cost;
		this.value = value;
		this.gold = gold;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}

	/**
	 * Returns the bonus value of the Cell
	 *
	 * @return the bonus value of the Cell
	 **/
	public int getBonus() {
		return this.bonus;
	}

	/**
	 * Sets the bonus value for the Cell; to be used in the game Fabric(e)
	 *
	 * @param bonus with the desired value
	 **/
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	/**
	 * Returns the (tactical) advantage value of the Cell
	 *
	 * @return the advantage value of the Cell
	 **/
	public int getAdvantage() {
		return this.tacticalAdvantage;
	}

	/**
	 * Sets the (tactical) advantage value for the Cell; to be used in the game Fabric(e)
	 *
	 * @param advantage with the desired value
	 **/
	public void setAdvantage(int advantage) {
		this.tacticalAdvantage = advantage;
	}

	/**
	 * Returns the gold value of the Cell
	 *
	 * @return the gold value of the Cell
	 **/
	public int getGold() {
		return this.gold;
	}

	/**
	 * Returns the Unit standing on the Cell
	 *
	 * @return the Unit standing on the Cell
	 **/
	public Unit getUnit() {
		return this.currentUnit;
	}

	/**
	 * Returns the value of the cell's resource in gold
	 *
	 * @return the value of the cell's resource in gold
	 **/
	public int getValue() {
		return this.value;
	}

	/**
	 * Adds a specified Unit on this cell, depending on its size. Returns true if
	 * it's possible to place the unit, false otherwise. Only effects this Cell's
	 * currentUnit attribute!
	 *
	 * @param unit to be deployed on this cell
	 * @return true if possible to place the unit, false otherwise
	 **/
	public boolean addUnit(Unit unit) {
		if ((unit.getSize() <= this.maxUnitSize) && !this.isBusy() && this.usableInThisGame()) {
			this.currentUnit = unit;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets current unit of this cell to null. Returns true if it's possible to
	 * remove a unit, false otherwise (there is no unit to be removed)
	 *
	 * @return true if possible to remove a unit, false otherwise
	 **/
	public boolean removeUnit() {
		if ((!this.isBusy())) {
			return false;
		} else {
			this.currentUnit = null;
			return true;
		}
	}

	/**
	 * Returns the horizontal coordinate of the Cell
	 *
	 * @return the horizontal coordinate of the Cell
	 **/
	public int getX() {
		return xIndex;
	}

	/**
	 * Returns the vertical coordinate of the Cell
	 *
	 * @return the vertical coordinate of the Cell
	 **/
	public int getY() {
		return yIndex;
	}

	/**
	 * Returns the cost for a unit to stay on the Cell
	 *
	 * @return the cost for a unit to stay on the Cell
	 **/
	public int getCost() {
		return this.cost;
	}

	/**
	 * Sets the cost for a unit to stay on the Cell
	 *
	 * @param cost, the desired cost
	 **/
	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return this.display() + "(" + this.xIndex + ":" + this.yIndex + ")";
	}

	/**
	 * Returns the Resource of the Cell
	 *
	 * @return the Resource of the Cell
	 **/
	public abstract Resource getResource();

	@Override
	public abstract boolean equals(Object o);

	/**
	 * Displays this Cell on standard output
	 *
	 * @return how the cell is displayed in the terminal
	 */
	public abstract String display();

	/**
	 * Returns the maximum size a unit can have on this cell
	 *
	 * @return maximum size of unit for this cell
	 */
	public int getMaxUnit() {
		return this.maxUnitSize;
	}

	/**
	 * Returns true if the maxUnitSize of this Cell is worth at least one by the
	 * game's rules, false otherwise.
	 *
	 * @return true if the maxUnitSize of this Cell is worth at least one by the
	 *         game's rules, false otherwise.
	 */
	public boolean usableInThisGame() {
		return (this.getMaxUnit() > 0);
	}

	/**
	 * Returns true if the cell is occupied by a unit, false otherwise
	 *
	 * @return true if the cell is occupied by a unit, false otherwise
	 */
	public boolean isBusy() {
		return this.currentUnit != null;
	}

	/**
	 * @return the board the Cell is in
	 */
	public BoardGame getInBoard() {
		return inBoard;
	}
}
