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
			initGameIntelVBox(game);
			initPlayersIntelVBox(game);
			
			game.play();
			
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
	
	/** Les Cell sont chargées avec les éventuelles Unit placées dessus.
	 * Les informations sont (a priori) modifiées quand une Unit y est déployée ou en est retirée (ou traitorousUnit()).
	 * Pour chaque tuile du plateau, on va créer un bouton; chaque bouton forme le plateau sur l'interface. 
	 * De plus, chaque bouton est cliquable et renvoie sur une autre fenêtre qui donne les informations 
	 * relatives à la tuile cliquée.
	 * ?TODO? Plutôt que de renvoyer vers une nouvelle fenêtre, implémenter les deux fenêtres côte à côte?
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
	
	/** Remplit la VBox fournie avec les informations pertinentes relatives à la Cell
	 * 
	 * @param cell				pour laquelle un bouton a été créé
	 * @param cellScene			la Scene, accédée par le bouton, et qui contiendra la VBox 
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
		VBox gameIntelVBox = new VBox();
		Label titleLabel= new Label("Game intel");
		titleLabel.setId("main-title");
		Button button2= new Button("N'appuyez pas sur ce bouton");
		button2.setId("menu-button");
		button2.setOnAction(e -> System.out.println("Coucou! Pourquoi tu clique sur ce bouton?"));
		
		Label label1 = new Label("This is an Eco Game");
		label1.setId("sub-title");
		String availableLabelString = "Available cells : ";
		for (Cell cell : game.getBoard().getAvailableCells()) {
			availableLabelString += "\n-"+ cell.toString();
		}
		
		Label label2 = new Label(availableLabelString);
		Label label3 = new Label(game.getWinner().toString());
		Label hasBonus = new Label("Honnêtement, jsais pas trop quoi mettre");
		
		updateGameStatus(game);
		
		gameIntelVBox.getChildren().addAll(button2, titleLabel, label1, label2, label3, hasBonus);
		mainScreenHBox.getChildren().add(gameIntelVBox);
	}
	
	private void initPlayersIntelVBox(Game game) {
		VBox playerIntelVBox = new VBox(5);
		Label titleLabel= new Label("Players intel");
		titleLabel.setId("main-title");
		Button button2= new Button("N'appuyez VRAIMENT pas!");
		button2.setId("menu-button");
		button2.setOnAction(e -> System.out.println("Mais? Arrête d'appuyer!"));
		
		Label label1 = new Label("Gandhi VS Odette");
		label1.setId("sub-title");
		String availableLabelString = "Available cells : ";
		for (Cell cell : game.getBoard().getAvailableCells()) {
			availableLabelString += "\n-"+ cell.toString();
		}
		
		Label label2 = new Label(availableLabelString);
		Label label3 = new Label(game.getWinner().toString());
		Label hasBonus = new Label("Honnêtement, jsais pas trop quoi mettre");
		
		updateGameStatus(game);
		
		playerIntelVBox.getChildren().addAll(button2, titleLabel, label1, label2, label3, hasBonus);
		mainScreenHBox.getChildren().add(playerIntelVBox);
	}
	
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
