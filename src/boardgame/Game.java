package boardgame;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import boardgame.util.AbstractPropertyChangeable;

/**
 * This abstract class defines some methods valid for any Game. Other methods
 * will be specific to a game's rule set (excepting any Cell-specific rule). The
 * BoardGame object used as parameter defines which rule set will be used to
 * create the Cells. Player objects must then be added after construction of the
 * Game object, and their inventory of Resource must be initialized before
 * starting a game.
 */

public abstract class Game extends AbstractPropertyChangeable implements Runnable {

	protected List<Player> thePlayers;
	protected List<Move> theMoves;
	protected BoardGame board;
	protected int maxRounds;
	protected List<Move> mandatoryMoves;
	protected boolean paused;
	//protected Thread thread = new Thread("Game");

	/**
	 * Create a game with a given board and corresponding set of moves
	 *
	 * @param board     the board to play with
	 * @param maxRounds to be played during this game, decreases after each round,
	 *                  counting down to zero
	 **/
	public Game(BoardGame board, int maxRounds) {
		this.board = board;
		this.maxRounds = maxRounds;
		thePlayers = new ArrayList<Player>();
		theMoves = new ArrayList<Move>();
		mandatoryMoves = new ArrayList<Move>();
		addMoveSet();
		addMandatoryMoves();
		paused = false;
		//thread.start();
	}

	/**
	 * Adds the moves for this game, one of which will be executed per player and
	 * per round
	 */
	public abstract void addMoveSet();

	/**
	 * @return the thePlayers
	 */
	public List<Player> getThePlayers() {
		return thePlayers;
	}

	/**
	 * @return the mandatoryMoves
	 */
	public List<Move> getMandatoryMoves() {
		return mandatoryMoves;
	}

	/**
	 * Adds the mandatory moves for this game, which will all be executed every
	 * round
	 */
	public abstract void addMandatoryMoves();

	/**
	 * Adds a player to the game
	 *
	 * @param p the player to add to the game
	 **/
	public void addPlayer(Player p) {
		thePlayers.add(p);
		p.setPlayingGame(this);
	}

	/**
	 * Returns the BoardGame used for this Game
	 *
	 * @return the BoardGame used for this Game
	 */
	public BoardGame getBoard() {
		return board;
	}

	/**
	 * Returns the list of choosable Moves for this game
	 *
	 * @return the list of choosable Moves for this game
	 */
	public List<Move> getTheMoves() {
		return theMoves;
	}

	/**
	 * Initializes the resource map for all player in the game map is made of an
	 * empty list for each resource type link to the id of the resource. Called at
	 * the start of this.play() method, after the Players were added to the game.
	 */
	private void initPlayerRes() {
		ArrayList<Cell> cell = this.board.getAllCells();
		for (Cell c : cell) {
			if (c.getResource() != null) {
				for (Player p : thePlayers) {
					if (!p.allResources().containsKey(c.getResource().getId())) {
						p.allResources().put(c.getResource().getId(), new ArrayList<Resource>());
					}
				}
			}
		}
	}

	/**
	 * Plays one turn after displaying the Board and owned necessities for current
	 * Player, if there are remaining available Cells
	 */
	public void playOneRound() {
		board.displayBoard();
		for (Player p : thePlayers) {
			if (!this.isFinished()) {
				/*
				while (paused) {try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
					break;
				}}
				*/
				System.out.println("__________________________________\n\n");
				System.out.println("#### BEGINNING " + p.toString().toUpperCase() + "'S TURN ####\nIt's " + p.toString()
						+ "'s turn !");
				if (!p.allControlledCells().isEmpty()) {
					System.out.println(" * List of controlled cells : ");
					for (Cell cell : p.allControlledCells())
						System.out.println(" *  → " + cell.toString());
				}
				System.out.println("\n * Owned necessities : " + p.getNeedQty() + p.needToString(p.getNeedQty()) + ".");
				playPlayerRound(p);
			}
		}
		maxRounds--;
		propertyChangeSupport.firePropertyChange("maxRounds", maxRounds+1, maxRounds);
	}

	/**
	 * Plays one player's turn, executing one chosen move, then all the mandatory
	 * ones.
	 *
	 * @param player whose turn it is
	 **/
	public void playPlayerRound(Player player) {
		player.getStrategy().chooseMove(this.theMoves).execute(player);

		for (Move move : this.mandatoryMoves) {
			move.execute(player);
		}
		paused = true;
	}

	/**
	 * Counts a player's score accordingly to this Game's rules
	 * 
	 * @param player whose score is to be counted
	 */
	public abstract void countPlayerScore(Player player);

	/** Counts the scores for all players */
	public void countAllScore() {
		for (Player player : this.thePlayers) {
			System.out.println("\nCOUNTING " + player.toString().toUpperCase() + "'S SCORE :");
			this.countPlayerScore(player);
			if (player.getScore() == 0)
				System.out.println(" → " + player.toString() + " has no points...");
		}
	}

	/**
	 * Returns the winner of the game. In case of tie returns an ArrayList of all
	 * players with the highest score
	 *
	 * @return player(s) with the higher score in an ArrayList
	 */
	public ArrayList<Player> getWinner() {
		ArrayList<Player> winner = new ArrayList<Player>();
		for (Player player : this.thePlayers) {
			if (winner.isEmpty()) {
				winner.add(player);
			} else {
				if (player.getScore() >= winner.get(0).getScore()) {
					if (player.getScore() > winner.get(0).getScore()) {
						winner.clear();
					}
					winner.add(player);
				}
			}
		}
		return winner;
	}

	/**
	 * Returns true if the game is finished (no more round to be played , or full
	 * board)
	 *
	 * @return true if the game is finished
	 **/
	public boolean isFinished() {
		return (maxRounds == 0 || this.board.getAvailableCells().size() == 0);
	}

	/**
	 * Plays a game with all players until it is finished after initializing their
	 * Resource inventory
	 */
	public void play() {
		this.initPlayerRes();
		int round = 1; 
		while (!this.isFinished()) {
			System.out.println("__________________________________\n\n");
			System.out.println("###################");
			System.out.println(" START OF ROUND " + round);
			System.out.println("###################");
			round++;
			this.playOneRound();
		}
		this.displayEnd();
	}

	/**
	 * Displays the winner of the game, in case of tie display all players with the
	 * higher score
	 *
	 * @param winner list of players with the higher score
	 * @return the String of the winner Display
	 */
	public String displayWinner(ArrayList<Player> winner) {
		String winnerDisplay;

		if (winner.size() == 1) {
			winnerDisplay = " =====> (^o^)b " + winner.get(0).toString().toUpperCase()
					+ " WON THE GAME ! d(^o^) <===== ";
		} else {
			winnerDisplay = " It's a tie between : ";
			for (Player player : winner) {
				winnerDisplay += (player.toString() + " ");
			}
		}

		int starCountDisplay = winnerDisplay.length();

		System.out.println("\n\n" + "*".repeat(starCountDisplay) + "\n");
		System.out.println(winnerDisplay);
		System.out.println("\n\n" + "*".repeat(starCountDisplay));
		
		return winnerDisplay;
	}

	/**
	 * Display the score of all the players of the game
	 */
	public void displayScore() {
		for (Player player : this.thePlayers) {
			System.out.println("\n ===> " + player.toString().toUpperCase() + " HAS " + player.getScore() + " "
					+ (player.getScore() == 1 ? "point" : "points") + " !");
		}
	}

	/**
	 * Displays all relevant information at the end of a game
	 */
	public void displayEnd() {
		System.out.println("__________________________________\n\n");
		System.out.println("\n#############\n GAME OVER !\n#############\nLet's settle the scores!");
		this.countAllScore();
		this.displayScore();
		this.displayWinner(this.getWinner());
	}

	/**
	 * Adds a strategy to the specified list of strategy. ### FOR DEBUG PURPOSES ###
	 *
	 * @param move     the move to be added
	 * @param moveList of the choosable moves for this game
	 */
	public void addSomeMove(Move move, List<Move> moveList) {
		moveList.add(move);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
		for (Cell cell: board.getAllCells())
			cell.addPropertyChangeListener(listener);
		for (Player player: thePlayers)
			player.addPropertyChangeListener(listener);
	}
	
	/**
	 * @return is paused or not
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused status to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
}
