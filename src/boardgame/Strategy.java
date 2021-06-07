package boardgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This interface defines the basics of what a Strategy needs to allow a Player
 * to play a Game and execute Moves.
 */

public interface Strategy {

	/**
	 * Asks player to choose a move from a moves list
	 *
	 * @param moves list to choose from
	 * @return move chosen by the player
	 */
	public Move chooseMove(List<Move> moves);

	/**
	 * Asks player to choose a cell; if the cell is unavailable, player needs to
	 * input another cell until the chosen cell is available
	 *
	 * @param board the board where the cell is
	 * @return chosen empty cell
	 */
	public Cell chooseCell(BoardGame board);

	/**
	 * Asks player for the size of the army he wants to create between min
	 * (included) and max (excluded)
	 *
	 * @param min minimum size of the army (included)
	 * @param max maximum size of the army (excluded)
	 * @return size of the army
	 */
	public int chooseSize(int min, int max);

	/**
	 * Asks player for the amount of resource he wants to convert between min
	 * (included) and max (excluded)
	 *
	 * @param min minimum amount (included)
	 * @param max maximum size of the army (excluded)
	 * @return amount
	 */
	public int chooseAmount(int min, int max);

	/**
	 * Asks player for x coordinate of a cell between min (included) and max
	 * (excluded)
	 *
	 * @param min x (included)
	 * @param max x (excluded)
	 * @return x
	 */
	public int chooseX(int min, int max);

	/**
	 * Asks player for y coordinate of a cell between min (included) and max
	 * (excluded)
	 *
	 * @param min y (included)
	 * @param max y (excluded)
	 * @return y
	 */
	public int chooseY(int min, int max);

	/**
	 * Asks player to select a resource type
	 *
	 * @param resource, a HashMap pairing a resource ID under String form, and its
	 *                  list of corresponding resource object. The hashmap is owned
	 *                  by the Player object.
	 * @return the id of the chosen Resource object
	 */
	public String chooseResourceType(HashMap<String, ArrayList<Resource>> resource);

	/**
	 * Asks player to select a Unit, must be one of their own.
	 *
	 * @param ownedUnits list of Units the Player deployed
	 * @return the selected Unit
	 */
	public Unit chooseUnit(List<Unit> ownedUnits);

}
