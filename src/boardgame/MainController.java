package boardgame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;

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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable, PropertyChangeListener {

    private static final int ROUNDS = 5;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_LENGTH = 10;

    private NoConsoleInputStrat inputStrat;
    private final Object loopKey = new Object();

    private Cell clickedCell;
    private GridPane buttonsContainer;
    private Game theGame;

    private Node[][] gridPaneArray = null;

    @FXML
    private TextArea console;
    private PrintStream ps;

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

    public MainController() {
    }

    /* Le joueur player sera utilise pour determiner le type de jeu a lancer...
     * pas tres opti mais c'est du bricolage */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("appel du controleur");

        initGame();
        initBoardView();

        /* ce bloc detourne le system.out.println() de la sortie standard vers le TextArea de l'interface */
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));

        updateRoundLabel();
    }

    private void appendText(String str) {
        Platform.runLater(() -> console.appendText(str));
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
        inputField.setVisible(true);
        inputButton.setVisible(true);
        startButton.setVisible(false);
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
        inputField.setVisible(false);
        inputButton.setVisible(false);
        startButton.setVisible(true);
        return value;
    }

    @FXML
    /* appele par le bouton "start game / next round"
     * Lance un tour de jeu si le jeu n'est pas encore fini; une fois fini,
     * le calcul de score et l'affichage du vainqueur sont faits.
     */
    protected void startGame(ActionEvent e) {
        if (!theGame.isFinished() && theGame.roundEnded()) {
            theGame.playOneRound();
            if (theGame.isFinished()) {
                theGame.displayEnd();
                updateWinnerLabel();
                playerLabel.setText("");
                startButton.setVisible(false);
            }
        }
        else {System.out.println("ca sert a rien de cliquer a ce moment!");}
    }

    private void updateCellStatus() {
        cellLabel.setText(clickedCell.getId() + " at [" + clickedCell.getX() + "," + clickedCell.getY() + ']');
        busyLabel.setText("Is busy : " + (clickedCell.isBusy()?"Yes, it's occupied by "+clickedCell.getUnit().toString():"No")+".");
        usableLabel.setText("Is usable : " + (clickedCell.usableInThisGame()?"Yes, it produces "+clickedCell.getResource().display():"No")+".");
        /*surroundingsLabel.setText("Is surrounded by : " + (determineSurroundings()?determineSurroundings()):"No one.");*/
        bonusLabel.setText((clickedCell.getBonus()>0)?"End game bonus for owning this cell : " + clickedCell.getBonus():"");
    }

    /** Les Cell sont chargees et transformees en boutons sur l'interface.
     * Les informations sont (a priori) modifiees quand une Unit y est deployee ou en est retiree (ou traitorousUnit()).
     * Pour chaque tuile du plateau, on va creer un bouton; chaque bouton forme le plateau sur l'interface.
     * De plus, chaque bouton est cliquable et renvoie sur une autre fenetre qui donne les informations
     * relatives a la tuile cliquee.
     *
     */
    private void initBoardView() {
        BoardGame board = theGame.getBoard();
        int boardY = board.getWidth();
        int boardX = board.getLength();
        buttonsContainer = new GridPane();
        if (boardPane != null) {boardPane.getChildren().add(buttonsContainer);}
        for (int y = 0; y < boardY; y++) {
            for (int x = 0; x < boardX; x++) {
                Cell cell = board.getCell(x, y);
                Button res = new Button(cell.display());
                res.setId("button-"+cell.getId());
                res.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/resources/css/Main.css")).toExternalForm());
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
        if (evt.getPropertyName().equals("maxRounds")) {
            updateRoundLabel();
        }
        if (evt.getPropertyName().equals("currentUnit")) {
            updateCellButtonId(evt);
        }
        if (evt.getPropertyName().equals("stuffToPleaseUnit")) {
            updateStuffToPleaseLabel(evt);
        }
        if (evt.getPropertyName().equals("currentUnit") || evt.getPropertyName().equals("team")) {
            updateOwnedCellsLabel(evt);
        }
        if (evt.getPropertyName().equals("resources"))  {System.out.println("buenos dias FUCKBOYS");}
        if (evt.getPropertyName().equals("currentPlayer"))
            updateCurrentPlayerLabel(evt);
        if (evt.getPropertyName().equals("warningText") || evt.getPropertyName().equals("messageText") || evt.getPropertyName().equals("availableSelection") || evt.getPropertyName().equals("inputValue"))
            updateAllInputLabels(evt);
        /* les autres evt ne sont pas ou mal detectes, jsp pourquoi */
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
                ownedCellsByPlayer += "\n* "+ player + " owns " + (owned==0?"no":owned) + " cell" + (owned==1?"":"s") + ".";
            }
        }
        eachplayernbofdeployed.setText(ownedCellsByPlayer);
    }

    /**
     * Met a jour le numero de tour sur l'interface
     */
    private void updateRoundLabel() {
        int currRound = ROUNDS - theGame.maxRounds +1;
        String roundLabelText = String.valueOf(currRound);
        if (theGame.maxRounds == 1) {
            roundLabelText += " (final)";
        }
        if (theGame.maxRounds != 0 && currentRound != null) {
            currentRound.setText(roundLabelText);
        }
    }

    /**
     * Affiche le nom du vainqueur en fin de partie
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
        this.gridPaneArray = new Node[BOARD_WIDTH][BOARD_LENGTH];
        for(Node node : this.buttonsContainer.getChildren()) {
            this.gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
        }
    }

    private void initGame() {
        NoConsoleInputStrat inputStrat = new NoConsoleInputStrat(this);
        this.inputStrat = inputStrat;
        inputStrat.addPropertyChangeListener(this);
        RandomStrat randomStrat = new RandomStrat();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ruleset selection");
        alert.setHeaderText("What will you be playing today?");
        alert.setContentText("Choose your option.");

        ButtonType buttonWar = new ButtonType("WAR");
        ButtonType buttonHarvest = new ButtonType("HARVEST");

        alert.getButtonTypes().setAll(buttonWar, buttonHarvest);

        Fabrice ruleSet;
        Player player;
        BoardGame board;
        int nbPlayers = askHowMany();
        boolean oops = false;

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonWar) {
            player = new WarPlayer(askForName(), inputStrat);
            ruleSet = new FabriceArmy();
            board = createBoard(ruleSet);
            theGame = new GameWar(board, ROUNDS);
            theGame.addPlayer(player);
            for (int i = 0; i < nbPlayers; i++)
                theGame.addPlayer(new WarPlayer(askForName(), randomStrat));
        } else if (result.get() == buttonHarvest) {
            player = new EcoPlayer(askForName(), inputStrat);
            ruleSet = new FabriceEcolo();
            board = createBoard(ruleSet);
            theGame = new GameEco(board, ROUNDS);
            theGame.addPlayer(player);
            for (int i = 0; i < nbPlayers; i++)
                theGame.addPlayer(new EcoPlayer(askForName(), randomStrat));
        } else {
            oops = true;
            System.out.println("I didn't expect that.");
        }
        if (!oops) {
            theGame.addPropertyChangeListener(this);
            theGame.initPlayerRes();
        }
    }

    private String askForName() {
        int count;
        if (theGame != null)
            count = theGame.getThePlayers().size();
        else
            count = 0;

        TextInputDialog dialog = new TextInputDialog("Patrick");
        dialog.setTitle("Name selection");
        if (theGame == null) {dialog.setHeaderText("What's your name?");}
        else {dialog.setHeaderText("What's the new player's name?");}
        dialog.setContentText("Enter a name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !nameTaken(result.get()) && !result.get().trim().isEmpty()) {
            return result.get().trim();
        }
        else {
            System.out.println("Defaulted to Patrick.");
            return "Patrick" + String.valueOf(count);
        }
    }

    private int askHowMany() {
        List<String> choices = new ArrayList<>();
        choices.add("1 (recommended)");
        choices.add("2");
        choices.add("3");
        choices.add("4");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("1 (recommended)", choices);
        dialog.setTitle("Player amount selection");
        dialog.setHeaderText("How many players will you play against?");
        dialog.setContentText("Chose a possible amount of players (1 to 4) :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return Integer.parseInt(result.get().substring(0, 1));
        }
        else {System.out.println("Defaulted to 1."); return 1;}
    }

    private BoardGame createBoard(Fabrice fabrice) {
        BoardGame board;
        board = new BoardGame(BOARD_WIDTH, BOARD_LENGTH, fabrice);
        while (board.getAvailableCells().size() < 5) {
            board = createBoard(fabrice);
        }
        return board;
    }

    private boolean nameTaken(String name) {
        boolean result = false;
        if (theGame != null) {
            for (Player player : theGame.getThePlayers()) {
                if (player.getName().equals(name.trim())) {
                    result = true;
                }
            }
        }
        return result;
    }

}
