package classes.entity;

import java.util.ArrayList;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Titan extends Bug<Titan> {
	public int healthBar = 2;
	public static int titanCount = 0;
    public static int numOfTitanBreeds = 0;
	public static int killCount = 0;


	private Entity<?> target;
	private Game game = Game.getInstance();

	public Titan() {
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Titan");
	}

	@Override
	public void move(Turn turn) {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.getOccupantVariant(getCell()) == "Doodlebug") {
				adjCell.removeOccupant();
				assignCell(adjCell);
                turn.setKillCount();
                killCount++;
				break;
			} else if (adjCell.isInBounds() && adjCell.isEmpty()) {
				assignCell(adjCell);
				break;
			}
		}
		
		movementCounter++;

		if (movementCounter == 8) {
			movementCounter = 0;
			this.breed(turn);
		}
	}

	@Override
	public void breed(Turn turn) {
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
}
