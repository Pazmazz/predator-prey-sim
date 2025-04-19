package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Math2;
import classes.entity.CellGrid.Cell;

public class Doodlebug extends Bug<Doodlebug> {
	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();

	public Doodlebug() {

		// properties
		this.setProperty(Property.IS_EATABLE, false);
		this.setProperty(Property.HUNGER_METER, new ValueMeter(3));

		ValueMeter movementMeter = this.getProperty(Property.MOVEMENT_METER, ValueMeter.class);
		movementMeter.setMaxAndFill(8);
	}

	public ValueMeter getHungerMeter() {
		return this.getProperty(Property.HUNGER_METER, ValueMeter.class);
	}

	@Override
	public void move() {
		ArrayList<Cell> adjCells = this.gameGrid.getCellsAdjacentTo(getCell());
		Cell randOccupiedCell = this.gameGrid.getRandomOccupiedCellFrom(adjCells);

		if (randOccupiedCell != null && randOccupiedCell.isOccupantEatable()) {
			randOccupiedCell.removeOccupant();
			this.assignCell(randOccupiedCell);
			this.getHungerMeter().setValue(0);
		} else {
			Cell randAvailableCell = this.gameGrid.getRandomAvailableCellFrom(adjCells);
			if (randAvailableCell != null)
				this.assignCell(randAvailableCell);
			this.getHungerMeter().increment();
		}
		this.getMovementMeter().increment();
	}

	@Override
	public String toString() {
		return String.format(Console.filterConsoleColors(
				"$text-green Doodlebug$text-reset #%s"),
				this.getId());
	}

	@Override
	public String serialize() {
		return "Doodlebug{}";
	}

	@Override
	public Doodlebug newInstance() {
		return new Doodlebug();
	}
}
