package boardgame.resources;

import boardgame.Resource;

/**
 * A sub-class of Resource, creates a Stone object to be collected on a Cell
 * accordingly to the Game's rule set.
 */

public class Stone extends Resource {

	/**
	 * Create a Stone resource with given value
	 *
	 * @param value the value of the resource
	 */
	public Stone(int value) {
		super(value);
	}

	@Override
	public String display() {
		return "Stone";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Stone) {
			Resource other = (Stone) obj;
			return this.value == other.getValue();
		} else {
			return false;
		}
	}

}
