package boardgame.game;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.Game;
import boardgame.Player;
import boardgame.Unit;
import boardgame.moves.DeployArmy;
import boardgame.moves.Exchange;
import boardgame.moves.FixNeed;
import boardgame.moves.Harvest;
import boardgame.moves.Idle;
import boardgame.players.WarPlayer;

/**
 * A sub-class of Game, creates a GameWar according to its specific rule set.
 */

public class GameWar extends Game {

	public static final int WAR_MIN_CONTROLLED_CELLS_FOR_BONUS = 10;
	public static final int BONUS_WAR_FOR_CONTROLLED_CELLS = 5;

	public GameWar(BoardGame board, int maxRounds) {
		super(board, maxRounds);
	}

	@Override
	public void addMoveSet() {
		this.theMoves.add(new DeployArmy());
		this.theMoves.add(new Idle());

	}

	@Override
	public void addMandatoryMoves() {
		this.mandatoryMoves.add(new Harvest());
		this.mandatoryMoves.add(new Exchange(true));
		this.mandatoryMoves.add(new FixNeed());
	}

	@Override
	public void countPlayerScore(Player player) {
		WarPlayer currPlayer = (WarPlayer) player;
		int totalGold = currPlayer.getGold();
		System.out.println(" → " + player.toString() + " has " + totalGold + " gold coin" + (totalGold == 1 ? "" : "s")
				+ " in their inventory.");
		int totalScore = 0;
		for (Unit currUnit : currPlayer.allDeployedUnits()) {
			int currentGold = currUnit.getGold();
			if (currentGold > 0)
				System.out.println(" → " + currUnit.toString() + " has " + currentGold + " gold coin"
						+ (currentGold == 1 ? "" : "s") + ".");
			totalGold += currentGold;
		}
		totalScore += totalGold;
		for (Cell cell : currPlayer.allControlledCells()) {
			totalScore += cell.getBonus();
			System.out.println(" → " + currPlayer.toString() + " gets " + cell.getBonus()
					+ (cell.getBonus() == 1 ? " point" : " points") + " for owning " + cell.toString() + ".");
		}
		if (currPlayer.allControlledCells().size() >= WAR_MIN_CONTROLLED_CELLS_FOR_BONUS) {
			totalScore += BONUS_WAR_FOR_CONTROLLED_CELLS;
			System.out.println(" → " + currPlayer.toString() + " has at least " + WAR_MIN_CONTROLLED_CELLS_FOR_BONUS
					+ " cells, granting " + BONUS_WAR_FOR_CONTROLLED_CELLS + " bonus points!");
		}
		currPlayer.setScore(totalScore);
	}

}
