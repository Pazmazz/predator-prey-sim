package classes.entity;

import classes.entity.Cell.CellType;

public class CellGrid {
	final private Vector2 size;
	final private Cell[][] grid;

	public CellGrid(Vector2 size) {
		this.size = size;
		this.grid = new Cell[size.X][size.Y];
	}

	public boolean isInBounds(Vector2 position) {
		return !(position.X < 0 || position.X >= size.X || position.Y < 0 || position.Y >= size.Y);
	}

	public boolean isInBounds(Cell cell) {
		return isInBounds(cell.getPosition());
	}

	public Cell getCell(Vector2 position) {
		if (!isInBounds(position)) {
			return new Cell(CellType.OUT_OF_BOUNDS, position);
		}

		return grid[position.X][position.Y];
	}

	public Cell setCell(Vector2 position) {
		Cell cell = new Cell(position);
		return setCell(position, cell);
	}

	public Cell setCell(Vector2 position, Cell cell) {
		if (!isInBounds(cell)) {
			cell.setCellType(CellType.OUT_OF_BOUNDS);
		}

		grid[position.X][position.Y] = cell;
		return cell;
	}

	public Vector2 getSize() {
		return size;
	}
}
