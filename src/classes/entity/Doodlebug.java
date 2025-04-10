package classes.entity;

import classes.abstracts.Bug;
import classes.util.Console;

public class Doodlebug extends Bug {

	int starvationTracker = 0;

	public Doodlebug(Game game) {
		super(game);
		idNum = (int) (Math.random() * 1000);
	}

	@Override
	public void move() {
		Cell currentCell = getProperty(
				Property.ASSIGNED_CELL,
				Cell.class);

		Cell[] adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(currentCell);

		for (Cell adjCell : adjCells) {
			if (adjCell.isOccupantEatable(currentCell)) {
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
		Cell currentCell = getProperty(
				Property.ASSIGNED_CELL,
				Cell.class);

		Cell[] adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(currentCell);

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Doodlebug(game));
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
}
