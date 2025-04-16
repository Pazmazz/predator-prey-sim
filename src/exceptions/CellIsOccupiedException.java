package exceptions;

import classes.abstracts.Entity;
import classes.entity.CellGrid.Cell;

public class CellIsOccupiedException extends RuntimeException {

	public CellIsOccupiedException(Cell thisCell, Entity<?> targetOccupant) {
		super(String.format(
				"CELL %s ALREADY OCCUPIED - Cannot assign Entity \"%s\" to cell because \"%s\" is currently occupying it",
				thisCell,
				targetOccupant,
				thisCell.getOccupant()));
	}
}
