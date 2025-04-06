/*
 * @written 4/2/2025
 */
package classes.entity;

import exceptions.CellIsOccupiedException;
import exceptions.NoCellFoundException;

/**
 * A superclass representing the most general entity that is allowed to be
 * considered an occupant in a {@code Cell} object.
 *
 * <p>
 * Any subclass that extends {@code CellOccupant} is elligable to be set as an
 * occupant in a cell using {@code cell.setOccupant}.
 */
public class CellOccupant {

    private boolean isEatable = false;
    private Cell currentCell;

    public CellOccupant() {

    }

    public void setEatable(boolean eatable) {
        this.isEatable = eatable;
    }

    public boolean isEatable() {
        return this.isEatable;
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
     * @param targetCell the cell to assign the occupant
     * @param cellAggregatesOccupant whether the assigned cell aggregates the
     * occupant
     *
     * @throws CellIsOccupiedException if the assigned cell already has an
     * occupant
     */
    public void assignCell(Cell targetCell, boolean cellAggregatesOccupant) {
        if (targetCell.hasOccupant() && targetCell.getOccupant() != this) {
            throw new CellIsOccupiedException();
        }

        if (hasCell()) {
            removeCell();
        }

        if (cellAggregatesOccupant) {
            targetCell.setOccupant(this, false);
        }

        this.currentCell = targetCell;
    }

    /**
     * Overload: {@code assignCell}
     *
     * Calls the root method while passing {@code cellAggregatesOccupant = true}
     * as default
     *
     * @param targetCell the cell to assign the occupant
     */
    public void assignCell(Cell targetCell) {
        assignCell(targetCell, true);
    }

    public Cell getCell() {
        return this.currentCell;
    }

    public boolean hasCell() {
        return this.currentCell != null;
    }

    /**
     * Removes the occupant's cell from the occupant, and removes the occupant
     * from the current cell. Does not check if a current cell already exists;
     * this must be checked for using {@code hasCell} before attempting to use
     * this method.
     *
     * @throws NoCellFoundException if the calling this method when the occupant
     * has no cell
     */
    public void removeCell() {
        if (!hasCell()) {
            throw new NoCellFoundException();
        }
        this.currentCell.removeOccupant();
        this.currentCell = null;
    }
}
