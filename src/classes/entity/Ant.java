package classes.entity;

import classes.abstracts.Bug;

public class Ant extends Bug {

	public Ant() {
		idNum = (int) (Math.random() * 1000);
		this.setEatable(isEatable());
	}

	@Override
	public void move(Cell currentCell, CellGrid grid) {
		Cell[] adjCells = grid.getCellsAdjacentTo(currentCell);

		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				// currentCell.moveOccupantTo(adjCell);
				// this.currentCell = adjCell;
				assignCell(adjCell);
				break;
			}
		}
		movementCounter++;

		if (movementCounter == 3) {
			movementCounter = 0;
			this.breed(adjCells);
		}
	}

	@Override
	public void breed(Cell[] adjCells) {
		for (Cell adjCell : adjCells) {
			if (adjCell.isInBounds() && adjCell.isEmpty()) {
				adjCell.setOccupant(new Ant());
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "Ant";
	}
}
