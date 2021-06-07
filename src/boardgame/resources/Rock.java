package boardgame.resources;

import boardgame.Resource;

/**
 * A sub-class of Resource, creates a Rock object to be collected on a Cell
 * accordingly to the Game's rule set.
 */

public class Rock extends Resource {

	/**
	 * Create a rock resource with given value
	 *
	 * @param value the value of the resource
	 */
	public Rock(int value) {
		super(value);
	}

	@Override
	public String display() {
		return "Cobblestone";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rock) {
			Resource other = (Rock) obj;
			return this.value == other.getValue();
		} else {
			return false;
		}
	}

}
