package classes.entity;

import classes.entity.Cell.CellType;
import java.util.HashMap;

public class CellGrid {
	final private Vector2 size;
	final private Cell[][] grid;
	final private HashMap<String, Cell> metaGrid = new HashMap<>();

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
			if (metaGrid.get(position.toString()) != null) {
				return metaGrid.get(position.toString());
			}  else {
				Cell cell = new Cell(CellType.OUT_OF_BOUNDS, position);
				metaGrid.put(position.toString(), cell);
				return cell;
			}
		}

		return grid[position.X][position.Y];
	}

	public Cell setCell(Vector2 position) {
		Cell cell = new Cell(position);
		return setCell(position, cell);
	}

	public Cell setCell(Vector2 position, Cell cell) {
		if (!isInBounds(position)) {
			cell.setCellType(CellType.OUT_OF_BOUNDS);
			metaGrid.put(position.toString(), cell);
		} else {
			grid[position.X][position.Y] = cell;
		}

		cell.setGrid(this);
		return cell;
	}

	public Cell destroyCell(Vector2 position) {
		Cell cell = getCell(position);

		if (!isInBounds(position)) {
			metaGrid.remove(position.toString());
		} else {
			grid[position.X][position.Y] = null;
		}

		return cell;
	}

	public Cell destroyCell(Cell cell) {
		return destroyCell(cell.getPosition());
	}

	public Vector2 getSize() {
		return size;
	}
}
