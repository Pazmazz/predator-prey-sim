/*
 * @written 4/2/2025
 */
package classes.entity;

import exceptions.CellIsOccupiedException;

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
     * Assigns the current entity to a given cell. Each entity can only be
     * assigned one cell at a time, so it is not required to remove the entity's
     * cell before assigning them to a new cell.
     *
     * <p>
     * If {@code assignCell} is called, and the entity already belongs to a
     * cell, then {@code cell.removeOccupant} is called on the old.
     *
     * @param targetCell
     * @param updateCell
     *
     * @throws CellIsOccupiedException if the assigned cell already has an
     * occupant
     */
    public void assignCell(Cell targetCell, boolean cellAggregatesOccupant) {
        if (targetCell.isOccupied() && targetCell.getOccupant() != this) {
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

    public void assignCell(Cell targetCell) {
        assignCell(targetCell, true);
    }

    public Cell getCell() {
        return this.currentCell;
    }

    public boolean hasCell() {
        return this.currentCell != null;
    }

    public void removeCell() {
        this.currentCell.removeOccupant();
        this.currentCell = null;
    }
}
