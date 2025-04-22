package classes.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Time;
import interfaces.Property;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.ImageSet;
import classes.entity.ValueMeter.MeterResetType;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Doodlebug extends Bug<Doodlebug> {
	final private static HashMap<Doodlebug, Doodlebug> doodlebugs = new HashMap<>();
	final private Game game = Game.getInstance();

	private ValueMeter antsEatenMeter = new ValueMeter(
			0,
			Double.POSITIVE_INFINITY,
			0,
			MeterResetType.NONE);
	private ValueMeter hungerMeter = new ValueMeter(
			0,
			game.getSettings().getDoodlebugHungerLimit(),
			0,
			MeterResetType.ON_MAX);

	public Doodlebug() {
		this.setAvatar(ImageSet.BASE_DOODLEBUG);
		this.setMovementSpeed(game.getSettings().getDoodlebugMovementSpeed());
		this.getBreedingMeter().setMax(8);

		// event listeners
		hungerMeter.onMaxValueReached.connect(e -> this.removeFromCell());
		if (game.getSettings().getDoodlebugBreedingEnabled())
			this.getBreedingMeter().onMaxValueReached.connect(e -> this.breed());
	}

	public ValueMeter getHungerMeter() {
		return this.hungerMeter;
	}

	public ValueMeter getAntsEatenMeter() {
		return this.antsEatenMeter;
	}

	@Override
	public boolean move() {
		ValueMeter breedingMeter = this.getBreedingMeter();
		ValueMeter hungerMeter = this.getHungerMeter();

		boolean moved = breedingMeter.increment() == breedingMeter.getMax();
		ArrayList<Cell> adjCells = game.getGameGrid().randomizeCells(
				game.getGameGrid().getCellsAdjacentTo(this.getAssignedCell()));
		Cell movementCell = null;

		// try to eat
		for (Cell cell : adjCells) {
			if (cell.hasOccupant()) {
				if (cell.isOccupantEatable()) {
					cell.removeOccupant();
					this.assignCell(cell);
					hungerMeter.empty();
					this.getAntsEatenMeter().increment();
					return true;
				}
			} else if (movementCell == null) {
				movementCell = cell;
			}
		}

		// check for starvation
		if (hungerMeter.increment() == hungerMeter.getMax()) {
			this.removeFromCell();
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
	public String getTooltipString() {
		Cell cell = this.getAssignedCell();
		Unit2 unit = cell.getUnit2();
		return new StringBuilder(this.getNameWithId())
				.append("<span style='font-size:10px;color:white;'>")
				.append("<br>Time alive: <span style='color:#bf00ff;'>")
				.append(Time.formatTime(this.getTimeInSimulationInSeconds()))
				.append("</span><br>Ants eaten: <span style='color:#bf00ff;'>")
				.append(this.getAntsEatenMeter().getValue())
				.append("</span><br>Generation: <span style='color:#bf00ff;'>")
				.append(this.getGenerationMeter().getValue())
				.append("</span><br>Cell: <span style='color:#44D0FF;'>Cell&lt;")
				.append(unit.getX())
				.append(", ")
				.append(unit.getY())
				.append(">")
				.append("</span>")
				.toString();
	}
}
