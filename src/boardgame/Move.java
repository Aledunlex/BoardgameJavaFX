package boardgame;

/**
 * This interface defines which methods must be done for a Move that will be
 * used in a Game's list of Move, either mandatory moves or choosable moves.
 */

public interface Move {

	/**
	 * Called by a Strategy to run the Move after displaying it on standard output
	 * 
	 * @param player executing the Move.
	 */
	public void execute(Player player);

	/**
	 * A display for the Move.
	 * 
	 * @return a display for the executed Move.
	 */
	public String display();

}
