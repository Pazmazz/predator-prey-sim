package classes.abstracts;

import classes.entity.Cell;
import classes.entity.CellOccupant;
import classes.entity.Game;

public abstract class Bug extends CellOccupant {
	public int idNum;
	Cell cell;
	public int movementCounter = 0;

	protected Bug(Game game) {
		super(game);
	}

	public abstract void move();

	public abstract void breed();
}
