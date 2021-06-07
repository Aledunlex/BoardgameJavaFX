package boardgame.game;

import boardgame.BoardGame;
import boardgame.Game;
import boardgame.Player;
import boardgame.Unit;
import boardgame.moves.DeployWorker;
import boardgame.moves.Exchange;
import boardgame.moves.FixNeed;
import boardgame.moves.Harvest;
import boardgame.moves.IdleEco;

/**
 * A sub-class of Game, creates a GameEco according to its specific rule set.
 */

public class GameEco extends Game {

	public GameEco(BoardGame board, int maxRounds) {
		super(board, maxRounds);
	}

	@Override
	public void addMoveSet() {
		this.theMoves.add(new DeployWorker());
		this.theMoves.add(new Exchange(false));
		this.theMoves.add(new IdleEco());
	}

	@Override
	public void addMandatoryMoves() {
		this.mandatoryMoves.add(new Harvest());
		this.mandatoryMoves.add(new FixNeed());
	}

	@Override
	public void countPlayerScore(Player player) {
		int totalGold = 0;
		for (Unit currUnit : player.allDeployedUnits()) {
			int currentGold = currUnit.getGold();
			if (currentGold > 0)
				System.out.println(
						" → " + currUnit.toString() + " has " + currentGold + currUnit.needToString(currentGold));
			totalGold += currentGold;
		}
		player.setScore(totalGold);
	}

}
