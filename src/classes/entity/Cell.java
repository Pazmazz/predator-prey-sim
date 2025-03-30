package classes.entity;

import classes.abstracts.CellOccupant;

public class Cell {
	final private Vector2 position;
	private CellType cellType;
	private CellOccupant occupant;
	private CellGrid grid;

	public enum CellType {
		OUT_OF_BOUNDS,
		NORMAL,
	}

	public Cell(Vector2 position) {
		this(CellType.NORMAL, position);
	}

	public Cell(CellType cellType, Vector2 position) {
		this.position = position;
		this.cellType = cellType;
	}

	public CellOccupant getOccupant() {
		return occupant;
	}

	public void setOccupant(CellOccupant occupant) {
		occupant = occupant;
	}

	public Vector2 getPosition() {
		return position;
	}

	public CellType getCellType() {
		return cellType;
	}

	public Cell destroy() {
		this.grid.destroyCell(this);
		this.grid = null;
		return this;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public void setGrid(CellGrid grid) {
		this.grid = grid;
	}

	public CellGrid getGrid() {
		return this.grid;
	}

	@Override
	public String toString() {
		return "$text-green Cell$text-reset " + position.toString();
	}
}
