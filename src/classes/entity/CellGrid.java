package classes.entity;

import classes.entity.Cell.CellType;
import classes.entity.Cell.CellVacancy;
import java.util.HashMap;

public class CellGrid {
	final private IntVector2 size;
	final private Cell[][] grid;
	final private HashMap<String, Cell> metaGrid = new HashMap<>();

	public CellGrid(IntVector2 size) {
		this.size = size;
		this.grid = new Cell[size.X][size.Y];
	}

	public boolean isInBounds(IntVector2 position) {
		return !(position.X < 0 || position.X >= size.X || position.Y < 0 || position.Y >= size.Y);
	}

	public boolean isInBounds(Cell cell) {
		return isInBounds(cell.getPosition());
	}

	public Cell getCell(IntVector2 position) {
		if (!isInBounds(position)) {
			if (metaGrid.get(position.toString()) != null) {
				return metaGrid.get(position.toString());
			} else {
				Cell cell = new Cell(CellType.OUT_OF_BOUNDS, position);
				metaGrid.put(position.toString(), cell);
				return cell;
			}
		}

		Cell cell = grid[position.X][position.Y];
		if (cell == null) cell = new Cell(position);
		return cell;
	}

	public Cell setCell(IntVector2 position) {
		Cell cell = new Cell(position);
		return setCell(position, cell);
	}

	public Cell setCell(IntVector2 position, Cell cell) {
		if (!isInBounds(position)) {
			cell.setCellType(CellType.OUT_OF_BOUNDS);
			metaGrid.put(position.toString(), cell);
		} else {
			grid[position.X][position.Y] = cell;
			cell.setCellVacancy(CellVacancy.EMPTY);
		}

		cell.setGrid(this);
		return cell;
	}

	public Cell destroyCell(IntVector2 position) {
		Cell cell = getCell(position);

		if (!isInBounds(position)) {
			metaGrid.remove(position.toString());
		} else {
			grid[position.X][position.Y] = null;
			cell.setCellVacancy(CellVacancy.NULL);
		}

		return cell;
	}

	public Cell destroyCell(Cell cell) {
		return destroyCell(cell.getPosition());
	}

	public Cell getTopCellTo(IntVector2 position) {
		return getCell(new IntVector2(position.X - 1, position.Y));
	}

	public Cell getBottomCellTo(IntVector2 position) {
		return getCell(new IntVector2(position.X + 1, position.Y));
	}

	public Cell getLeftCellTo(IntVector2 position) {
		return getCell(new IntVector2(position.X, position.Y - 1));
	}

	public Cell getRightCellTo(IntVector2 position) {
		return getCell(new IntVector2(position.X, position.Y + 1));
	}

	public Cell getTopCellTo(Cell cell) {
		return getTopCellTo(cell.getPosition());
	}

	public Cell getBottomCellTo(Cell cell) {
		return getBottomCellTo(cell.getPosition());
	}

	public Cell getLeftCellTo(Cell cell) {
		return getLeftCellTo(cell.getPosition());
	}

	public Cell getRightCellTo(Cell cell) {
		return getRightCellTo(cell.getPosition());
	}

	public Cell[] getCellsAdjacentTo(IntVector2 position) {
		Cell[] cells = new Cell[4];

		cells[0] = getTopCellTo(position);
		cells[1] = getBottomCellTo(position);
		cells[2] = getLeftCellTo(position);
		cells[3] = getRightCellTo(position);

		return cells;
	}

	public IntVector2 getSize() {
		return size;
	}
}
