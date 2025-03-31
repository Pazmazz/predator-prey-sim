package classes.entity;

import classes.abstracts.CellOccupant;
import classes.util.Console;

public class Cell {
	final private IntVector2 position;
	private CellType cellType;
	private CellVacancy cellVacancy;
	private CellOccupant cellOccupant;
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
		this.cellVacancy = CellVacancy.NULL;
	}

	public CellOccupant getOccupant() {
		return cellOccupant;
	}

	public void removeOccupant() {
		this.cellOccupant = null;
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

	public CellDirection getDirectionRelativeTo(Cell cell) {
		Vector2 unitDirection = cell.getPosition()
			.subtract(this.getPosition())
			.getUnit();

		if (unitDirection.X < 0) return CellDirection.RIGHT;
		if (unitDirection.X > 0) return CellDirection.LEFT;
		if (unitDirection.Y > 0) return CellDirection.TOP;
		
		return CellDirection.BOTTOM;
	}

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

	public void setOccupant(CellOccupant cellOccupant) {
		this.cellOccupant = cellOccupant;
		cellVacancy = CellVacancy.OCCUPIED;
	}

	@Override
	public String toString() {
		return "$text-green Cell$text-reset " + position.toString();
	}
}
