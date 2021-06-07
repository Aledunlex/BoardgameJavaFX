package boardgame;
	
import boardgame.fabrices.FabriceEcolo;
import boardgame.game.GameEco;
import boardgame.players.EcoPlayer;
import boardgame.strategy.RandomStrat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			System.out.println("# Beginning of eco main #");

			Strategy strat = new RandomStrat();
			BoardGame board = createEcoBoard();
			GameEco game = new GameEco(board, 6);

			EcoPlayer philippe = new EcoPlayer("Gandhi", strat);
			EcoPlayer musclor = new EcoPlayer("Odette", strat);
			game.addPlayer(philippe);
			game.addPlayer(musclor);

			placeButtons(root, board);
			game.play();

			System.out.println("\n # End of eco main #");
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static BoardGame createEcoBoard() {
		BoardGame board = new BoardGame(10, 10, new FabriceEcolo());
		while (board.getAvailableCells().size() < 2) {
			board = createEcoBoard();
		}
		return board;
	}
	
	private void placeButtons(BorderPane root, BoardGame board) {
		int boardY = board.getWidth();
		int boardX = board.getLength();
		for (int y = 1; y < boardY; y++) {
			for (int x = 1; x < boardX; x++) {
				Button res = new Button(board.getCell(boardX, boardY).getId());
				res.setId("button-"+board.getCell(boardX, boardY).getId());
				
			}

		}
	}
}
