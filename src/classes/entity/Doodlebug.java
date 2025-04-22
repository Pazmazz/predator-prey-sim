package classes.entity;

import java.io.File;
import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Time;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.ImageSet;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Doodlebug extends Bug<Doodlebug> {
	public ImageSet avatar = ImageSet.BASE_DOODLEBUG;

	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private GameSettings settings = game.getSettings();

	public Doodlebug() {
		// properties
		this.setProperty(Property.IS_EATABLE, false);
		this.setProperty(Property.MOVEMENT_COOLDOWN, settings.getDoodlebugMovementCooldown());
		this.setProperty(Property.ANTS_EATEN, new ValueMeter(0, 0).removeLimit());

		ValueMeter breedingMeter = this.getProperty(Property.MOVEMENT_METER, ValueMeter.class);
		breedingMeter.setMax(8);

		ValueMeter hungerMeter = new ValueMeter(settings.getDoodlebugHungerLimit(), 0, 0);
		this.setProperty(Property.HUNGER_METER, hungerMeter);

		// event listeners
		hungerMeter.onMaxValueReached.connect(e -> this.removeFromCell());
		if (settings.getDoodlebugBreedingEnabled())
			breedingMeter.onMaxValueReached.connect(e -> this.breed());
	}

	public ValueMeter getHungerMeter() {
		return this.getProperty(Property.HUNGER_METER, ValueMeter.class);
	}

	public ValueMeter getAntsEatenMeter() {
		return this.getProperty(Property.ANTS_EATEN, ValueMeter.class);
	}

	@Override
	public boolean move() {
		boolean moved = this.getBreedingMeter().increment() == this.getBreedingMeter().getMax();
		ArrayList<Cell> adjCells = this.gameGrid.randomizeCells(
				this.gameGrid.getCellsAdjacentTo(getCell()));
		Cell movementCell = null;

		// try to eat
		for (Cell cell : adjCells) {
			if (cell.hasOccupant()) {
				if (cell.isOccupantEatable()) {
					cell.removeOccupant();
					this.assignCell(cell);
					this.getHungerMeter().empty();
					this.getAntsEatenMeter().increment();
					return true;
				}
			} else if (movementCell == null) {
				movementCell = cell;
			}
		}

		// check for starvation
		if (this.getHungerMeter().increment() == this.getHungerMeter().getMax()) {
			return true;
		}

		// try to move
		if (movementCell != null) {
			this.assignCell(movementCell);
			return true;
		}
		return moved;
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
	public ImageSet getAvatar() {
		return this.avatar;
	}

	@Override
	public void setAvatar(ImageSet avatar) {
		this.avatar = avatar;
	}

	@Override
	public String getTooltipString() {
		Cell cell = this.getCell();
		Unit2 unit = cell.getUnit2();
		return new StringBuilder(this.getNameWithId())
				.append("<span style='font-size:10px;color:white;'>")
				.append("<br>Time alive: <span style='color:#bf00ff;'>")
				.append(Time.formatTime(this.getTimeInSimulationInSeconds()))
				.append("</span><br>Ants eaten: <span style='color:#bf00ff;'>")
				.append(this.getAntsEatenMeter().getValue())
				.append("</span><br>Generation: <span style='color:#bf00ff;'>")
				.append(this.getGeneration())
				.append("</span><br>Cell: <span style='color:#44D0FF;'>Cell&lt;")
				.append(unit.getX())
				.append(", ")
				.append(unit.getY())
				.append(">")
				.append("</span>")
				.toString();
	}
}
