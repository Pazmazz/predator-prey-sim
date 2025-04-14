package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Ant extends Bug<Ant> {
    public static int antCount = 0;
    public static int numOfAntBreeds = 0;

	private Game game = Game.getInstance();

	public Ant() {
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, true);
		setProperty(Property.VARIANT, "Ant");
        
		antCount++;
	}

    public int getAntCount(){
        return antCount;
    }

	@Override
	public void move(Turn turn) {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				assignCell(adjCell);
				break;
			}
		}
		movementCounter++;

		if (movementCounter == 3) {
			movementCounter = 0;
			this.breed(turn);
		}
	}

	@Override
	public void breed(Turn turn) {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Ant());
				break;
			}
		}
        numOfAntBreeds++;
        turn.setAntBreedCount();
	}

	@Override
	public String toString() {
		return String.format(Console.withConsoleColors(
				"$text-green Ant$text-reset #%s"),
				idNum);
	}

	@Override
	public String serialize() {
		return "Ant{}";
	}
}
