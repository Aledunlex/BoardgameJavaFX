package boardgame.resources;

import boardgame.Resource;

/**
 * A sub-class of Resource, creates a Sand object to be collected on a Cell
 * accordingly to the Game's rule set.
 */

public class Sand extends Resource {

	/**
	 * Create a sand resource with given value
	 *
	 * @param value the value of the resource
	 */
	public Sand(int value) {
		super(value);
	}

	@Override
	public String display() {
		return "Sand";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sand) {
			Resource other = (Sand) obj;
			return this.value == other.getValue();
		} else {
			return false;
		}
	}

}
