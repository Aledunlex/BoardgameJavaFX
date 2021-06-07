package boardgame.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.Move;
import boardgame.Resource;
import boardgame.Strategy;
import boardgame.Unit;

/**
 * An implementation of Strategy, to be used with a Player object to let them
 * make random choices within the game's limits.
 */

public class RandomStrat implements Strategy {

	private Random random;

	public RandomStrat() {
		this.random = new Random();
	}

	@Override
	public Cell chooseCell(BoardGame board) {
		int randomIndex = this.random.nextInt(board.getAvailableCells().size());
		Cell chosenCell = board.getAvailableCells().get(randomIndex);
		System.out.println("Selected cell at " + chosenCell.toString() + ".");
		return chosenCell;
	}

	@Override
	public int chooseSize(int min, int max) {
		return this.random.nextInt(max - 1) + 1;
	}

	@Override
	public int chooseAmount(int min, int max) {
		return this.random.nextInt(max - 1) + 1;
	}

	@Override
	public int chooseX(int min, int max) {
		return this.random.nextInt(max);

	}

	@Override
	public int chooseY(int min, int max) {
		return this.random.nextInt(max);
	}

	@Override
	public String chooseResourceType(HashMap<String, ArrayList<Resource>> resource) {
		Set<String> key = resource.keySet();
		ArrayList<String> ar = new ArrayList<String>();
		for (String k : key) {
			if (!resource.get(k).isEmpty()) {
				ar.add(k);
			}
		}
		int choice;

		choice = this.random.nextInt(ar.size() + 1);

		if (choice == ar.size()) {
			return null;
		} else {
			return ar.get(choice);
		}
	}

	@Override
	public Move chooseMove(List<Move> moves) {
		return moves.get(this.random.nextInt(moves.size()));
	}

	@Override
	public Unit chooseUnit(List<Unit> ownedUnits) {
		int randomIndex = this.random.nextInt(ownedUnits.size());
		int i = 0;
		Unit chosenUnit = null;
		for (Unit unit : ownedUnits) {
			if (i == randomIndex) {
				chosenUnit = unit;
			}
			i++;
		}
		System.out.println("Selected unit at " + chosenUnit.getCell().toString() + ".");
		return chosenUnit;
	}

}
