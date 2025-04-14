package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Doodlebug extends Bug<Doodlebug> {
    int starvationTracker = 0;
    public static int doodlebugCounter = 0;
    public static int killCount = 0;
    public static int numOfDoodlebugBreeds = 0;

	private Game game = Game.getInstance();

	public Doodlebug() {
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Doodlebug");
	}

	@Override
	public void move(Turn turn) {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isOccupantEatable(getCell())) {
				adjCell.removeOccupant();
				assignCell(adjCell);
				starvationTracker = 0;
                turn.setKillCount();
                killCount++;
				break;
			} else if (adjCell.isInBounds() && adjCell.isEmpty()) {
				assignCell(adjCell);
				starvationTracker++;
				break;
			}
		}
		movementCounter++;

		if (starvationTracker == 3) {
			removeFromCell();
		}

		if (movementCounter == 8) {
			movementCounter = 0;
			this.breed(turn);
		}
	}

	@Override
	public void breed(Turn turn) {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Doodlebug());
                numOfDoodlebugBreeds++;
                turn.setDoodlebugBreedCount();
				break;
			}
		}
	}

	@Override
	public String toString() {
		return String.format(Console.withConsoleColors(
				"$text-green Doodlebug$text-reset #%s"),
				idNum);
	}

	@Override
	public String serialize() {
		return "Doodlebug{}";
	}
}
