package boardgame;

/**
 * This abstract class implements Move and defines a display at the
 * Move execution. 
 */

public abstract class MoveAdapter implements Move {

	public void execute(Player player) {
		this.stdDisplay();
		this.onExecution(player);
	}

	/**
	 * A display for the standard Output.
	 */
	public void stdDisplay() {
		int dashCount = this.display().length() + 2;
		System.out.println("__________________________________");
		System.out.println("\n" + "-".repeat(dashCount));
		System.out.println(" " + this.display());
		System.out.println("-".repeat(dashCount) + "\n");
	}

	/**
	 * Executes the implemented Move for the player in parameter.
	 */
	public abstract void onExecution(Player player);

}
