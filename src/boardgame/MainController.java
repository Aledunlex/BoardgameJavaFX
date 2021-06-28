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
import boardgame.strategy.NoConsoleInputStrat;
import boardgame.strategy.RandomStrat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable, PropertyChangeListener {
	
	private static int ROUNDS = 10;
	private static int BOARD_WIDTH = 10;
	private static int BOARD_LENGHT = 10;

	private NoConsoleInputStrat inputStrat;
	private final Object loopKey = new Object();
	
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
	private Button startButton, inputButton;
	@FXML
	private Label warningLabel, messageLabel, availableInputLabel, playerLabel;
	@FXML
	private TextField inputField;
	
	/* Le joueur player sera utilise pour determiner le type de jeu a lancer... 
	 * pas tres opti mais c'est du bricolage */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("appel du contrôleur");
		Player player = new WarPlayer("any", new RandomStrat()); 
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
	/**
	 * Apres avoir entre un chiffre dans le TextField et apres avoir clique sur le bouton associe,
	 * appel de cette fonction pour verifier que c'est bien un chiffre. Si oui, la valeur est passee
	 * a la NoConsoleInputStrat.
	 * @param e
	 */
	protected void validateInput(ActionEvent e) {
		try {
			int res =  Integer.parseInt(inputField.getText());
			inputStrat.setInputValue(res);
			Platform.exitNestedEventLoop(loopKey, null);
		}
		catch(IllegalArgumentException error) {
			System.out.println("nope");
		}

	}
	
	/**
	 * Presque le meme que dans RandomStrategy; est appele par NoConsoleInputStrat
	 * @param min value
	 * @param max value
	 * @param name de ce qui est input
	 * @return la valeur si correct, sinon 0
	 */
	public int checkCorrectInput(int min, int max, String name) {
		boolean saisieCorrect = false;
		int value = 0;
		String currText = inputStrat.getMessageText();
		while (!saisieCorrect) {
			inputStrat.setMessageText(currText + '\n' + name + " : ");
			Platform.enterNestedEventLoop(loopKey);
			value = NoConsoleInputStrat.getInputValue();
			if (value < min) {
				inputStrat.setWarningText("The input is too small, it must be greater than or equal to " + min);
			}
			else if (value >= max) {
				inputStrat.setWarningText("The input is too large, it must be less than " + max);
			}
			else {
				saisieCorrect = true;
			}
		}
		return value;
	}
	
	@FXML
	/* appelé par le bouton "start game / next round"
	 * Lance un tour de jeu si le jeu n'est pas encore fini; une fois fini, 
	 * le calcul de score et l'affichage du vainqueur sont faits.  
	 */
	protected void startGame(ActionEvent e) {
		if (!theGame.isFinished() && theGame.roundEnded()) {
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

	/**
	 * Quand une des proprietes d'un objet dont MainController est passe Listener change,
	 * cette modification est, normalement, recuperee ici sous forme de PropertyChangeEvent.
	 * On peut alors recuperer le nom de la propriete modifiee et faire ce qui est approprie.
	 */
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
		if (evt.getPropertyName() == "currentPlayer")
			updateCurrentPlayerLabel(evt);
		if (evt.getPropertyName() == "warningText" || evt.getPropertyName() == "messageText" || evt.getPropertyName() == "availableSelection" || evt.getPropertyName() == "inputValue")
			updateAllInputLabels(evt);
		/* les autres evt ne sont pas ou mal détectés, jsp pourquoi */
		/* pour debug */
		//else {System.out.println("###########################La source est : ".toUpperCase() + evt.getPropertyName());}
	}
	
	/**
	 * Modifie le nom du joueur dont c'est le tour sur l'interface
	 * @param evt
	 */
	private void updateCurrentPlayerLabel(PropertyChangeEvent evt) {
		String display = "Currently playing : ";
		playerLabel.setText(display + theGame.getCurrentPlayer().getName());
	}
	
	/**
	 * Modifie les affichages relatifs a la selection d'action sur l'interface
	 * @param evt
	 */
	private void updateAllInputLabels(PropertyChangeEvent evt) {
		NoConsoleInputStrat strat = this.inputStrat;
		warningLabel.setText(strat.getWarningText());
		messageLabel.setText(strat.getMessageText());
		availableInputLabel.setText(strat.getAvailableSelection());
	}
	
	/**
	 * Modifie l'id du bouton de la tuile selon qu'elle est occupee ou non, pour son CSS
	 * @param evt
	 */
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
	
	/**
	 * Met a jour la quantite de nourriture/or possedee par chaque joueur
	 * @param evt
	 */
	private void updateStuffToPleaseLabel(PropertyChangeEvent evt) {
		String stuffToPleaseByPlayer = "";
		for (Player player : theGame.getThePlayers()) {
			stuffToPleaseByPlayer += "\n* "+ player.toString() + " has " + player.getNeedQty() + player.needToString(player.getNeedQty());
		}
		eachplayerremainingfood.setText(stuffToPleaseByPlayer);
	}
	
	/**
	 * Met a jour la quantite de tuiles possedee par chaque joueur
	 * @param evt
	 */
	private void updateOwnedCellsLabel(PropertyChangeEvent evt) {
		String ownedCellsByPlayer = "";
		for (Player player : theGame.getThePlayers()) {
			if(player.allControlledCells() != null) {
				int owned = player.allControlledCells().size();
				ownedCellsByPlayer += "\n* "+ player.toString() + " owns " + (owned==0?"no":owned) + " cell" + (owned==1?"":"s") + ".";
			}
		}
		eachplayernbofdeployed.setText(ownedCellsByPlayer);
	}
	
	/**
	 * Met a jour le numero de tour sur l'interface
	 * @param evt
	 */
	private void updateRoundLabel() {
		int currRound = ROUNDS - theGame.maxRounds;
		String roundLabelText = String.valueOf(currRound);
		if (theGame.maxRounds == 0) { 
			roundLabelText += " (final)";
		}
		currentRound.setText(roundLabelText);
	}

	/**
	 * Affiche le nom du vainqueur en fin de partie
	 * @param evt
	 */
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
		RandomStrat strat = new RandomStrat();
		NoConsoleInputStrat inputStrat = new NoConsoleInputStrat(this);
		this.inputStrat = inputStrat;
		inputStrat.addPropertyChangeListener(this);
		Player player1;
		Player player2;
		if (player instanceof WarPlayer) {
			player1 = new WarPlayer("Arnold", inputStrat);
			player2 = new WarPlayer("Musclor", strat);
		}
		else if (player instanceof EcoPlayer) {
			player1 = new EcoPlayer("Gandhi", inputStrat);
			player2 = new EcoPlayer("Odette", strat);
		}
		else {player1 = null; player2 = null;}
		BoardGame board = createBoard(player);
		Game game;
		if (player1 instanceof WarPlayer) {game = new GameWar(board, ROUNDS); }
		else if (player1 instanceof EcoPlayer) {game = new GameEco(board, ROUNDS);}
		else {game = null;}
		game.addPlayer(player1);
		game.addPlayer(player2);
		theGame=game;
		theGame.addPropertyChangeListener(this);
		theGame.initPlayerRes();
	}
	
	/**
	 * Cree un board avec le fabrice correspondant au type de player passe en parametre.
	 * La creation de plateau est relancee tant qu'on n'a pas au moins 5 tuiles dessus.
	 * @param player
	 * @return un board.
	 */
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
