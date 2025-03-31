/*
 * @Written: 3/30/2025
 * 
 * class CellGrid:
 * 
 * This class creates a virtual 2D grid using a HashMap, which
 * maps a position string to a Cell object.
 * 
 * Documentation can be found here: https://github.com/Pazmazz/predator-prey-sim/blob/will-feature-1/docs/CellGrid.md
 */
package classes.entity;

import classes.entity.Cell.CellType;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CellGrid {
	final private IntVector2 size;
	final private HashMap<String, Cell> virtualGrid = new HashMap<>();

	public CellGrid(IntVector2 size) {
		this.size = size;
	}

	public boolean isInBounds(IntVector2 position) {
		return !(position.X < 0 || position.X >= size.X || position.Y < 0 || position.Y >= size.Y);
	}

	public boolean isInBounds(Cell cell) {
		return isInBounds(cell.getPosition());
	}

	public Cell getCell(IntVector2 position) {
		Cell cell = virtualGrid.get(position.toString());

		if (cell != null) return cell;
		
		cell = new Cell(position);
		virtualGrid.put(position.toString(), cell);

		if (!isInBounds(position)) {
			cell.setType(CellType.OUT_OF_BOUNDS);
		}

		return cell;
	}
	
	public Cell collectCell(IntVector2 position) {
		Cell cell = virtualGrid.get(position.toString());

		if (cell == null) return null;

		if (cell.isCollectable()) {
			virtualGrid.remove(position.toString());
			cell.setType(CellType.GARBAGE_COLLECTED);
		}

		return cell;
	}

	public Cell collectCell(Cell cell) {
		return collectCell(cell.getPosition());
	}

	public void collectCells() {
		Iterator<Map.Entry<String, Cell>> gridIterator = virtualGrid.entrySet().iterator();
		int count = 0;

		while (gridIterator.hasNext()) {
			Map.Entry<String, Cell> cellEntry = gridIterator.next();
			Cell cell = cellEntry.getValue();

			if (cell.isCollectable()) {
				gridIterator.remove();
				count++;
			}
		}

		Console.debugPrint(
			DebugPriority.MEDIUM,
			"Freed $text-red %s $text-reset cells during garbage collection".formatted(count)
		);
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

	public Cell[] getCellsAdjacentTo(Cell cell) {
		return getCellsAdjacentTo(cell.getPosition());
	}

	public IntVector2 getSize() {
		return size;
	}
}
