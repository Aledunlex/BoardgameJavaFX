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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable, PropertyChangeListener {
	
	private static int ROUNDS = 6;
	private static int BOARD_WIDTH = 10;
	private static int BOARD_LENGHT = 10;

	private Cell clickedCell;
	private GridPane buttonsContainer;
	private Game theGame;
	
	@FXML
	private Pane boardPane;
	@FXML
	private Label cellLabel, busyLabel, usableLabel, surroundingsLabel, bonusLabel;
	@FXML
	private Label currentRound, availableCells, winnerDisplay;
	
	@FXML
	protected void handleCellClicked(ActionEvent e) {
		int row = GridPane.getRowIndex((Button) e.getSource());
		int column = GridPane.getColumnIndex((Button) e.getSource());
		clickedCell = theGame.getBoard().getCell(row, column);
		updateCellStatus();
	}
	
	private void updateCellStatus() {
		cellLabel.setText(clickedCell.getId() + " at [" + clickedCell.getX() + "," + clickedCell.getY() + ']');
		busyLabel.setText("Is busy : " + (clickedCell.isBusy()?"Yes, it's occupied by "+clickedCell.getUnit().toString():"No")+".");
		usableLabel.setText("Is usable : " + (clickedCell.usableInThisGame()?"Yes, it produces "+clickedCell.getResource().display():"No")+".");
		/*surroundingsLabel.setText("Is surrounded by : " + (determineSurroundings()?determineSurroundings()):"No one.");*/
		bonusLabel.setText((clickedCell.getBonus()>0)?"End game bonus for owning this cell : " + clickedCell.getBonus():"");
	}
	
	
	
	private void determineSurroundings() {
		;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("appel du contrôleur");
		initEcoGame();
		initBoardView();
		theGame.play();
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
		for (int y = 0; y < boardY; y++) {
			buttonsContainer = new GridPane();
			boardPane.getChildren().add(buttonsContainer);
			for (int x = 0; x < boardX; x++) {
				Cell cell = board.getCell(y, x);
				Button res = new Button(cell.display());
				res.setId("button-"+cell.getId());
				res.getStylesheets().add(getClass().getResource("/resources/css/Main.css").toExternalForm());
				res.setOnAction(e -> handleCellClicked(e)); 
				buttonsContainer.add(res,y,x);
			}
		}
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
		// TODO Auto-generated method stub
		
	}

}
