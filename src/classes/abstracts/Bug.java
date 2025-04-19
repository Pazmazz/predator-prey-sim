package classes.abstracts;

import classes.entity.CellGrid.Cell;
import classes.entity.Null;
import classes.entity.Vector2;
import classes.util.Console;

public abstract class Bug<T> extends Entity<T> {
	public int idNum;
	Cell cell;
	private double rotation = 0;

	public int movementCounter = 0;

	protected Bug() {
		// properties
		setProperty(Property.POSITION, new Vector2());
		setProperty(Property.ROTATION, 0);
		setProperty(Property.MOVEMENT_SPEED, 5);
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.ASSIGNED_CELL, new Null());
		setProperty(Property.MOVEMENT_COOLDOWN, 1);
		setProperty(Property.NAME, "Bug");
	}

	public boolean isEatable() {
		return getProperty(Property.IS_EATABLE, Boolean.class);
	}

	public abstract void move();

	public abstract void breed();

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
