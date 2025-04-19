package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Math2;
import classes.entity.CellGrid.Cell;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Doodlebug extends Bug<Doodlebug> {
	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private GameSettings settings = game.getSettings();

	public Doodlebug() {
		// properties
		ValueMeter hungerMeter = new ValueMeter(4, 0, 0);
		this.setProperty(Property.HUNGER_METER, hungerMeter);
		this.setProperty(Property.IS_EATABLE, false);

		ValueMeter movementMeter = this.getProperty(Property.MOVEMENT_METER, ValueMeter.class);
		movementMeter.setMax(8);

		// event listeners
		hungerMeter.onMaxValueReached.connect(e -> this.removeFromCell());
		if (settings.getDoodlebugBreedingEnabled())
			movementMeter.onMaxValueReached.connect(e -> this.breed());
	}

	public ValueMeter getHungerMeter() {
		return this.getProperty(Property.HUNGER_METER, ValueMeter.class);
	}

	@Override
	public void move() {
		this.getMovementMeter().increment();
		ArrayList<Cell> adjCells = this.gameGrid.randomizeCells(
				this.gameGrid.getCellsAdjacentTo(getCell()));
		Cell fallbackCell = null;

		for (Cell cell : adjCells) {
			if (cell.hasOccupant()) {
				if (cell.isOccupantEatable()) {
					cell.removeOccupant();
					this.assignCell(cell);
					this.getHungerMeter().empty();
					return;
				}
			} else if (fallbackCell == null) {
				fallbackCell = cell;
			}
		}

		if (fallbackCell != null)
			this.assignCell(fallbackCell);
		this.getHungerMeter().increment();
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
