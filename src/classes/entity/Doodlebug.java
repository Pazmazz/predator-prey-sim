package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Doodlebug extends Bug<Doodlebug> {

	private Game game = Game.getInstance();

	int starvationTracker = 0;

	public Doodlebug() {
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, false);
	}

	@Override
	public void move() {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isOccupantEatable(getCell())) {
				adjCell.removeOccupant();
				assignCell(adjCell);
				starvationTracker = 0;
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
			this.breed();
		}
	}

	@Override
	public void breed() {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Doodlebug());
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
