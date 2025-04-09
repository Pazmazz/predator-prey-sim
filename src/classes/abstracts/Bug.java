package classes.abstracts;

import classes.entity.Cell;
import classes.entity.CellOccupant;
import classes.entity.Game;

public abstract class Bug extends CellOccupant {
	public int idNum;
	Cell cell;

	public int movementCounter = 0;
	private double movementSpeed = 1;

	protected Bug(Game game) {
		super(game);
	}

	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public double getMovementSpeed() {
		return this.movementSpeed;
	}

	public abstract void move();

	public abstract void breed();
}
