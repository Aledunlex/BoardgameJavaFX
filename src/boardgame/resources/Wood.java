package boardgame.resources;

import boardgame.Resource;

/**
 * A sub-class of Resource, creates a Wood object to be collected on a Cell
 * accordingly to the Game's rule set.
 */

public class Wood extends Resource {

	/**
	 * Create a wood resource with given value
	 *
	 * @param value the value of the resource
	 */
	public Wood(int value) {
		super(value);
	}

	@Override
	public String display() {
		return "Wood";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Wood) {
			Resource other = (Wood) obj;
			return this.value == other.getValue();
		} else {
			return false;
		}
	}

}
