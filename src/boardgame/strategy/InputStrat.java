package boardgame.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.Move;
import boardgame.Resource;
import boardgame.Strategy;
import boardgame.Unit;
import boardgame.util.Input;

/**
 * An implementation of Strategy, to be used with a Player object to let them
 * make choices within the game's limits, based on a user's inputs.
 */

public class InputStrat implements Strategy {

	public InputStrat() {
	}

	@Override
	public Unit chooseUnit(List<Unit> ownedUnits) {
		System.out.println("Please select one of your units :");
		int i = 1;
		for (Unit unit : ownedUnits) {
			System.out.println("[" + i + "] " + unit.typeToString(true) + unit.getCell().toString());
			i++;
		}

		int choice = this.checkCorrectInput(1, ownedUnits.size() + 1, "unit");
		return ownedUnits.get(choice - 1);
	}

	@Override
	public Cell chooseCell(BoardGame board) {
		boolean possibleToPlace = false;
		Cell cell = null;
		System.out.println("Select a box : ");
		while (!possibleToPlace) {
			int x = this.chooseX(0, board.getWidth());
			int y = this.chooseY(0, board.getLength());
			cell = board.getCell(x, y);
			if (!cell.isBusy()) {
				possibleToPlace = true;
			} else {
				System.out.println("This box is already occupied, you have to choose another one.");
			}
		}
		return cell;
	}

	@Override
	public int chooseSize(int min, int max) {
		System.out.println("Enter the size :");
		return this.checkCorrectInput(min, max, "size");
	}

	@Override
	public int chooseAmount(int min, int max) {
		System.out.println("Enter the amount :");
		return this.checkCorrectInput(min, max, "amount");
	}

	@Override
	public int chooseX(int min, int max) {
		System.out.println("Enter x :");
		return this.checkCorrectInput(min, max, "x");
	}

	@Override
	public int chooseY(int min, int max) {
		System.out.println("Enter y :");
		return this.checkCorrectInput(min, max, "y");
	}

	/**
	 * Returns an int from the player, the int has to be between min (included) and
	 * max (excluded), asks for an input until the input is correct
	 *
	 * @param min  minimum value (included) of the input int
	 * @param max  maximum value (excluded) of the input int
	 * @param name name of the value ask by the input (use only for display)
	 * @exception java.io.IOException if input does not correspond to an int
	 * @return correct int the player chose
	 */
	public int checkCorrectInput(int min, int max, String name) {
		boolean saisieCorrect = false;
		int value = 0;

		while (!saisieCorrect) {
			System.out.print(name + " : ");
			try {
				value = Input.readInt();

				if (value < min) {
					System.out.println("The input is too small, it must be greater than or equal to " + min);
				}

				else if (value >= max) {
					System.out.println("The input is too large, it must be less than " + max);
				}

				else {
					saisieCorrect = true;
				}
			} catch (java.io.IOException e) {
				System.out.println("You only need to enter numbers !");
			}
		}
		return value;

	}

	@Override
	public String chooseResourceType(HashMap<String, ArrayList<Resource>> resource) {
		Set<String> key = resource.keySet();
		String[] ar = key.toArray(new String[0]);
		System.out.println("Choose a resource to convert");
		int i = 1;
		for (String k : key) {
			System.out.println("[" + i + "] " + k + " (" + resource.get(k).size() + " available)");
			i++;
		}
		System.out.println("[" + i + "] convert nothing");
		int choice = this.checkCorrectInput(1, ar.length + 2, "choice");
		if (choice == ar.length + 1) {
			return null;
		} else {
			return ar[choice - 1];
		}
	}

	@Override
	public Move chooseMove(List<Move> moves) {
		System.out.println("What do you want to do ?");
		int i = 1;
		for (Move move : moves) {
			System.out.println("[" + i + "] " + move.display());
			i++;
		}
		int choice = this.checkCorrectInput(1, moves.size() + 1, "choice");
		return moves.get(choice - 1);
	}
}
