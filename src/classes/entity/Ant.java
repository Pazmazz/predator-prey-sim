package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

@SuppressWarnings("unused")
public class Ant extends Bug<Ant> {

	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();

	public Ant() {
		this.setMovementCountLimit(3);
		this.onMovementLimitReached.connect(e -> breed());
		// properties
		setProperty(Property.IS_EATABLE, true);
	}

	@Override
	public void move() {
		ArrayList<Cell> adjCells = gameGrid.getCellsAdjacentTo(getCell());
		Cell randCell = gameGrid.getRandomAvailableCellFrom(adjCells);

		if (randCell != null) {
			double angle = (randCell.getUnit2Center().subtract(getCell().getUnit2Center())).screenAngle();
			setRotation(angle);
			assignCell(randCell);
		}
		incrementMovement();
	}

	@Override
	public String toString() {
		return String.format(Console.filterConsoleColors(
				"$text-green Ant$text-reset #%s"),
				getId());
	}

	@Override
	public String serialize() {
		return "Ant{}";
	}

	@Override
	public Ant newInstance() {
		return new Ant();
	}
}
