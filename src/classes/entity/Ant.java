package classes.entity;

import classes.abstracts.Bug;

public class Ant extends Bug {

	public Ant(Game game) {
		super(game);
		idNum = (int) (Math.random() * 1000);
		this.setEatable(isEatable());
	}

	@Override
	public void move() {
		Cell[] adjCells = game
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
		Cell[] adjCells = game
				.getGameGrid()
				.getCellsAdjacentTo(getCell());

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Ant(game));
				break;
			}
		}
	}

	@Override
	public String toString() {
		return String.format(
				"$text-green Ant$text-reset #%s", idNum);
	}
}
