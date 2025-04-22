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
	final private ValueMeter generationMeter = new ValueMeter(0, Double.POSITIVE_INFINITY, 0);

	/*
	 * Properties:
	 */
	private double movementSpeed = 1;
	private boolean isEatable = false;
	final private ValueMeter breedingMeter = new ValueMeter(0, Double.POSITIVE_INFINITY, 0, MeterResetType.ON_MAX);

	protected Bug() {
		this.idNum = (int) (Math.random() * 1000);
	}

	// Property getters
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

	public void setEatable(boolean eatable) {
		this.isEatable = eatable;
	}

	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
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
