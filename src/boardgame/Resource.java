package boardgame;

/**
 * This abstract class defines some methods valid for any Resource. Resources
 * have specific displays for standard output, specific IDs to be used in a
 * Player's HashMap of owned Resources, and a determined value upon
 * construction.
 */

public abstract class Resource {

	protected int value;

	/**
	 * Creates a resource with a given value
	 *
	 * @param value of the resource when exchange for gold or food
	 */
	public Resource(int value) {
		this.value = value;
	}

	/**
	 * Returns the value of the resource
	 *
	 * @return value of the resource
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Returns a display for this resource, to be used on standard output.
	 * 
	 * @return how the resource is displayed on the terminal during the game
	 */
	public abstract String display();

	/**
	 * Returns id of the resource used in the player resource map
	 *
	 * @return id of resource
	 */
	public String getId() {
		return this.display();
	}
}
