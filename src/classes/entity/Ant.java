package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Ant extends Bug<Ant> {

	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private GameSettings settings = game.getSettings();

	public Ant() {
		// properties
		this.setProperty(Property.IS_EATABLE, true);

		if (settings.getAntBreedingEnabled())
			this.getMovementMeter().onMaxValueReached.connect(e -> breed());
	}

	@Override
	public void move() {
		ArrayList<Cell> adjCells = gameGrid.getCellsAdjacentTo(getCell());
		Cell randCell = gameGrid.getRandomAvailableCellFrom(adjCells);

		if (randCell != null) {
			double angle = (randCell.getUnit2Center().subtract(getCell().getUnit2Center())).screenAngle()
					+ Math.PI * 3 / 2;
			setRotation(angle);
			assignCell(randCell);
		}
		this.getMovementMeter().increment();
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
