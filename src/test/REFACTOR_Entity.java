/*
 * @written 4/2/2025
 */
package classes.abstracts;

import classes.entity.CellGrid.Cell;

import java.util.HashMap;

import classes.entity.Null;
import exceptions.CellIsOccupiedException;
import exceptions.NoCellFoundException;
import interfaces.Serializable;

/**
 * A superclass representing the most general entity that is allowed to be
 * considered an occupant in a {@code Cell} object.
 *
 * <p>
 * Any subclass that extends {@code Entity} is eligible to be set as an
 * occupant in a cell using {@code cell.setOccupant}.
 */
public abstract class REFACTOR_Entity<T> {

	final private HashMap<

	public void assignCell(Cell targetCell, boolean withAggregation) {
		if (targetCell == null)
			throw new NoCellFoundException();

		if (withAggregation) {
			if (hasCell())
				removeFromCell();

			targetCell.setOccupant(this, false);
		}

		setProperty(Property.ASSIGNED_CELL, targetCell);
	}

	public void assignCell(Cell targetCell) {
		assignCell(targetCell, true);
	}

	public Cell getCell() {
		return getProperty(Property.ASSIGNED_CELL, Cell.class);
	}

	public boolean hasCell() {
		return getCell() != null;
	}

	public void removeFromCell(boolean withAggregation) {
		if (!hasCell())
			throw new NoCellFoundException();

		if (withAggregation)
			getCell().removeOccupant(false);

		setProperty(Property.ASSIGNED_CELL, new Null());
	}

	public void removeFromCell() {
		removeFromCell(true);
	}
}
