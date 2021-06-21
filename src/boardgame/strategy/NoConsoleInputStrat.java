package boardgame.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import boardgame.BoardGame;
import boardgame.Cell;
import boardgame.Move;
import boardgame.Resource;
import boardgame.Strategy;
import boardgame.Unit;
import boardgame.util.AbstractPropertyChangeable;
import boardgame.util.Input;

/**
 * An implementation of Strategy, to be used with a Player object to let them
 * make choices within the game's limits, based on a user's inputs (not in
 * the console but in the 
 */

public class NoConsoleInputStrat extends AbstractPropertyChangeable implements Strategy {
	
	private String warningText = "";
	private String messageText = "";
	private String availableSelection = "";
	private String inputValue = "0";
	
	public NoConsoleInputStrat() {
	}

	@Override
	public Unit chooseUnit(List<Unit> ownedUnits) {
		setMessageText("Please select one of your units :");
		int i = 1;
		String res = "";
		for (Unit unit : ownedUnits) {
			res += "[" + i + "] " + unit.typeToString(true) + unit.getCell().toString() + "\n";
			i++;
		}
		setAvailableSelection(res);
		int choice = this.checkCorrectInput(1, ownedUnits.size() + 1, "unit");
		return ownedUnits.get(choice - 1);
	}

	@Override
	public Cell chooseCell(BoardGame board) {
		boolean possibleToPlace = false;
		Cell cell = null;
		setMessageText("Select a box : ");
		while (!possibleToPlace) {
			int x = this.chooseX(0, board.getWidth());
			int y = this.chooseY(0, board.getLength());
			cell = board.getCell(x, y);
			if (!cell.isBusy()) {
				possibleToPlace = true;
			} else {
				setWarningText("This box is already occupied, you have to choose another one.");
			}
		}
		return cell;
	}

	@Override
	public int chooseSize(int min, int max) {
		setMessageText("Enter the size :");
		return this.checkCorrectInput(min, max, "size");
	}

	@Override
	public int chooseAmount(int min, int max) {
		setMessageText("Enter the amount :");
		return this.checkCorrectInput(min, max, "amount");
	}

	@Override
	public int chooseX(int min, int max) {
		setMessageText("Enter x :");
		return this.checkCorrectInput(min, max, "x");
	}

	@Override
	public int chooseY(int min, int max) {
		setMessageText("Enter y :");
		return this.checkCorrectInput(min, max, "y");
	}

	/**
	 * Returns an int from the player, the int has to be between min (included) and
	 * max (excluded), asks for an input until the input is correct
	 *
	 * @param min  minimum value (included) of the input int
	 * @param max  maximum value (excluded) of the input int
	 * @param name name of the value ask by the input (use only for display)
	 * @exception java.io.IOException if input does not correspond to an int
	 * @return correct int the player chose
	 */
	public int checkCorrectInput(int min, int max, String name) {
		boolean saisieCorrect = false;
		int value = 0;

		if (!saisieCorrect) {
			setMessageText(name + " : ");
			value = Integer.valueOf(inputValue);

			if (value < min) {
				setWarningText("The input is too small, it must be greater than or equal to " + min);
			}

			else if (value >= max) {
				setWarningText("The input is too large, it must be less than " + max);
			}

			else {
				saisieCorrect = true;
			}
		}
		return value;

	}

	@Override
	public String chooseResourceType(HashMap<String, ArrayList<Resource>> resource) {
		Set<String> key = resource.keySet();
		String[] ar = key.toArray(new String[0]);
		setMessageText("Choose a resource to convert");
		int i = 1;
		String res = "";
		for (String k : key) {
			res +="[" + i + "] " + k + " (" + resource.get(k).size() + " available)\n";
			i++;
		}
		res += "[" + i + "] convert nothing";
		int choice = this.checkCorrectInput(1, ar.length + 2, "choice");
		setAvailableSelection(res);
		if (choice == ar.length + 1) {
			return null;
		} else {
			return ar[choice - 1];
		}
		
	}

	@Override
	public Move chooseMove(List<Move> moves) {
		setMessageText("What do you want to do ?");
		int i = 1;
		String res = "";
		for (Move move : moves) {
			res +="[" + i + "] " + move.display() + "\n";
			i++;
		}
		setAvailableSelection(res);
		int choice = this.checkCorrectInput(1, moves.size() + 1, "choice");
		return moves.get(choice - 1);
	}
	
	public String getWarningText() {
		return warningText;
	}

	public void setWarningText(String warningText) {
		String prev = this.warningText;
		this.warningText = warningText;
		propertyChangeSupport.firePropertyChange("warningText", prev, warningText);
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		String prev = this.messageText;
		this.messageText = messageText;
		propertyChangeSupport.firePropertyChange("messageText", prev, messageText);
	}

	public String getAvailableSelection() {
		return availableSelection;
	}

	public void setAvailableSelection(String availableSelection) {
		String prev = this.availableSelection;
		this.availableSelection = availableSelection;
		propertyChangeSupport.firePropertyChange("availableSelection", prev, availableSelection);
	}
	
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	/*
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
	}*/
}
