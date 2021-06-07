package boardgame.fabrices;

import boardgame.Cell;
import boardgame.Fabrice;
import boardgame.cells.DesertCell;
import boardgame.cells.ForestCell;
import boardgame.cells.MountainCell;
import boardgame.cells.OceanCell;
import boardgame.cells.PlainCell;

/**
 * An implementation of Fabrice, creates a FabriceArmy object, which will then
 * be used by a BoardGame to create Cells accordingly to the specific rule set
 * of a WarGame.
 */

public class FabriceArmy extends Fabrice {

	private static final int WAR_SIMPLE_COST = 1;
	private static final int WAR_DOUBLE_COST = 2;
	private static final int OCEAN_VALUE = 0;
	private static final int OCEAN_MAXUNIT = 0;
	private static final int OCEAN_GOLD = 0;
	private static final int DESERT_VALUE = 0;
	private static final int DESERT_MAXUNIT = 3;
	private static final int DESERT_GOLD = 1;
	private static final int FOREST_VALUE = 1;
	private static final int FOREST_MAXUNIT = 5;
	private static final int FOREST_GOLD = 1;
	private static final int MOUNTAIN_VALUE = 0;
	private static final int MOUNTAIN_MAXUNIT = 3;
	private static final int MOUNTAIN_GOLD = 1;
	private static final int PLAIN_VALUE = 5;
	private static final int PLAIN_MAXUNIT = 5;
	private static final int PLAIN_GOLD = 1;
	private static final int BONUS_WAR_OWNED_PLAIN = 1;
	private static final int BONUS_WAR_OWNED_FOREST = 2;
	private static final int BONUS_WAR_OWNED_MOUNTAIN_DESERT = 4;
	private static final int MOUNTAIN_TACTICAL_ENEMY_SIZE_ADVANTAGE = 2;
	private static final int NO_TACTICAL_ENEMY_SIZE_ADVANTAGE = 0;

	/**
	 * A fabric used to create cells for the war game
	 */
	public FabriceArmy() {
	}

	/**
	 * Create a plain cell at the given coordinates with properties depending on the
	 * wargame rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new plain cell at the given coordinates with properties depending
	 *         on the wargame rules
	 */
	@Override
	public Cell buildPlainCell(int x, int y) {
		PlainCell cell = new PlainCell(x, y, WAR_SIMPLE_COST, PLAIN_GOLD, PLAIN_MAXUNIT, PLAIN_VALUE);
		cell.setBonus(BONUS_WAR_OWNED_PLAIN);
		cell.setAdvantage(NO_TACTICAL_ENEMY_SIZE_ADVANTAGE);
		return cell;
	}

	/**
	 * Create a mountain cell at the given coordinates with properties depending on
	 * the wargame rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new mountain cell at the given coordinates with properties
	 *         depending on the wargame rules
	 */
	@Override
	public Cell buildMountainCell(int x, int y) {
		MountainCell cell = new MountainCell(x, y, WAR_SIMPLE_COST, MOUNTAIN_GOLD, MOUNTAIN_MAXUNIT, MOUNTAIN_VALUE);
		cell.setBonus(BONUS_WAR_OWNED_MOUNTAIN_DESERT);
		cell.setAdvantage(MOUNTAIN_TACTICAL_ENEMY_SIZE_ADVANTAGE);
		return cell;
	}

	/**
	 * Create a cell forest at the given coordinates with properties depending on
	 * the wargame rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new forest cell at the given coordinates with properties depending
	 *         on the wargame rules
	 */
	@Override
	public Cell buildForestCell(int x, int y) {
		ForestCell cell = new ForestCell(x, y, WAR_SIMPLE_COST, FOREST_GOLD, FOREST_MAXUNIT, FOREST_VALUE);
		cell.setBonus(BONUS_WAR_OWNED_FOREST);
		cell.setAdvantage(NO_TACTICAL_ENEMY_SIZE_ADVANTAGE);
		return cell;
	}

	/**
	 * Create a desert cell at the given coordinates with properties depending on
	 * the wargame rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new desert cell at the given coordinates with properties depending
	 *         on the wargame rules
	 */
	@Override
	public Cell buildDesertCell(int x, int y) {
		DesertCell cell = new DesertCell(x, y, WAR_DOUBLE_COST, DESERT_GOLD, DESERT_MAXUNIT, DESERT_VALUE);
		cell.setBonus(BONUS_WAR_OWNED_MOUNTAIN_DESERT);
		cell.setAdvantage(NO_TACTICAL_ENEMY_SIZE_ADVANTAGE);
		return cell;
	}

	/**
	 * Create an ocean cell at the given coordinates with properties depending on
	 * the wargame rules
	 *
	 * @param x the x coordinate of the cell
	 * @param y the y coordinate of the cell
	 * @return a new ocean cell at the given coordinates with properties depending
	 *         on the wargame rules
	 */
	@Override
	public Cell buildOceanCell(int x, int y) {
		return new OceanCell(x, y, WAR_SIMPLE_COST, OCEAN_GOLD, OCEAN_MAXUNIT, OCEAN_VALUE);
	}

}
