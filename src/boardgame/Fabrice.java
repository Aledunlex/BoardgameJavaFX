package boardgame;

import java.util.Random;

/**
 * This class defines the basics of what a Fabrice object will do, ergo
 * determines which rule set the cell creation will follow. Any optional
 * parameter can be set using specific setters after constructing a Cell with
 * buildXCell().
 */

public abstract class Fabrice {

	public static final int PLAIN = 0;
	public static final int MOUNTAIN = 1;
	public static final int FOREST = 2;
	public static final int DESERT = 3;
	public static final int OCEAN = 4;

	/** To be incremented in case any Cell type is added */
	public static final int MAX_INDEX = 4;

	public Fabrice() {
	}

	/**
	 * Returns a Cell of a specific type at the specified coordinates, cost and gold
	 * parameters depend on the war game rules; or null if input index does not
	 * match any of the currently specified Cell types (more can be added by
	 * appending this method).
	 *
	 * @param x     the cell's horizontal coordinate
	 * @param y     the cell's vertical coordinate
	 * @param index determined by createCell to specify the cell type
	 * @return returns a Cell of a specific type
	 */
	public Cell createCell(int x, int y, int index) {
		if (index == PLAIN) {
			return this.buildPlainCell(x, y);
		}

		if (index == MOUNTAIN) {
			return this.buildMountainCell(x, y);
		}

		if (index == FOREST) {
			return this.buildForestCell(x, y);
		}

		if (index == DESERT) {
			return this.buildDesertCell(x, y);
		}

		if (index == OCEAN) {
			return this.buildOceanCell(x, y);
		}

		else {
			return null;
		}

	}

	/**
	 * Returns a Cell of a random type (any except OceanCell) at the specified
	 * coordinates
	 *
	 * @param x the cell's horizontal coordinate
	 * @param y the cell's vertical coordinate
	 * @return returns a Cell of a random type
	 */
	public Cell defineCell(int x, int y) {
		Random random = new Random();
		return this.createCell(x, y, random.nextInt(MAX_INDEX));

	}

	public abstract Cell buildPlainCell(int x, int y);

	public abstract Cell buildMountainCell(int x, int y);

	public abstract Cell buildForestCell(int x, int y);

	public abstract Cell buildDesertCell(int x, int y);

	public abstract Cell buildOceanCell(int x, int y);

}
