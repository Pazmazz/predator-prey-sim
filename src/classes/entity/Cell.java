/*
 * @Written: 3/31/2025
 * 
 * class Cell:
 * 
 * This class allows you to interface with the virtual
 * grid by adding occupants to a cell, checking grid
 * conditions, finding other relative cell positions,
 * etc.
 */
package src.classes.entity;

import src.classes.abstracts.CellOccupant;

public class Cell {
	private IntVector2 position;
	private CellType cellType;
	private CellVacancy cellVacancy;
	private CellOccupant cellOccupant;

	public enum CellType {
		OUT_OF_BOUNDS,
		NORMAL,
		GARBAGE_COLLECTED,
	}

	public enum CellVacancy {
		EMPTY,
		OCCUPIED,
	}

	public enum CellDirection {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
	}

	public Cell(IntVector2 position) {
		this(CellType.NORMAL, position);
	}

	public Cell(CellType cellType, IntVector2 position) {
		this.position = position;
		this.cellType = cellType;
		this.cellVacancy = CellVacancy.EMPTY;
	}

	public CellOccupant getOccupant() {
		return cellOccupant;
	}

	public void removeOccupant() {
		this.cellOccupant = null;
		cellVacancy = CellVacancy.EMPTY;
	}

	public void moveOccupantTo(Cell cell) {
		cell.setOccupant(getOccupant());
		removeOccupant();
	}

	public IntVector2 getPosition() {
		return position;
	}

	public CellType getType() {
		return cellType;
	}

	public CellVacancy getVacancy() {
		return cellVacancy;
	}

	public boolean isEmpty() {
		return cellVacancy == CellVacancy.EMPTY;
	}

	public boolean isOccupied() {
		return !isEmpty();
	}

	public boolean isOutOfBounds() {
		return cellType == CellType.OUT_OF_BOUNDS;
	}

	public boolean isInBounds() {
		return cellType == CellType.NORMAL;
	}

	public boolean isCollectable() {
		return isEmpty() || cellOccupant == null;
	}

	public CellDirection getDirectionRelativeTo(IntVector2 position) {
		Vector2 unitDirection = position.subtract(this.getPosition()).getUnit();

		if (unitDirection.X < 0)
			return CellDirection.RIGHT;
		if (unitDirection.X > 0)
			return CellDirection.LEFT;
		if (unitDirection.Y > 0)
			return CellDirection.TOP;

		return CellDirection.BOTTOM;
	}
	
	public CellDirection getDirectionRelativeTo(Cell cell) {
		return getDirectionRelativeTo(cell.getPosition());
	}

	public void setType(CellType cellType) {
		this.cellType = cellType;
	}

	public void setVacancy(CellVacancy cellVacancy) {
		this.cellVacancy = cellVacancy;
	}

	public void setOccupant(CellOccupant cellOccupant) {
		this.cellOccupant = cellOccupant;
		cellVacancy = CellVacancy.OCCUPIED;
	}

	@Override
	public String toString() {
		return "$text-green Cell$text-reset " + position.toString();
	}
}
