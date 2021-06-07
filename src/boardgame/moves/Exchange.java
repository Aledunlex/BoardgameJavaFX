package boardgame.moves;

import java.util.Set;

import boardgame.MoveAdapter;
import boardgame.Player;
import boardgame.Resource;

/**
 * An extension of MoveAdapter, creates an Exchange Move to be added to a Game's
 * list of Moves.
 */

public class Exchange extends MoveAdapter {

	/**
	 * Is set to true when the player exchanged at least once this round, blocking
	 * them from choosing another Move once they don't own any resource to exchange
	 */
	private boolean hasExchanged;
	/**
	 * Is set to true in a Game class if exchanging can happen alongside another
	 * selected choosable Move.
	 */
	private boolean isMandatory;

	/**
	 * Creates an Exchange Move for a Game, mandatory or not
	 *
	 * @param isMandatory determines if move is always executed after a Player's
	 *                    choice or not
	 */
	public Exchange(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * See @MoveAdapter Resets the hasExchanged to false attribute at every player
	 * round (i.e to each execute() call). If no exchanged was performed during this
	 * action, the player will have to select another Move before ending their
	 * round.
	 */
	@Override
	public void execute(Player player) {
		hasExchanged = false;
		super.execute(player);
	}

	/**
	 * See @MoveAdapter If no exchanged was performed during this action, the player
	 * will have to select another Move before ending their round.
	 */
	@Override
	public void onExecution(Player player) {
		if (this.isMandatory)
			this.hasExchanged = true;

		int initNeed = player.getNeedQty();
		int initAvailable = 0;
		Set<String> key = player.allResources().keySet();

		for (String k : key) {
			int thisAmnt = player.getResAmnt(k);
			initAvailable += thisAmnt;
		}
		if (initAvailable == 0 && !hasExchanged) {
			System.out.println(player.toString() + " can't exchange anything, another action must be selected !");
			player.getStrategy().chooseMove(player.getPlayingGame().getTheMoves()).execute(player);
		}

		else {
			String idRes = player.getStrategy().chooseResourceType(player.allResources());
			if (idRes != null) {
				if (player.getResAmnt(idRes) > 0) {
					Resource kRes = player.getResourceList(idRes).get(0);
					System.out.println("* Resource selected : " + idRes + " (1 unit is worth " + kRes.getValue()
							+ player.needToString(kRes.getValue()) + ")");
					Resource res = player.getResourceList(idRes).get(0);
					int ownedAmnt = player.getResAmnt(idRes);
					int rqdAmnt = player.getStrategy().chooseAmount(0, ownedAmnt + 1);
					System.out.println("* Amount selected : " + rqdAmnt);
					for (int i = 0; i < rqdAmnt; i++) {
						player.setNeedQty(player.getNeedQty() + res.getValue());
						player.removeResource(idRes, 1);
					}
					System.out.print(player.toString() + " had " + initNeed + player.needToString(initNeed) + " and ["
							+ ownedAmnt + (ownedAmnt == 1 ? " unit of " + idRes : " units of " + idRes) + "], ");
					System.out.print("now has " + player.getNeedQty() + player.needToString(player.getNeedQty())
							+ " and [" + player.getResAmnt(idRes)
							+ (player.getResAmnt(idRes) == 1 ? " unit of " + idRes : " units of " + idRes) + "].\n");
					hasExchanged = true;
				} else {
					System.out.println(player.toString() + " doesn't have any of the selected resource.\n");
				}
				this.onExecution(player);
			} else {
				System.out.print(player.toString() + " left the exchange selection");
				System.out.print(
						(hasExchanged ? "" : " without exchanging any resource. SELECTING A NEW ACTION NOW..") + ".\n");
				if (!hasExchanged)
					player.getStrategy().chooseMove(player.getPlayingGame().getTheMoves()).execute(player);
			}
		}
	}

	/**
	 * See @MoveAdapter
	 */
	@Override
	public String display() {
		return "Exchange resources";
	}

}
