/*
 * @written 4/2/2025
 */
package classes.abstracts;

import classes.entity.Cell;
import classes.entity.Game;
import classes.entity.Null;
import exceptions.CellIsOccupiedException;
import exceptions.NoCellFoundException;

/**
 * A superclass representing the most general entity that is allowed to be
 * considered an occupant in a {@code Cell} object.
 *
 * <p>
 * Any subclass that extends {@code Entity} is eligible to be set as an
 * occupant in a cell using {@code cell.setOccupant}.
 */
public abstract class Entity<T> extends Properties {

	public Game game;

	// Unused constructor for now
	public Entity(Game game) {
		this.game = game;

		// default properties
		// ...
	}

	/**
	 * Assigns the current occupant to a given cell. Each occupant can only be
	 * assigned one cell at a time, so it is not required to remove the
	 * occupant's cell before assigning them to a new cell. This is done
	 * internally.
	 *
	 * <p>
	 * If {@code assignCell} is called, and the occupant already belongs to a
	 * cell, then {@code cell.removeOccupant} is called on the current cell
	 * before assigning the occupant to the new cell, if the new cell is not
	 * already occupied.
	 *
	 * @param targetCell      the cell to assign the occupant
	 * @param withAggregation whether the assigned cell aggregates the
	 *                        occupant
	 *
	 * @throws CellIsOccupiedException if the assigned cell already has an
	 *                                 occupant
	 * @throws NoCellFoundException    if the {@code targetCell} is null
	 */
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

	/**
	 * Overload: {@code assignCell}
	 *
	 * Calls the root method while passing {@code withAggregation = true}
	 * as default
	 *
	 * @param targetCell the cell to assign the occupant
	 */
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

	/**
	 * Removes the occupant's cell from the occupant, and removes the occupant
	 * from the current cell. Does not check if a current cell already exists;
	 * this must be checked for using {@code hasCell} before attempting to use
	 * this method.
	 *
	 * @throws NoCellFoundException if the calling this method when the occupant
	 *                              has no cell
	 */
	public void removeFromCell() {
		removeFromCell(true);
	}
}
