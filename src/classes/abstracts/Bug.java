package classes.abstracts;

import classes.entity.Cell;
import classes.entity.Game;
import classes.entity.Null;
import classes.entity.Vector2;
import classes.util.Console;

public abstract class Bug extends Entity {
	public int idNum;
	Cell cell;

	public int movementCounter = 0;

	protected Bug(Game game) {
		super(game);

		// properties
		setProperty(Property.POSITION, new Vector2());
		setProperty(Property.ROTATION, 0);
		setProperty(Property.MOVEMENT_SPEED, 5);
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.ASSIGNED_CELL, new Null());
	}

	public boolean isEatable() {
		return getProperty(Property.IS_EATABLE, Boolean.class);
	}

	public abstract void move();

	public abstract void breed();
}
