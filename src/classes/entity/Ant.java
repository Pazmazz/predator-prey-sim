package classes.entity;

import java.util.ArrayList;

import classes.abstracts.Bug;
import classes.util.Console;

public class Ant extends Bug<Ant> {
    public static int antCount = 0;
    public static int numOfAntBreeds = 0;

	public Ant(Game game) {
		super(game);
		idNum = (int) (Math.random() * 1000);

		// properties
		setProperty(Property.IS_EATABLE, true);
        antCount++;
	}

    public int getAntCount(){
        return antCount;
    }

	@Override
	public void move() {
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
			this.breed();
		}
	}

	@Override
	public void breed() {
		ArrayList<Cell> adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Ant(game));
				break;
			}
		}
        numOfAntBreeds++;
        game.setAntBreedCount();
	}

	@Override
	public String toString() {
		return String.format(Console.withConsoleColors(
				"$text-green Ant$text-reset #%s"),
				idNum);
	}
}
