package boardgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Button;;

/**
 * This class defines methods valid for any BoardGame. The Fabrice object used
 * as parameter defines which rule set will be used to create the Cells.
 */

public class BoardGame {

	private int boardX;
	private int boardY;
	private Cell[][] theCells;
	private List<Cell> availableCells;
	private List<Button> theButtons;
	private Fabrice gFabric;
	private Random random;

	private static final int MINIMUM_OCEAN = 2 / 3;

	/**
	 * Builds a board of given width and length
	 *
	 * @param boardX  the board's width
	 * @param boardY  the board's length
	 * @param gFabric the fabrice used for this game, aka under which rule set the
	 *                board will be built
	 **/
	public BoardGame(int boardX, int boardY, Fabrice gFabric) {
		this.boardX = boardX;
		this.boardY = boardY;
		this.gFabric = gFabric;
		this.theCells = new Cell[this.boardX][this.boardY];
		this.availableCells = new ArrayList<Cell>();
		this.random = new Random();
		this.initBoard();
		this.initAvailableCells();
	}

	/**
	 * Initializes each cell of the board to have a minimum of 66% of Ocean Cells,
	 * called in the constructor
	 */
	private void initBoard() {
		int maxEarth = MINIMUM_OCEAN * (this.boardX * this.boardY);
		for (int i = 0; i < this.boardX; i++) {
			for (int j = 0; j < boardY; j++) {
				this.theCells[i][j] = this.gFabric.buildOceanCell(i, j);
			}
		}
		for (int i = 0; i < this.boardX; i++) {
			for (int j = 0; j < boardY; j++) {
				if (maxEarth <= 0) {
					if (this.random.nextInt(3) == 1) {
						this.theCells[i][j] = this.gFabric.defineCell(i, j);
						maxEarth--;
					}
				}
			}
		}
		for (int i = 0; i < this.boardX; i++) {
			for (int j = 0; j < boardY; j++) {
				if (this.oceanSurround(i, j)) {
					this.theCells[i][j] = this.gFabric.buildOceanCell(i, j);
				}

			}
		}
	}

	/**
	 * Initializes the list of Cells where a unit can initially be deployed at start
	 * of game, only to be called in the constructor
	 */
	private void initAvailableCells() {
		for (int i = 0; i < this.boardX; i++) {
			for (int j = 0; j < boardY; j++) {
				if (this.theCells[i][j].usableInThisGame()) {
					this.availableCells.add(theCells[i][j]);
				}
			}
		}
	}

	/**
	 * Returns a list of Cells where a unit can be deployed. This list is dynamic
	 * and changes throughout a game with each deployed or sacrificed unit.
	 *
	 * @return a list of Cells where a unit can be deployed
	 */
	public List<Cell> getAvailableCells() {
		return this.availableCells;
	}

	/**
	 * Returns the width of the board
	 *
	 * @return width of the board
	 */
	public int getWidth() {
		return this.boardX;
	}

	/**
	 * Returns the length of the board
	 *
	 * @return length of the board
	 */
	public int getLength() {
		return this.boardY;
	}

	/**
	 * Returns the cell of specified coordinates
	 *
	 * @param coordX the horizontal coordinate of the desired cell
	 * @param coordY the vertical coordinate of the desired cell
	 * @return desired cell
	 **/
	public Cell getCell(int coordX, int coordY) {
		return this.theCells[coordX][coordY];
	}

	/**
	 * Sets a cell with specified coordinates to the board Only used in tests.
	 *
	 * @param cell to be set
	 **/
	public void setCell(Cell cell) {
		this.theCells[cell.getX()][cell.getY()] = cell;
	}

	/**
	 * Returns the list of all cells surrounding the specified cell
	 *
	 * @param cell for which we want to get the adjacent ones
	 * @return the list of the cells around the specified one
	 **/
	public List<Cell> getAdjacentCell(Cell cell) {
		int coordX = cell.getX();
		int coordY = cell.getY();

		List<Cell> result = new ArrayList<Cell>();

		if (coordY != 0) {
			Cell top = this.getCell(coordX, coordY - 1);
			result.add(top);
		}
		if (coordY != boardY - 1) {
			Cell bottom = this.getCell(coordX, coordY + 1);
			result.add(bottom);
		}
		if (coordX != 0) {
			Cell left = this.getCell(coordX - 1, coordY);
			result.add(left);
		}
		if (coordX != boardX - 1) {
			Cell right = this.getCell(coordX + 1, coordY);
			result.add(right);
		}

		return result;
	}

	/**
	 * Returns true if all surrounding cells are of OceanCell type (ergo non usable
	 * on this board), false otherwise
	 *
	 * @param x the desired cell's horizontal coordinate
	 * @param y the desired cell's vertical coordinate
	 * @return true if all cells are OceanCells, false otherwise
	 **/
	public boolean oceanSurround(int x, int y) {
		Cell cell = this.getCell(x, y);
		boolean onlyOcean = true;
		List<Cell> surround = this.getAdjacentCell(cell);
		for (Cell c : surround) {
			if (c.usableInThisGame()) {
				onlyOcean = false;
			}
		}
		return onlyOcean;
	}

	/**
	 * Displays the board on the terminal
	 */
	public void displayBoard() {
		System.out.println();
		for (int i = 0; i < this.boardX; i++) {
			if (i < 10) {
				System.out.print(" " + i + " ");
			} else if (i < 100) {
				System.out.print(i + " ");
			} else {
				System.out.print(i);
			}
		}
		System.out.println();
		for (int y = 0; y < this.boardY; y++) {
			for (int x = 0; x < this.boardX; x++) {
				Cell cell = this.getCell(x, y);
				System.out.print(cell.display());
				Button button = cellToButton(cell);
				if (!theButtons.contains(button)) {theButtons.add(button);}
			}
			System.out.print(" " + y);
			System.out.println();
		}
	}

	/**
	 * Returns the list of all Cells from this BoardGame
	 * 
	 * @return the list of all Cells from this BoardGame
	 * 
	 */
	public ArrayList<Cell> getAllCells() {
		ArrayList<Cell> res = new ArrayList<Cell>();
		for (int i = 0; i < this.boardX; i++) {
			for (int j = 0; j < boardY; j++) {
				res.add(theCells[i][j]);
			}
		}
		return res;
	}
	
	public List<Button> getAllButtons() {
		return this.theButtons;
	}
	
	private Button cellToButton(Cell cell) {
		Button res = new Button();
		
		res.setId(cell.getId());
		
		return res;
	}

}
