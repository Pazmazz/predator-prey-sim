package classes.entity;

import classes.abstracts.Bug;
import classes.util.Console;

public class Ant extends Bug {

	public Ant(Game game) {
		super(game);
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, true);
	}

	@Override
	public void move() {
		Cell[] adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				assignCell(adjCell);
				break;
			}
		}
		movementCounter++;

		if (movementCounter == 3) {
			movementCounter = 0;
			this.breed();
		}
	}

	@Override
	public void breed() {
		Cell[] adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Ant(game));
				break;
			}
		}
	}

	@Override
	public String toString() {
		return String.format(Console.withConsoleColors(
				"$text-green Ant$text-reset #%s"),
				idNum);
	}
}
