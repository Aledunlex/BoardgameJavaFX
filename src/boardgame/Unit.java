package boardgame;

/**
 * This abstract class defines some methods valid for any Unit. Units have
 * specific displays for standard output, and may or may not need a salary.
 */

public abstract class Unit {

	protected Cell cell;
	protected Player team;
	protected int gold;
	protected int size;
	/** Only for display purposes */
	protected String unitType;

	/**
	 * Deploys a Unit on a specified cell for the given player
	 *
	 * @param cell on which the Unit will first be deployed
	 * @param team whose team the Unit is on, ergo which player owns it
	 **/
	public Unit(Cell cell, Player team) {
		this.cell = cell;
		this.team = team;
		this.gold = 0;
	}

	/**
	 * Returns true if the unit keeps what they're paid with, or false if the
	 * payment is immediately consumed (not stocked)
	 *
	 * @return true if the unit stocks what they're paid with (e.g gold), false if
	 *         the payment is immediately consumed (e.g. food)
	 */
	public abstract boolean needsSalary();

	/**
	 * Displays a String for the unit's need
	 *
	 * @return a String displaying the unit's need
	 */
	public String needToString(int amount) {
		return this.team.needToString(amount);
	}

	/**
	 * Returns the cell on which the Unit is standing on
	 *
	 * @return the cell on which the Unit is standing on
	 **/
	public Cell getCell() {
		return cell;
	}

	/**
	 * Sets this Unit on a specified cell
	 *
	 * @param cell on which the Unit will be moved
	 **/
	public void setCell(Cell cell) {
		this.cell = cell;

	}

	/**
	 * Returns the Player who owns the Unit
	 *
	 * @return the Player who owns the Unit
	 **/
	public Player getTeam() {
		return team;
	}

	/**
	 * Sets this Unit's team, therefore which player owns it
	 *
	 * @param team, the player who will own this unit
	 **/
	public void setTeam(Player team) {
		this.team = team;
	}

	/**
	 * Returns the amount of gold owned by the Unit
	 *
	 * @return the amount of gold owned by the Unit
	 **/
	public int getGold() {
		return gold;
	}

	/**
	 * Sets the amount of gold owned by the Unit
	 *
	 * @param gold, the desired amount of gold
	 **/
	public void setGold(int gold) {
		this.gold = gold;
	}

	/**
	 * Returns the size of the Unit
	 *
	 * @return the size of the Unit
	 **/
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of the Unit
	 *
	 * @param newSize the desired size for the Unit
	 **/
	public void setSize(int newSize) {
		this.size = newSize;
	}

	/**
	 * Sets most of this Unit's attributes to null or 0, effectively removing it
	 * from the game
	 **/
	public void quit() {
		this.cell.removeUnit();
		this.cell = null;
		this.gold = 0;
		this.size = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Unit) {
			Unit other = (Unit) o;
			boolean partialRes = this.cell.equals(other.cell) && this.gold == other.gold && this.size == other.size;
			if (this.team != null) {
				return partialRes && this.team.equals(other.team);
			} else {
				return partialRes && other.team == null;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns a String representation for this unit's type, depending on the game,
	 * with a capitalized first letter or not
	 * 
	 * @param capitalized must be true if the first letter is wished to be
	 *                    capitalized, e.g. at the start of a sentence
	 * @return a String representation for this unit's type, depending on the game
	 */
	public String typeToString(boolean capitalized) {
		String typeString = this.unitType;
		if (capitalized) {
			typeString = typeString.substring(0, 1).toUpperCase() + typeString.substring(1);
		}
		return typeString;
	}

	@Override
	public String toString() {
		return "[" + this.team.getName() + "'s " + this.typeToString(false) + " {s:" + size + "} at "
				+ this.cell.toString() + "]";
	}

}
