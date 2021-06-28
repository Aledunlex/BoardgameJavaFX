package boardgame.players;

import java.util.List;

import boardgame.Cell;
import boardgame.Player;
import boardgame.Strategy;
import boardgame.Unit;
import boardgame.units.Army;

/**
 * A sub-class of Player, creates a WarPlayer object to play the WarGame.
 */

public class WarPlayer extends Player {

	private int gold;
	private int availableUnits;
	private static final int WAR_MAXUNIT = 35;
	private static final int WAR_START_FOOD = 10;
	private static final int WAR_START_GOLD = 0;

	/**
	 * Creates a WarPlayer for the resource-gathering based game, of a chosen name
	 * and using a chosen strategy
	 *
	 * @param name     of this player
	 * @param strategy used by this player
	 */
	public WarPlayer(String name, Strategy strategy) {
		super(name, strategy);
		this.availableUnits = WAR_MAXUNIT;
		this.stuffToPleaseUnit = WAR_START_FOOD;
		this.gold = WAR_START_GOLD;
	}

	/**
	 * Returns the maximum amount of deployable units for this player
	 *
	 * @return the maximum amount of deployable units for this player
	 */
	public int getAvailableUnits() {
		return this.availableUnits;
	}

	@Override
	public String needToString(int amount) {
		String need = " portion";
		String ofWhat = " of food";
		return (amount == 1 ? need + ofWhat : need + "s" + ofWhat);
	}

	@Override
	/**
	 * Takes into account the availableUnits parameter of the WarPlayer :
	 * see @Player
	 * 
	 */
	public boolean addDeployedUnit(Unit unit) {
		int count = 0;
		String soldierDisplay = "soldier";
		for (Unit theseUnits : this.allDeployedUnits()) {
			count += theseUnits.getSize();
		}
		System.out.println(
				"\n" + this.toString() + " has " + count + " " + (count == 1 ? soldierDisplay : soldierDisplay + "s")
						+ " with " + this.availableUnits + " available, and wants to deploy " + unit.getSize() + ". ");
		if (unit.getSize() <= this.availableUnits) {
			this.setAvailableUnits(this.availableUnits - unit.getSize());
			return super.addDeployedUnit(unit);
		} else {
			return false;
		}
	}

	/**
	 * See Player@removeUnit Only to be used when the Army 'quits', ergo starves to
	 * death
	 *
	 * @param unit to be removed from the player's units list
	 */
	public void removeUnit(Army unit) {
		super.removeUnit(unit);
	}

	/**
	 * A unit's team is modified into another team. No bonus gold, no call to the
	 * quit() method (the unit's cell remains as is) Only to be used when the Army
	 * is lost to an enemy Player!
	 *
	 * @param unit        about to be lost by this player
	 * @param otherPlayer about to win the unit
	 */
	public void traitorousUnit(Army unit, WarPlayer otherPlayer) {
		List<Unit> previousP1Units = this.deployedUnits;
		List<Cell> previousP1Cells = this.controlledCells;
		List<Unit> previousP2Units = otherPlayer.deployedUnits;
		List<Cell> previousP2Cells = otherPlayer.controlledCells;
		
		this.controlledCells.remove(unit.getCell());
		this.deployedUnits.remove(unit);
		otherPlayer.deployedUnits.add(unit);
		otherPlayer.controlledCells.add(unit.getCell());
		unit.setTeam(otherPlayer);
		
		propertyChangeSupport.firePropertyChange("deployedUnits", previousP1Units, this.deployedUnits);
		propertyChangeSupport.firePropertyChange("controlledCells", previousP1Cells, this.controlledCells);
		otherPlayer.getChangeSupport().firePropertyChange("deployedUnits", previousP2Units, otherPlayer.deployedUnits);
		otherPlayer.getChangeSupport().firePropertyChange("controlledCells", previousP2Cells, otherPlayer.controlledCells);
	}

	/**
	 * Sets a new maximum amount of deployable units for this player.
	 *
	 * @param newMax amount of owned units
	 */
	public void setAvailableUnits(int newMax) {
		this.availableUnits = newMax;
	}

	/**
	 * Returns the amount of owned gold
	 *
	 * @return the gold of the player
	 */
	public int getGold() {
		return this.gold;
	}

	/**
	 * Sets the amount of owned gold
	 *
	 * @param value to be set
	 */
	public void setGold(int value) {
		this.gold = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WarPlayer) {
			WarPlayer theOther = ((WarPlayer) o);
			return (super.equals(theOther) && this.gold == theOther.getGold()
					&& this.availableUnits == theOther.getAvailableUnits());
		} else {
			return false;
		}
	}
}
