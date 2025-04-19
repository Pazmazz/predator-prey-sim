package classes.abstracts;

import java.util.ArrayList;

import classes.entity.CellGrid;
import classes.entity.CellGrid.Cell;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.entity.Doodlebug;
import classes.entity.EventSignal;
import classes.entity.Game;
import classes.entity.Null;
import classes.entity.ValueMeter;
import classes.entity.Vector2;
import classes.util.Console;
import classes.util.Math2;
import interfaces.Callback;

@SuppressWarnings("unused")
public abstract class Bug<T extends Bug<T>> extends Entity<T> {
	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private int idNum;

	protected Bug() {
		this.idNum = (int) (Math.random() * 1000);

		// properties
		this.setProperty(Property.POSITION, new Vector2());
		this.setProperty(Property.ROTATION, 0.0);
		this.setProperty(Property.MOVEMENT_SPEED, 5);
		this.setProperty(Property.IS_EATABLE, false);
		this.setProperty(Property.ASSIGNED_CELL, new Null());
		this.setProperty(Property.MOVEMENT_COOLDOWN, 1);
		this.setProperty(Property.VARIANT, getClass().getSimpleName());
		this.setProperty(Property.NAME, this.getClass().getSimpleName());
		this.setProperty(Property.HEALTH_METER, new ValueMeter(1));
		this.setProperty(Property.MOVEMENT_METER, new ValueMeter(3, 0, 0, RESET_TYPE.ON_MAX));
	}

	public ValueMeter getHealthMeter() {
		return this.getProperty(Property.HEALTH_METER, ValueMeter.class);
	}

	public ValueMeter getMovementMeter() {
		return this.getProperty(Property.MOVEMENT_METER, ValueMeter.class);
	}

	public double getRotation() {
		return this.getProperty(Property.ROTATION, Double.class);
	}

	public int getId() {
		return this.idNum;
	}

	public boolean isEatable() {
		return this.getProperty(Property.IS_EATABLE, Boolean.class);
	}

	public boolean isAlive() {
		return this.getHealthMeter().getValue() > 0;
	}

	public void setRotation(double rotation) {
		this.setProperty(Property.ROTATION, rotation);
	}

	public void breed() {
		ArrayList<Cell> adjCells = gameGrid.getCellsAdjacentTo(getCell());
		Cell randCell = gameGrid.getRandomAvailableCellFrom(adjCells);

		if (randCell != null) {
			newInstance().assignCell(randCell);
		}
	}

	public abstract void move();

	public abstract T newInstance();
}
