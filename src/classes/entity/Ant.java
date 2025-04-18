package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Ant extends Bug<Ant> {

	private Game game = Game.getInstance();
	private final String avatar = "src/assets/ant2.jpg";

	public Ant() {
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, true);
		setProperty(Property.VARIANT, "Ant");
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	@Override
	public void move() {
		CellGrid grid = game.getGameGrid();
		ArrayList<Cell> adjCells = grid.getCellsAdjacentTo(getCell());
		Cell randCell = grid.getRandomAvailableCellFrom(adjCells);
		if (randCell != null) {
			double angle = (randCell.getUnit2Center().subtract(getCell().getUnit2Center())).screenAngle();
			setRotation(angle);
			assignCell(randCell);
		}

		movementCounter++;
		if (movementCounter == 3) {
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
				adjCell.setOccupant(new Ant());
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

	@Override
	public String serialize() {
		return "Ant{}";
	}
}
