package boardgame.players;

import boardgame.Player;
import boardgame.Strategy;

/**
 * A sub-class of Player, creates an EcoPlayer object to play the EcoGame.
 */

public class EcoPlayer extends Player {

	private static final int ECO_START_GOLD = 15;

	/**
	 * Creates an EcoPlayer for the resource-gathering based game, of a chosen name
	 * and using a chosen strategy
	 *
	 * @param name     the name of this player
	 * @param strategy used by this player
	 */
	public EcoPlayer(String name, Strategy strategy) {
		super(name, strategy);
		this.stuffToPleaseUnit = ECO_START_GOLD;
	}

	@Override
	public String needToString(int amount) {
		String need = " gold coin";
		return (amount == 1 ? need : need + "s");
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EcoPlayer) {
			EcoPlayer theOther = ((EcoPlayer) o);
			return super.equals(theOther);
		} else {
			return false;
		}
	}

}
