package boardgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import boardgame.util.AbstractPropertyChangeable;

/**
 * This abstract class defines some methods valid for any Player. The strategy
 * object used as parameter defines which strategy will be used by the Player.
 */

public abstract class Player extends AbstractPropertyChangeable {

	protected final String name;
	protected int score;
	protected int stuffToPleaseUnit;
	protected HashMap<String, ArrayList<Resource>> resources;
	protected List<Unit> deployedUnits;
	protected List<Cell> controlledCells;
	protected Strategy strat;
	protected Game playedGame;

	/**
	 * Builds a Player of given name and a Strategy
	 *
	 * @param name     of this player
	 * @param strategy used by this player
	 */
	public Player(String name, Strategy strategy) {
		this.name = name;
		this.deployedUnits = new ArrayList<Unit>();
		this.controlledCells = new ArrayList<Cell>();
		this.score = 0;
		this.strat = strategy;
		this.resources = new HashMap<String, ArrayList<Resource>>();
	}

	/**
	 * Returns the Game in which the Player was added
	 *
	 * @return the Game in which the Player was added
	 */
	public Game getPlayingGame() {
		return this.playedGame;
	}

	/**
	 * Only to be used using addPlayer() from a Game object
	 * 
	 * @param playedGame
	 */
	public void setPlayingGame(Game playedGame) {
		this.playedGame = playedGame;
	}

	@Override
	public String toString() {
		return "[" + this.getName() + "]";
	}

	/**
	 * Displays the player's name as written during construction
	 *
	 * @return the player's name as written during construction
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the owned quantity of whatever the player's unit needs to keep
	 * playing
	 *
	 * @return the owned quantity of whatever the player's unit needs to keep
	 *         playing
	 */
	public int getNeedQty() {
		return this.stuffToPleaseUnit;
	}

	/**
	 * Sets the owned amount of whatever the player's unit needs to keep playing
	 *
	 * @param value to be set
	 */
	public void setNeedQty(int value) {
		int prev = this.stuffToPleaseUnit;
		this.stuffToPleaseUnit = value;
		propertyChangeSupport.firePropertyChange("stuffToPleaseUnit", prev, value);
	}

	/**
	 * Displays a String for the Player's unit's need, plural or singular in
	 * relevant cases when possible Note that this assumes the Player only has one
	 * type of Units, as expected from the current games' rules.
	 *
	 * @param amount of relevant need, used to determine if words needs to be plural
	 *               or singular, when possible
	 * @return a String displaying what this player's units need to keep playing
	 */
	public abstract String needToString(int amount);

	/**
	 * Returns the score for this player
	 *
	 * @return the score for this player
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Returns the type of the Strategy used by the Player.
	 *
	 * @return the type of the Strategy used by the Player
	 */
	public Strategy getStrategy() {
		return this.strat;
	}

	/**
	 * Sets the score this player
	 *
	 * @param score to be set
	 */
	public void setScore(int score) {
		int prev = this.score;
		this.score = score;
		propertyChangeSupport.firePropertyChange("score", prev, score);
	}

	/**
	 * Allows to add a deployed Unit to deployedUnits and its cell to
	 * controlledCells. Returns true if the Player's attributes allows to place this
	 * unit (it does not effect the cell's currentUnit attribute).
	 *
	 * @param unit the unit to be deployed
	 * @return true if the Player's attributes allows to place this unit
	 */
	public boolean addDeployedUnit(Unit unit) {
		List<Unit> previousUnits = deployedUnits;
		List<Cell> previousCells = controlledCells;
		this.deployedUnits.add(unit);
		this.controlledCells.add(unit.getCell());
		unit.addPropertyChangeListener(this.getChangeSupport().getPropertyChangeListeners()[0]);
		propertyChangeSupport.firePropertyChange("deployedUnits", previousUnits, this.deployedUnits);
		propertyChangeSupport.firePropertyChange("controlledCells", previousCells, this.controlledCells);
		return true;
	}

	/**
	 * Returns the List of units deployed by the player
	 *
	 * @return List of units deployed by the player
	 */
	public List<Unit> allDeployedUnits() {
		return this.deployedUnits;
	}

	/**
	 * Removes a unit from the player's list of owned units, its cell from the
	 * player's list of controlled cells, then calls the quit() method it To be used
	 * when a player can no longer satisfy the needs of this unit
	 *
	 * @param unit to be removed from this player's list of units
	 */
	public void removeUnit(Unit unit) {
		List<Unit> previousUnits = deployedUnits;
		List<Cell> previousCells = controlledCells;
		this.controlledCells.remove(unit.getCell());
		this.deployedUnits.remove(unit);
		unit.quit();
		propertyChangeSupport.firePropertyChange("deployedUnits", previousUnits, this.deployedUnits);
		propertyChangeSupport.firePropertyChange("controlledCells", previousCells, this.controlledCells);
	}
	
	/**
	 * Removes a unit from the player's list of owned units, its cell from the
	 * player's list of controlled cells, then calls the quit() method it To be used
	 * when a player can no longer satisfy the needs of this unit
	 *
	 * @param unit to be removed from this player's list of units
	 */
	public void loseAllUnits() {
		List<Unit> previousUnits = deployedUnits;
		List<Cell> previousCells = controlledCells;
		for (Unit unit : deployedUnits)
			unit.quit();
		this.controlledCells = new ArrayList<Cell>();
		this.deployedUnits = new ArrayList<Unit>();
		propertyChangeSupport.firePropertyChange("deployedUnits", previousUnits, this.deployedUnits);
		propertyChangeSupport.firePropertyChange("controlledCells", previousCells, this.controlledCells);
	}

	/**
	 * Returns the list of all cells controlled by the player's units
	 *
	 * @return list of all cells controlled by the player's units
	 */
	public List<Cell> allControlledCells() {
		return this.controlledCells;
	}

	/**
	 * Returns the set of each owned Resource with their corresponding amount
	 *
	 * @return the set of each owned Resource with their corresponding amount
	 */
	public HashMap<String, ArrayList<Resource>> allResources() {
		return this.resources;
	}

	/**
	 * Returns the quantity of owned specified Resource
	 *
	 * @param id of the resource to get the owned quantity
	 * @return the quantity of owned specified Resource
	 */
	public int getResAmnt(String id) {
		ArrayList<Resource> res = this.getResourceList(id);
		return res.size();
	}

	/**
	 * Adds one resource to the player resource
	 *
	 * @param resource type of resource to add
	 */
	public void addResource(Resource resource) {
		HashMap<String, ArrayList<Resource>> previous = this.resources;
		ArrayList<Resource> res = this.resources.get(resource.getId());
		res.add(resource);
		propertyChangeSupport.firePropertyChange("resources", previous, this.resources);
	}

	/**
	 * Removes given amount of one resource type from the player Must only be called
	 * after checking if said resource is initialized in the Player's inventory.
	 *
	 * @param id     the id of the resource to remove
	 * @param amount the amount to remove
	 */
	public void removeResource(String id, int amount) {
		HashMap<String, ArrayList<Resource>> previous = this.resources;
		ArrayList<Resource> res = this.resources.get(id);
		for (int i = 0; i < amount; i++) {
			res.remove(0);
		}
		propertyChangeSupport.firePropertyChange("resources", previous, this.resources);
	}

	/**
	 * Returns the list of all the resources of one type owned by the player. To be
	 * used to know the amount of an owned resource type. Amounts equal to 0 will be
	 * listed.
	 *
	 * @param id of the type of resource for which the list is required
	 * @return list of all the resource of one type owned by the player
	 */
	public ArrayList<Resource> getResourceList(String id) {
		return this.resources.get(id);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			Player theOther = ((Player) o);
			return this.name.equals(theOther.name) && this.resources.equals(theOther.resources)
					&& this.deployedUnits.equals(theOther.deployedUnits)
					&& this.controlledCells.equals(theOther.controlledCells) && this.score == theOther.score
					&& this.stuffToPleaseUnit == theOther.stuffToPleaseUnit;
		} else {
			return false;
		}
	}

}
