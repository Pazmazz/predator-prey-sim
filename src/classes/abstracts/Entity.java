/*
 * @written 4/2/2025
 */
package classes.abstracts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.ImageSet;
import classes.util.Console;
import exceptions.CellIsOccupiedException;
import exceptions.NoCellFoundException;
import interfaces.Property;
import interfaces.Serializable;

/**
 * A superclass representing the most general entity that is allowed to be
 * considered an occupant in a {@code Cell} object.
 *
 * <p>
 * Any subclass that extends {@code Entity} is eligible to be set as an
 * occupant in a cell using {@code cell.setOccupant}.
 */
public abstract class Entity<T extends Entity<T>> extends Properties {

	final private static HashMap<Entity<?>, Entity<?>> entities = new HashMap<>();

	public abstract ImageSet getAvatar();

	public abstract void setAvatar(ImageSet image);

	protected Cell assignedCell = null;
	protected String name = null;

	public enum EntityVariant {
		DOODLEBUG,
		ANT,
		TITAN,
	}

	// Unused constructor for now
	public Entity() {
		entities.put(this, this);
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
			if (this.hasAssignedCell())
				this.removeFromCell();

			targetCell.setOccupant(this, false);
		}

		this.assignedCell = targetCell;
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
		this.assignCell(targetCell, true);
	}

	public Cell getAssignedCell() {
		// return getProperty(Property.ASSIGNED_CELL, Cell.class);
		return this.assignedCell;
	}

	public boolean hasAssignedCell() {
		return this.assignedCell != null;
	}

	public void removeFromCell(boolean withAggregation) {
		if (!this.hasAssignedCell())
			throw new NoCellFoundException();

		if (withAggregation)
			this.assignedCell.removeOccupant(false);

		this.assignedCell = null;
		entities.remove(this);
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
		this.removeFromCell(true);
	}

	public static Collection<Entity<?>> getEntitiesReadOnly() {
		return entities.values();
	}
}
