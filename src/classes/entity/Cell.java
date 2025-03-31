package classes.entity;

import classes.abstracts.CellOccupant;

public class Cell {
	final private IntVector2 position;
	private CellType cellType;
	private CellVacancy cellVacancy;
	private CellOccupant occupant;
	private CellGrid grid;

	public enum CellType {
		OUT_OF_BOUNDS,
		NORMAL,
	}

	public enum CellVacancy {
		EMPTY,
		OCCUPIED,
		NULL,
	}

	public Cell(IntVector2 position) {
		this(CellType.NORMAL, position);
	}

	public Cell(CellType cellType, IntVector2 position) {
		this.position = position;
		this.cellType = cellType;
		this.cellVacancy = CellVacancy.NULL;
	}

	public CellOccupant getOccupant() {
		return occupant;
	}

	public void setOccupant(CellOccupant occupant) {
		occupant = occupant;
		cellVacancy = CellVacancy.OCCUPIED;
	}

	public void removeOccupant(CellOccupant occupant) {
		occupant = null;
		cellVacancy = CellVacancy.EMPTY;
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

	public CellGrid getGrid() {
		return this.grid;
	}

	// public Cell directionFrom(Cell cell) {

	// }

	public Cell destroy() {
		this.grid.destroyCell(this);
		this.grid = null;
		return this;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public void setCellVacancy(CellVacancy cellVacancy) {
		this.cellVacancy = cellVacancy;
	}

	public void setGrid(CellGrid grid) {
		this.grid = grid;
	}

	@Override
	public String toString() {
		return "$text-green Cell$text-reset " + position.toString();
	}
}
