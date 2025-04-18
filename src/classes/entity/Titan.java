package classes.entity;

import java.util.ArrayList;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Titan extends Bug<Titan> {

	private Entity<?> target;
	private Game game = Game.getInstance();

	public Titan() {
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Titan");
	}

	@Override
	public void move() {
		CellGrid grid = game.getGameGrid();
		Cell cell = getCell();
		setTarget(grid.getCellWithNearestOccupant(cell).getOccupant());
		// Console.println("Titan target: ", titan.getTarget());

		ArrayList<Cell> pathCells = grid.getCellPath(
				cell.getUnit2Center(),
				getTarget().getProperty(Property.POSITION, Vector2.class));

		if (pathCells.size() > 0) {
			Cell c = pathCells.get(1);
			if (c.isAvailable()) {
				assignCell(c);
			}
		}
	}

	@Override
	public void breed() {
	}

	@Override
	public String toString() {
		return String.format(Console.withConsoleColors(
				"$text-green Ant$text-reset #%s"),
				idNum);
	}

	@Override
	public String serialize() {
		return "Titan{}";
	}

	public void setTarget(Entity<?> target) {
		this.target = target;
	}

	public Entity<?> getTarget() {
		return this.target;
	}

	@Override
	public String getAvatar() {
		// TODO Auto-generated method stub
		return "";
	}
}
