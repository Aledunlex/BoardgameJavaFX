package boardgame;
	
import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
import boardgame.strategy.RandomStrat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 * DEPRECATED
 * @author Alexandre
 *
 */

public class JFXEcoMainRandom extends Application {
	private HBox mainScreenHBox;
	private Scene mainScreenScene;
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("# Beginning of eco main #");
			
			this.primaryStage = primaryStage;
			primaryStage.setTitle("Boardgame - Eco Game - Random Strategy - 2 predefined players - 6 rounds, 10x10 board");
			
			mainScreenHBox = new HBox();
			
			mainScreenScene = new Scene(mainScreenHBox);
			
			mainScreenScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Strategy strat = new RandomStrat();
			BoardGame board = createEcoBoard();
			GameEco game = new GameEco(board, 6);
			EcoPlayer philippe = new EcoPlayer("Gandhi", strat);
			EcoPlayer musclor = new EcoPlayer("Odette", strat);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
			
			

			initBoardView(board);
			game.play();
			
			initGameIntelVBox(game);
			initPlayersIntelVBox(game);
			primaryStage.setScene(mainScreenScene);
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.centerOnScreen();


			System.out.println("\n # End of eco main #");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static BoardGame createEcoBoard() {
		BoardGame board = new BoardGame(10, 10, new FabriceEcolo());
		while (board.getAvailableCells().size() < 5) {
			board = createEcoBoard();
		}
		return board;
	}
	
	/** Les Cell sont charg�es avec les �ventuelles Unit plac�es dessus.
	 * Les informations sont (a priori) modifi�es quand une Unit y est d�ploy�e ou en est retir�e (ou traitorousUnit()).
	 * Pour chaque tuile du plateau, on va cr�er un bouton; chaque bouton forme le plateau sur l'interface. 
	 * De plus, chaque bouton est cliquable et renvoie sur une autre fen�tre qui donne les informations 
	 * relatives � la tuile cliqu�e.
	 * ?TODO? Plut�t que de renvoyer vers une nouvelle fen�tre, impl�menter les deux fen�tres c�te � c�te?
	 * 
	 * @param board du jeu
	 */
	private void initBoardView(BoardGame board) {
		int boardY = board.getWidth();
		int boardX = board.getLength();
		for (int y = 0; y < boardY; y++) {
			VBox buttonsContainer = new VBox();
			mainScreenHBox.getChildren().add(buttonsContainer);
			for (int x = 0; x < boardX; x++) {
				Cell cell = board.getCell(x, y);
				VBox cellContentVBox = new VBox(5);
				Scene cellScene = new Scene(cellContentVBox);

				Button res = new Button(board.getCell(x, y).display());
				res.setId("button-"+board.getCell(x, y).getId());
				res.setOnAction(e -> primaryStage.setScene(cellScene)); 
				buttonsContainer.getChildren().add(res);
				initCellScene(cell, cellScene, cellContentVBox);
			}
		}
	}
	
	/** Remplit la VBox fournie avec les informations pertinentes relatives � la Cell
	 * 
	 * @param cell				pour laquelle un bouton a �t� cr��
	 * @param cellScene			la Scene, acc�d�e par le bouton, et qui contiendra la VBox 
	 * @param cellContentVBox	la VBox qui contiendra les Label avec les informations pertinentes
	 */
	private void initCellScene(Cell cell, Scene cellScene, VBox cellContentVBox) {
		Label titleLabel= new Label("Cell details");
		titleLabel.setId("main-title");
		Button button2= new Button("Back to board");
		button2.setId("menu-button");
		button2.setPrefWidth(5000);
		button2.setOnAction(e -> primaryStage.setScene(mainScreenScene));
		
		Label label1 = new Label(cell.getId() + " [" + cell.getX() + ";" + cell.getY() + "]");
		label1.setId("sub-title");
		Label label2 = cell.getBusyLabel();
		Label label3 = cell.getUsableLabel();
		Label hasBonus = cell.getBonusLabel();
		
		updateCellStatus(cell);
		
		cellContentVBox.getChildren().addAll(button2, titleLabel, label1, label2, label3, hasBonus);
		cellContentVBox.setId("cell-scene");
		
		cellScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	private void updateCellStatus(Cell cell) {
		cell.updateCellStatus();
	}
	
	private void initGameIntelVBox(Game game) {
		VBox gameIntelVBox = new VBox(10);
		Label titleLabel= new Label("Game intel");
		titleLabel.setId("main-title");
		Button button2= new Button("Ne cliquez pas");
		button2.setId("menu-button");
		button2.setOnAction(e -> System.out.println("Coucou! Pourquoi tu clique sur ce bouton?"));
		
		Label label1 = new Label("This is an Eco Game");
		label1.setId("sub-title");
		String availableLabelString = "Available cells : ";
		for (Cell cell : game.getBoard().getAvailableCells()) {
			availableLabelString += "\n- "+ cell.toString();
		}
		String theMovesString = "This game includes these moves : ";
		for (Move move : game.getTheMoves()) {
			theMovesString += "\n- "+ move.display()+"(chosable)";
		}
		for (Move move : game.getMandatoryMoves()) {
			theMovesString += "\n- "+ move.display()+"(mandatory)";
		}
		
		Label availableCellsLabel = new Label(availableLabelString);
		Label winnerLabel = new Label("Winner : " + game.getWinner().toString() + (game.getWinner().size()>1?" (tie).":"."));
		Label allMovesLabel = new Label(theMovesString);
		
		updateGameStatus(game);
		
		gameIntelVBox.getChildren().addAll(button2, titleLabel, label1, availableCellsLabel, winnerLabel, allMovesLabel);
		mainScreenHBox.getChildren().add(gameIntelVBox);
	}
	
	private void initPlayersIntelVBox(Game game) {
		VBox playerIntelVBox = new VBox(10);
		Label titleLabel= new Label("Players intel");
		titleLabel.setId("main-title");
		Button button2= new Button("Ne cliquez VRAIMENT pas!");
		button2.setId("menu-button");
		button2.setOnAction(e -> System.out.println("Mais? Arr�te d'appuyer!"));
		
		Label subTitleLabel = new Label("Gandhi VS Odette");
		subTitleLabel.setId("sub-title");
		String deployedUnits = "";
		for (Player player : game.getThePlayers()) {
			deployedUnits += "\n- "+player.toString()+" deployed :";
			for (Unit unit : player.allDeployedUnits()) {
				deployedUnits += "\n--- "+unit.toString();
			}
			deployedUnits += "\n";
		}
		String necessityOwned = "";
		for (Player player : game.getThePlayers()) {
			necessityOwned += "\n- "+ player.toString() + " has " + player.getNeedQty() + player.needToString(player.getNeedQty());
		}
		String totalOwnedGold = "";
		for (Player player : game.getThePlayers()) {
			totalOwnedGold += "\n- "+player.toString() + "'s units cumulated a total of ";
			int unitsGold = 0;
			if (!player.allDeployedUnits().isEmpty()) {
				for (Unit unit : player.allDeployedUnits())
					unitsGold += unit.getGold();
			}
			totalOwnedGold += unitsGold + " coin" +(unitsGold==1?"":"s") +".";
		}
		
		Label deployedUnitsLabel = new Label(deployedUnits);
		Label unitsOwnedGOldLabel = new Label(totalOwnedGold);
		Label ownsNecessities = new Label(necessityOwned);
		
		updateGameStatus(game);
		
		playerIntelVBox.getChildren().addAll(button2, titleLabel, subTitleLabel, deployedUnitsLabel, unitsOwnedGOldLabel, ownsNecessities);
		mainScreenHBox.getChildren().add(playerIntelVBox);
	}
	
	/** Devra mettre � jour les diff�rents labels ci-dessus
	 * Il faudra aussi faire un updatePlayersStatus(Game game)
	 * 
	 * @param game dont il faut mettre � jour les labels
	 */
	private void updateGameStatus(Game game) {
		//game.updateCellStatus();
	}
	
	/*
	private class CellButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			updateCellStatus();
		}
	}
	*/
}