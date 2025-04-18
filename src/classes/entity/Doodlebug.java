package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Doodlebug extends Bug<Doodlebug> {

	private Game game = Game.getInstance();
	final private String avatar = "src/assets/doodlebug2.jpg";

	int starvationTracker = 0;

	public Doodlebug() {
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Doodlebug");
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	@Override
	public void move() {
		CellGrid grid = game.getGameGrid();
		ArrayList<Cell> adjCells = grid.getCellsAdjacentTo(getCell());
		Cell randOccupiedCell = grid.getRandomOccupiedCellFrom(adjCells);
		Cell randAvailableCell = grid.getRandomAvailableCellFrom(adjCells);

		if (randOccupiedCell != null) {
			// Console.println("Occupant eatable: ", adjCell.isOccupantEatable(getCell()));
			if (randOccupiedCell.isOccupantEatable()) {
				randOccupiedCell.removeOccupant();
				assignCell(randOccupiedCell);
				starvationTracker = -1;
			}
		} else if (randAvailableCell != null) {
			assignCell(randAvailableCell);
		}

		starvationTracker++;
		movementCounter++;

		if (movementCounter == 8) {
			movementCounter = 0;
			this.breed();
		}
		if (starvationTracker == 3) {
			removeFromCell();
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
