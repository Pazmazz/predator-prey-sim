package classes.entity;

import java.io.File;
import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Math2;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.IMAGE;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Doodlebug extends Bug<Doodlebug> {
	public IMAGE avatar = IMAGE.BASE_DOODLEBUG;

	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private GameSettings settings = game.getSettings();

	public Doodlebug() {
		// properties
		this.setProperty(Property.IS_EATABLE, false);
		this.setProperty(Property.MOVEMENT_COOLDOWN, settings.getDoodlebugMovementCooldown());
		this.setProperty(Property.ANTS_EATEN, new ValueMeter(0, 0).removeLimit());

		ValueMeter movementMeter = this.getProperty(Property.MOVEMENT_METER, ValueMeter.class);
		movementMeter.setMax(8);

		ValueMeter hungerMeter = new ValueMeter(settings.getDoodlebugHungerLimit(), 0, 0);
		this.setProperty(Property.HUNGER_METER, hungerMeter);

		// event listeners
		hungerMeter.onMaxValueReached.connect(e -> this.removeFromCell());
		if (settings.getDoodlebugBreedingEnabled())
			movementMeter.onMaxValueReached.connect(e -> this.breed());
	}

	public ValueMeter getHungerMeter() {
		return this.getProperty(Property.HUNGER_METER, ValueMeter.class);
	}

	public ValueMeter getAntsEatenMeter() {
		return this.getProperty(Property.ANTS_EATEN, ValueMeter.class);
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
					this.getAntsEatenMeter().increment();
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

	@Override
	public IMAGE getAvatar() {
		return this.avatar;
	}

	@Override
	public void setAvatar(IMAGE avatar) {
		this.avatar = avatar;
	}

	@Override
	public String getTooltipString() {
		return new StringBuilder(this.getNameWithId())
				.append("<br>test")
				.toString();
	}
}
