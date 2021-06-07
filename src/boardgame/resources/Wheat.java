package boardgame.resources;

import boardgame.Resource;

/**
 * A sub-class of Resource, creates a Wheat object to be collected on a Cell
 * accordingly to the Game's rule set.
 */

public class Wheat extends Resource {

	/**
	 * Create a wheat resource with given value
	 *
	 * @param value the value of the resource
	 */
	public Wheat(int value) {
		super(value);
	}

	@Override
	public String display() {
		return "Wheat";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Wheat) {
			Resource other = (Wheat) obj;
			return this.value == other.getValue();
		} else {
			return false;
		}
	}

}
