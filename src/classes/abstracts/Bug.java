package classes.abstracts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import classes.entity.CellGrid;
import classes.entity.CellGrid.Cell;
import classes.entity.ValueMeter.MeterResetType;
import classes.entity.Doodlebug;
import classes.entity.EventSignal;
import classes.entity.Game;
import classes.entity.Timestamp;
import classes.entity.ValueMeter;
import classes.entity.Vector2;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Time;
import interfaces.Callback;

@SuppressWarnings("unused")
public abstract class Bug<T extends Bug<T>> extends Entity<T> {
	public abstract boolean move();

	public abstract T newInstance();

	final private static HashMap<Bug<?>, Bug<?>> bugs = new HashMap<>();
	final private Game game = Game.getInstance();
	final private int idNum;

	/*
	 * Metadata
	 */
	private Timestamp timeLastMoved = new Timestamp(0);
	final private ValueMeter timeInSimulationMeter = new ValueMeter(0, Integer.MAX_VALUE, 0);
	final private ValueMeter generationMeter = new ValueMeter(0, Integer.MAX_VALUE, 0);

	// TODO: this is currently for animating fade-ins, replace with a animation
	// service later
	private Timestamp timeCreated = new Timestamp();

	/*
	 * Properties:
	 */
	private double movementSpeed = 1;
	private boolean isEatable = false;
	final private ValueMeter breedingMeter = new ValueMeter(0, 3, 0, MeterResetType.ON_MAX);

	protected Bug() {
		this.idNum = (int) (Math.random() * 1000);
	}

	// Property getters
	public String getName() {
		return this.name;
	}

	public ValueMeter getBreedingMeter() {
		return this.breedingMeter;
	}

	public double getMovementSpeed() {
		return this.movementSpeed;
	}

	public boolean isEatable() {
		return this.isEatable;
	}

	// Metadata getters
	public ValueMeter getGenerationMeter() {
		return this.generationMeter;
	}

	public int getId() {
		return this.idNum;
	}

	public ValueMeter getTimeInSimulationMeter() {
		return this.timeInSimulationMeter;
	}

	public double getTimeInSimulationInSeconds() {
		return Time.nanoToSeconds((long) this.timeInSimulationMeter.getValue());
	}

	// Metadata setters
	public String getTooltipString() {
		return this.getNameWithId();
	}

	public void breed() {
		ArrayList<Cell> adjCells = game.getGameGrid().getCellsAdjacentTo(this.assignedCell);
		Cell randCell = game.getGameGrid().getRandomAvailableCellFrom(adjCells);

		if (randCell != null) {
			Bug<T> bug = newInstance();
			bug.assignCell(randCell);
			bug.getGenerationMeter().increment();
		}
	}

	public String getNameWithId() {
		return new StringBuilder(this.getName())
				.append(" (#")
				.append(this.getId())
				.append(")")
				.toString();
	}

	@Override
	public void removeFromCell() {
		super.removeFromCell();
		bugs.remove(this);
	}

	public Collection<Bug<?>> getBugsReadOnly() {
		return bugs.values();
	}
}
