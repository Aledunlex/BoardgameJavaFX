package boardgame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import boardgame.fabrices.FabriceArmy;
import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.game.GameWar;
import boardgame.players.EcoPlayer;
import boardgame.players.WarPlayer;
import boardgame.strategy.RandomStrat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable, PropertyChangeListener {
	
	private static int ROUNDS = 10;
	private static int BOARD_WIDTH = 10;
	private static int BOARD_LENGHT = 10;

	private Cell clickedCell;
	private GridPane buttonsContainer;
	private Game theGame;
	private Node[][] gridPaneArray = null;
	
	@FXML
	private Pane boardPane;
	@FXML
	private Label cellLabel, busyLabel, usableLabel, surroundingsLabel, bonusLabel;
	@FXML
	private Label gameProgressLabel, currentRound, winnerDisplay;
	@FXML
	private Label eachplayernbofdeployed, eachplayerremainingfood, eachplayerunitsownedgold;
	@FXML
	private Button startButton;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("appel du contrôleur");
		Player player = new WarPlayer("any", new RandomStrat()); /* player sera utilise pour determiner le type de jeu a lancer... pas tres opti mais c'est du bricolage */
		initGame(player);
		initBoardView();
	}
	
	
	@FXML
	protected void handleCellClicked(ActionEvent e) {
		int row = GridPane.getRowIndex((Button) e.getSource());
		int column = GridPane.getColumnIndex((Button) e.getSource());
		clickedCell = theGame.getBoard().getCell(row, column);
		updateCellStatus();
	}
	
	@FXML
	/* appelé par le bouton start game */
	protected void startGame(ActionEvent e) {
		if (!theGame.isFinished()) {
			theGame.playOneRound();
			if (theGame.isFinished()) {
				theGame.displayEnd();
				updateWinnerLabel();
			}
		}
	}
	
	private void updateCellStatus() {
		cellLabel.setText(clickedCell.getId() + " at [" + clickedCell.getX() + "," + clickedCell.getY() + ']');
		busyLabel.setText("Is busy : " + (clickedCell.isBusy()?"Yes, it's occupied by "+clickedCell.getUnit().toString():"No")+".");
		usableLabel.setText("Is usable : " + (clickedCell.usableInThisGame()?"Yes, it produces "+clickedCell.getResource().display():"No")+".");
		/*surroundingsLabel.setText("Is surrounded by : " + (determineSurroundings()?determineSurroundings()):"No one.");*/
		bonusLabel.setText((clickedCell.getBonus()>0)?"End game bonus for owning this cell : " + clickedCell.getBonus():"");
	}
	
	/** Les Cell sont chargées et transformées en boutons sur l'interface.
	 * Les informations sont (a priori) modifiées quand une Unit y est déployée ou en est retirée (ou traitorousUnit()).
	 * Pour chaque tuile du plateau, on va créer un bouton; chaque bouton forme le plateau sur l'interface. 
	 * De plus, chaque bouton est cliquable et renvoie sur une autre fenêtre qui donne les informations 
	 * relatives à la tuile cliquée.
	 * 
	 */
	private void initBoardView() {
		BoardGame board = theGame.getBoard();
		int boardY = board.getWidth();
		int boardX = board.getLength();
		buttonsContainer = new GridPane();
		boardPane.getChildren().add(buttonsContainer);
		for (int y = 0; y < boardY; y++) {
			for (int x = 0; x < boardX; x++) {
				Cell cell = board.getCell(x, y);
				Button res = new Button(cell.display());
				res.setId("button-"+cell.getId());
				res.getStylesheets().add(getClass().getResource("/resources/css/Main.css").toExternalForm());
				res.setOnAction(e -> handleCellClicked(e)); 
				buttonsContainer.add(res,y,x);
			}
		}
		initializeGridPaneArray();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "maxRounds") {
			updateRoundLabel();
		}
		if (evt.getPropertyName() == "currentUnit") {
			updateCellButtonId(evt);
		}
		if (evt.getPropertyName() == "stuffToPleaseUnit") {
			updateStuffToPleaseLabel(evt);
		}
		if (evt.getPropertyName() == "currentUnit"  || evt.getPropertyName() == "controlledCells") {
			updateOwnedCellsLabel(evt);
		}
		/* les autres evt ne sont pas ou mal détectés, jsp pourquoi */
		/* pour debug */
		//else {System.out.println("###########################La source est : ".toUpperCase() + evt.getPropertyName());}
	}
	
	private void updateCellButtonId(PropertyChangeEvent evt) {
		Cell cell = (Cell) evt.getSource();
		Button button = getButtonAt(cell);
		String btnId = button.getId();
		if (evt.getNewValue() == null) {
			btnId = btnId.substring(0, btnId.length()-5);
		}
		else {
			btnId += "-busy";
		}
		button.setId(btnId);
	}
	
	private void updateStuffToPleaseLabel(PropertyChangeEvent evt) {
		String stuffToPleaseByPlayer = "";
		for (Player player : theGame.getThePlayers()) {
			stuffToPleaseByPlayer += "\n* "+ player.toString() + " has " + player.getNeedQty() + player.needToString(player.getNeedQty());
		}
		eachplayerremainingfood.setText(stuffToPleaseByPlayer);
	}
	
	private void updateOwnedCellsLabel(PropertyChangeEvent evt) {
		String ownedCellsByPlayer = "";
		for (Player player : theGame.getThePlayers()) {
			if(player.allControlledCells() != null) {
				ownedCellsByPlayer += "\n* "+ player.toString() + " owns " + player.allControlledCells().size() + " cells.";
			}
		}
		eachplayernbofdeployed.setText(ownedCellsByPlayer);
	}
	
	private void updateRoundLabel() {
		int currRound = ROUNDS - theGame.maxRounds;
		String roundLabelText = String.valueOf(currRound);
		if (theGame.maxRounds == 0) { 
			roundLabelText += " (final)";
		}
		currentRound.setText(roundLabelText);
	}

	private void updateWinnerLabel() {
		gameProgressLabel.setText("Game over!");
		winnerDisplay.setText(theGame.displayWinner(theGame.getWinner()));
	}
	
	/* les deux methodes ci dessous servent a recuperer le bouton
	 * qui a les coordonnees de la cell d'interet
	 */
	private Button getButtonAt(Cell cell) {
		int x = cell.getX();
		int y = cell.getY();
	    Button result = null;    
    	result = (Button) this.gridPaneArray[x][y];
	    return result;
	}
	
    private void initializeGridPaneArray() {
    	this.gridPaneArray = new Node[BOARD_WIDTH][BOARD_LENGHT];
    	for(Node node : this.buttonsContainer.getChildren()) {
    		this.gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
    	}
    }
    
    /* pas tres classe mais c'est temporaire, dans l'ideal plus tard on pourra cliquer 
     * sur un bouton pour choisir le type de jeu qu'on veut et entrer dans une barre
     * d'input les noms des joueurs a ajouter
     */
	private void initGame(Player player) {
		Strategy strat = new RandomStrat();
		Player player1;
		Player player2;
		if (player instanceof WarPlayer) {
			player1 = new WarPlayer("Arnold", strat);
			player2 = new WarPlayer("Musclor", strat);
		}
		else if (player instanceof EcoPlayer) {
			player1 = new EcoPlayer("Gandhi", strat);
			player2 = new EcoPlayer("Odette", strat);
		}
		else {player1 = null; player2 = null;}
		BoardGame board = createBoard(player);
		Game game;
		if (player1 instanceof WarPlayer) {game = new GameWar(board, ROUNDS); }
		else if (player1 instanceof EcoPlayer) {game = new GameEco(board, ROUNDS);}
		else {game = null;}
		System.out.println(player1);
		game.addPlayer(player1);
		game.addPlayer(player2);
		theGame=game;
		theGame.addPropertyChangeListener(this);
		theGame.initPlayerRes();
	}
	
	private BoardGame createBoard(Player player) {
		BoardGame board;
		Fabrice fabrice;
		if (player instanceof WarPlayer) {fabrice = new FabriceArmy();}
		else if (player instanceof EcoPlayer) {fabrice = new FabriceEcolo();}
		else {fabrice = null;}
		board = new BoardGame(BOARD_WIDTH, BOARD_LENGHT, fabrice);
		while (board.getAvailableCells().size() < 5) {
			board = createBoard(player);
		}
		return board;
	}

}
