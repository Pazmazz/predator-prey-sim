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

		if (cell != null)
			return cell;

		cell = new Cell(position);
		virtualGrid.put(position.toString(), cell);

		if (!isInBounds(position)) {
			cell.setType(CellType.OUT_OF_BOUNDS);
		}

		return cell;
	}

	public Cell collectCell(IntVector2 position) {
		Cell cell = virtualGrid.get(position.toString());

		if (cell == null)
			return null;

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
			if (cellEntry.getValue().isCollectable()) {
				gridIterator.remove();
				count++;
			}
		}

		Console.debugPrint(
				DebugPriority.MEDIUM,
				"Freed $text-red %s $text-reset cells during garbage collection".formatted(count));
	}

	public Cell getCellTopOf(IntVector2 position) {
		return getCell(new IntVector2(position.X, position.Y - 1));
	}

	public Cell getCellBottomOf(IntVector2 position) {
		return getCell(new IntVector2(position.X, position.Y + 1));
	}

	public Cell getCellLeftOf(IntVector2 position) {
		return getCell(new IntVector2(position.X - 1, position.Y));
	}

	public Cell getCellRightOf(IntVector2 position) {
		return getCell(new IntVector2(position.X + 1, position.Y));
	}

	public Cell getCellTopOf(Cell cell) {
		return getCellTopOf(cell.getPosition());
	}

	public Cell getCellBottomOf(Cell cell) {
		return getCellBottomOf(cell.getPosition());
	}

	public Cell getCellLeftOf(Cell cell) {
		return getCellLeftOf(cell.getPosition());
	}

	public Cell getCellRightOf(Cell cell) {
		return getCellRightOf(cell.getPosition());
	}

	public Cell[] getCellsAdjacentTo(IntVector2 position) {
		Cell[] cells = new Cell[4];
		cells[0] = getCellTopOf(position);
		cells[1] = getCellBottomOf(position);
		cells[2] = getCellLeftOf(position);
		cells[3] = getCellRightOf(position);

		return cells;
	}

	public Cell[] getCellsAdjacentTo(Cell cell) {
		return getCellsAdjacentTo(cell.getPosition());
	}

	// public Cell getNextCellPath(IntVector2 from, IntVector2 to) {
	// 	Vector2 centerFrom = from.getCenter();
	// 	Vector2 centerTo = to.getCenter();
	// 	Vector2 direction = centerTo.subtract(centerFrom);

	// 	double angle = Math.atan2(direction.Y, direction.X);
	// 	final double FOURTH_PI = Math.PI / 4;

	// 	if (angle % FOURTH_PI == 0) {
	// 		Console.println("Angle is on fault, choosing: ", angle > 0 ? "top" : "bottom");
	// 		return angle > 0 ? getCellTopOf(from) : getCellBottomOf(from);
	// 	}

	// 	return getCellBottomOf(from);
	// }

	// public Cell getNextCellPath(Cell fromCell, Cell toCell) {
	// 	return getNextCellPath(fromCell.getPosition(), toCell.getPosition());
	// }

	public IntVector2 getSize() {
		return size;
	}

	public void printCellsAdjacentTo(IntVector2 position) {
		Cell[] adjCells = getCellsAdjacentTo(position);

		for (Cell adjCell : adjCells) {
			adjCell.printInfo();
			adjCell.printInfoItem("Direction", adjCell.getDirectionRelativeTo(position).toString());
		}
	}

	public void printCellsAdjacentTo(Cell cell) {
		printCellsAdjacentTo(cell.getPosition());
	}
}
