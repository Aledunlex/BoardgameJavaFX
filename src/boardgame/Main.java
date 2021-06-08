package boardgame;
	
import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
import boardgame.strategy.RandomStrat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {
	private VBox boardVBox;
	private Scene boardScene;
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			primaryStage.setTitle("Boardgame");
			
			boardVBox = new VBox();
			
			boardScene = new Scene(boardVBox,600,600);
			
			boardScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			
			System.out.println("# Beginning of eco main #");

			Strategy strat = new RandomStrat();
			BoardGame board = createEcoBoard();
			GameEco game = new GameEco(board, 6);

			EcoPlayer philippe = new EcoPlayer("Gandhi", strat);
			EcoPlayer musclor = new EcoPlayer("Odette", strat);
			game.addPlayer(philippe);
			game.addPlayer(musclor);
			
			
			
			game.play();

			System.out.println("\n # End of eco main #");
			
			initBoardView(board);
			primaryStage.setScene(boardScene);
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.centerOnScreen();
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
	
	/** Pour le moment est appelé après la partie, les Cell sont chargées avec les éventuelles Unit placées dessus.
	 * Si appelé avant la partie, les informations ne sont pas (encore) mises à jour au fil de la partie.
	 * Pour chaque tuile du plateau, on va créer un bouton; chaque bouton forme le plateau sur l'interface. 
	 * De plus, chaque bouton est cliquable et renvoie sur une autre fenêtre qui donne les informations 
	 * relatives à la tuile cliquée.
	 * ?TODO? Implémenter la MAj dynamique des informations contenues dans cette autre fenêtre au fil de la partie.
	 * ?TODO? Plutôt que de renvoyer vers une nouvelle fenêtre, implémenter les deux fenêtres côte à côte?
	 * 
	 * @param board du jeu
	 */
	private void initBoardView(BoardGame board) {
		int boardY = board.getWidth();
		int boardX = board.getLength();
		for (int y = 0; y < boardY; y++) {
			HBox coordinatesLabels = new HBox();
			HBox buttonsContainer = new HBox();
			boardVBox.getChildren().addAll(coordinatesLabels, buttonsContainer);
			for (int x = 0; x < boardX; x++) {
				Cell cell = board.getCell(x, y);
				VBox cellContentVBox = new VBox(5);
				Scene cellScene = new Scene(cellContentVBox,600,600);
				if (y==0) {
					int rectSize = 600/boardX;
					Rectangle rectangle = new Rectangle(rectSize, rectSize); 
					coordinatesLabels.getChildren().add(rectangle);
				}
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
		titleLabel.setShape(new Rectangle(10,10));
		Button button2= new Button("Back to board");
		button2.setId("menu-button");
		button2.setPrefWidth(5000);
		button2.setOnAction(e -> primaryStage.setScene(boardScene));
		
		Label label1 = new Label(cell.getId() + " [" + cell.getX() + ";" + cell.getY() + "]");
		Label label2 = new Label("Is busy : " + booleanToYesNo(cell.isBusy())); 
		Label label3 = new Label("Is usable : " + booleanToYesNo(cell.usableInThisGame())); 
		cellContentVBox.getChildren().addAll(button2, titleLabel, label1, label2, label3);
		cellContentVBox.setId("cell-scene");
		
		if (cell.isBusy()) { Label occupiedBy = new Label("Occupied by : " + cell.getUnit().toString()); cellContentVBox.getChildren().add(occupiedBy); }
		if (cell.usableInThisGame()) { Label producesSome = new Label("Produces : " + cell.getResource().display()); cellContentVBox.getChildren().add(producesSome); }
		if (cell.getBonus()>0) { Label hasBonus = new Label("End game bonus for owning this cell : " + cell.getBonus()); cellContentVBox.getChildren().add(hasBonus); }
		
		cellScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	/** Ca coule de source...
	 * 
	 * @param bool le booléen à convertir en String
	 * @return lire le nom de la méthode...
	 */
	private String booleanToYesNo(boolean bool) {
		if (bool) { return "Yes.";}
		else { return "No.";}
	}
}
