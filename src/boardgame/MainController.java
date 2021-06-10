package boardgame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
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
	
	private static int ROUNDS = 15;
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
	private Button startButton, nextPlayer;
	
	@FXML
	protected void handleCellClicked(ActionEvent e) {
		int row = GridPane.getRowIndex((Button) e.getSource());
		int column = GridPane.getColumnIndex((Button) e.getSource());
		clickedCell = theGame.getBoard().getCell(row, column);
		updateCellStatus();
	}
	
	@FXML
	protected void startGame(ActionEvent e) {
		theGame.play();
		updateWinnerLabel();
	}
	
	private void updateCellStatus() {
		cellLabel.setText(clickedCell.getId() + " at [" + clickedCell.getX() + "," + clickedCell.getY() + ']');
		busyLabel.setText("Is busy : " + (clickedCell.isBusy()?"Yes, it's occupied by "+clickedCell.getUnit().toString():"No")+".");
		usableLabel.setText("Is usable : " + (clickedCell.usableInThisGame()?"Yes, it produces "+clickedCell.getResource().display():"No")+".");
		/*surroundingsLabel.setText("Is surrounded by : " + (determineSurroundings()?determineSurroundings()):"No one.");*/
		bonusLabel.setText((clickedCell.getBonus()>0)?"End game bonus for owning this cell : " + clickedCell.getBonus():"");
	}

	@FXML
	/**
	 * Pas aussi simple
	 * @param e when player clicks "unpause" button, currently non functionnal
	 */
	private void nextPlayer(ActionEvent e) {
		if (theGame.isPaused())
			theGame.setPaused(false);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("appel du contrôleur");
		initEcoGame();
		initBoardView();
	}
	
	/** Les Cell sont chargées avec les éventuelles Unit placées dessus.
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
	
	public void initEcoGame() {
		Strategy strat = new RandomStrat();
		BoardGame board = createEcoBoard();
		GameEco game = new GameEco(board, ROUNDS);
		EcoPlayer philippe = new EcoPlayer("Gandhi", strat);
		EcoPlayer musclor = new EcoPlayer("Odette", strat);
		game.addPlayer(philippe);
		game.addPlayer(musclor);
		theGame=game;
		theGame.addPropertyChangeListener(this);
	}
	
	private BoardGame createEcoBoard() {
		BoardGame board = new BoardGame(BOARD_WIDTH, BOARD_LENGHT, new FabriceEcolo());
		while (board.getAvailableCells().size() < 5) {
			board = createEcoBoard();
		}
		return board;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "maxRounds") {
			updateRoundLabel();
		}
		else if (evt.getPropertyName() == "currentUnit") {
			updateCellButtonId(evt);
		}
		else if (evt.getPropertyName() == "stuffToPleaseUnit") {
			updateStuffToPleaseLabel(evt);
		}
		/* en dessous plus rien ne marche jsp pourquoi */
		else if (evt.getPropertyName() == "controlledCells") {
			System.out.println("couco#######################################################################################u");
			updateOwnedCellsLabel(evt);
		}
		/*else {System.out.println("La source est : " + evt.getSource());}*/
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
			ownedCellsByPlayer += "\n* "+ player.toString() + " owns " + player.allControlledCells() + " cells.";
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

}
