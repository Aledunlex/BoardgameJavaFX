package boardgame.fabrices;

import boardgame.Cell;
import boardgame.Fabrice;
import boardgame.cells.DesertCell;
import boardgame.cells.ForestCell;
import boardgame.cells.MountainCell;
import boardgame.cells.OceanCell;
import boardgame.cells.PlainCell;

/**
 * An implementation of Fabrice, creates a FabriceEcolo object, which will then
 * be used by a BoardGame to create Cells accordingly to the specific rule set
 * of an EcoGame.
 */

public class FabriceEcolo extends Fabrice {

	private static final int OCEAN_VALUE = 0;
	private static final int OCEAN_MAXUNIT = 0;
	private static final int OCEAN_GOLD = 0;
	private static final int OCEAN_COST = 0;
	private static final int DESERT_VALUE = 5;
	private static final int DESERT_MAXUNIT = 1;
	private static final int DESERT_GOLD = 2;
	private static final int DESERT_COST = 3;
	private static final int FOREST_VALUE = 2;
	private static final int FOREST_MAXUNIT = 1;
	private static final int FOREST_GOLD = 1;
	private static final int FOREST_COST = 1;
	private static final int MOUNTAIN_VALUE = 8;
	private static final int MOUNTAIN_MAXUNIT = 1;
	private static final int MOUNTAIN_GOLD = 0;
	private static final int MOUNTAIN_COST = 5;
	private static final int PLAIN_VALUE = 2;
	private static final int PLAIN_MAXUNIT = 1;
	private static final int PLAIN_GOLD = 1;
	private static final int PLAIN_COST = 1;

	/**
	 * A fabric used to create cells for the eco game
	 */
	public FabriceEcolo() {
	}

	/**
	 * Create a plain cell at the given coordinates with properties depending on the
	 * eco game rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new plain cell at the given coordinates with properties depending
	 *         on the eco game rules
	 */
	@Override
	public Cell buildPlainCell(int x, int y) {
		return new PlainCell(x, y, PLAIN_COST, PLAIN_GOLD, PLAIN_MAXUNIT, PLAIN_VALUE);
	}

	/**
	 * Create a mountain cell at the given coordinates with properties depending on
	 * the eco game rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new mountain cell at the given coordinates with properties
	 *         depending on the eco game rules
	 */
	@Override
	public Cell buildMountainCell(int x, int y) {
		return new MountainCell(x, y, MOUNTAIN_COST, MOUNTAIN_GOLD, MOUNTAIN_MAXUNIT, MOUNTAIN_VALUE);
	}

	/**
	 * Create a forest cell at the given coordinates with properties depending on
	 * the eco game rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new forest cell at the given coordinates with properties depending
	 *         on the eco game rules
	 */
	@Override
	public Cell buildForestCell(int x, int y) {
		return new ForestCell(x, y, FOREST_COST, FOREST_GOLD, FOREST_MAXUNIT, FOREST_VALUE);
	}

	/**
	 * Create a desert cell at the given coordinates with properties depending on
	 * the eco game rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new desert cell at the given coordinates with properties depending
	 *         on the eco game rules
	 */
	@Override
	public Cell buildDesertCell(int x, int y) {
		return new DesertCell(x, y, DESERT_COST, DESERT_GOLD, DESERT_MAXUNIT, DESERT_VALUE);
	}

	/**
	 * Create a ocean cell at the given coordinates with properties depending on the
	 * eco game rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new ocean cell at the given coordinates with properties depending
	 *         on the eco game rules
	 */
	@Override
	public Cell buildOceanCell(int x, int y) {
		return new OceanCell(x, y, OCEAN_COST, OCEAN_GOLD, OCEAN_MAXUNIT, OCEAN_VALUE);
	}

}
