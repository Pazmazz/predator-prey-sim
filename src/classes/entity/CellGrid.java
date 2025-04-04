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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CellGrid {
	final private Unit2 size;
	final private HashMap<String, Cell> virtualGrid = new HashMap<>();

	public CellGrid(Unit2 size) {
		this.size = size;
	}

	/*
	 * isInBounds()
	 * 
	 * Returns true if a given cell unit exists within the
	 * bounds of the specified grid size.
	 */
	public boolean isInBounds(Unit2 unit) {
		return !(unit.getX() <= 0
				|| unit.getX() > this.size.getX()
				|| unit.getY() <= 0
				|| unit.getY() > this.size.getY());
	}

	/*
	 * @overload: isInBounds()
	 * 
	 * Returns true if an actual coordinate point exists
	 * within the bounds of the virtual grid
	 */
	public boolean isInBounds(Vector2 position) {
		return !(position.getX() < 0
				|| position.getX() > this.size.getX()
				|| position.getY() < 0
				|| position.getY() > this.size.getY());
	}

	/*
	 * @overload: isInBounds()
	 * 
	 * Returns true if a cell object exists within the
	 * bounds of the virtual grid
	 */
	public boolean isInBounds(Cell cell) {
		return isInBounds(cell.getUnit2());
	}

	public boolean outOfBounds(Unit2 unit) {
		return !isInBounds(unit);
	}

	public boolean outOfBounds(Vector2 position) {
		return !isInBounds(position);
	}

	public boolean outOfBounds(Cell cell) {
		return !isInBounds(cell);
	}

	public Cell getCell(Unit2 unit) {
		Cell cell = virtualGrid.get(unit.toString());

		if (cell != null)
			return cell;

		cell = new Cell(unit);
		virtualGrid.put(unit.toString(), cell);

		if (outOfBounds(unit)) {
			cell.setType(CellType.OUT_OF_BOUNDS);
		}

		return cell;
	}

	public Cell getCell(Vector2 position) {
		return getCell(new Unit2((int) Math.ceil(position.getX()), (int) Math.ceil(position.getY())));
	}

	public Cell collectCell(Unit2 unit) {
		Cell cell = virtualGrid.get(unit.toString());

		if (cell == null)
			return null;

		if (cell.isCollectable()) {
			virtualGrid.remove(unit.toString());
			cell.setType(CellType.GARBAGE_COLLECTED);
		}

		return cell;
	}

	public Cell collectCell(Cell cell) {
		return collectCell(cell.getUnit2());
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

	public Cell getCellTopOf(Unit2 unit) {
		return getCell(unit.add(new Unit2(0, -1)));
	}

	public Cell getCellBottomOf(Unit2 unit) {
		return getCell(unit.add(new Unit2(0, 1)));
	}

	public Cell getCellLeftOf(Unit2 unit) {
		return getCell(unit.add(new Unit2(-1, 0)));
	}

	public Cell getCellRightOf(Unit2 unit) {
		return getCell(unit.add(new Unit2(1, 0)));
	}

	public Cell getCellTopOf(Cell cell) {
		return getCellTopOf(cell.getUnit2());
	}

	public Cell getCellBottomOf(Cell cell) {
		return getCellBottomOf(cell.getUnit2());
	}

	public Cell getCellLeftOf(Cell cell) {
		return getCellLeftOf(cell.getUnit2());
	}

	public Cell getCellRightOf(Cell cell) {
		return getCellRightOf(cell.getUnit2());
	}

	public Cell getCellTopOf(Vector2 position) {
		return getCellTopOf(getCell(position));
	}

	public Cell getCellBottomOf(Vector2 position) {
		return getCellBottomOf(getCell(position));
	}

	public Cell getCellLeftOf(Vector2 position) {
		return getCellLeftOf(getCell(position));
	}

	public Cell getCellRightOf(Vector2 position) {
		return getCellRightOf(getCell(position));
	}

	public Cell[] getCellsAdjacentTo(Unit2 unit) {
		Cell[] cells = new Cell[4];
		cells[0] = getCellTopOf(unit);
		cells[1] = getCellBottomOf(unit);
		cells[2] = getCellLeftOf(unit);
		cells[3] = getCellRightOf(unit);
		return cells;
	}

	public Cell[] getCellsAdjacentTo(Cell cell) {
		return getCellsAdjacentTo(cell.getUnit2());
	}

	public Unit2 getSize() {
		return size;
	}

	public void printCellsAdjacentTo(Unit2 unit) {
		Cell[] adjCells = getCellsAdjacentTo(unit);

		for (Cell adjCell : adjCells) {
			adjCell.printInfo();
		}
	}

	public void printCellsAdjacentTo(Cell cell) {
		printCellsAdjacentTo(cell.getUnit2());
	}

	/*
	 * getNextGridIntercept()
	 * 
	 * Given a start point and an end point, find the next point
	 * that intersects either grid lines and return that point
	 * as a Vector2. If the start point and end point are within
	 * the same cell, then the method returns null.
	 * 
	 * Example:
	 * ```
	 * CellGrid grid = new CellGrid(new Unit2(10, 10));
	 * 
	 * Vector2 p0 = new Vector2(1.5, 1.5);
	 * Vector2 p1 = new Vector2(1.75, 2.5);
	 * 
	 * Console.println(grid.getNextGridIntercept(p0, p1));
	 * ```
	 * > Output: Vector2<1.625, 2.0>
	 */
	public Vector2 getGridInterceptPoint(Vector2 start, Vector2 end) {
		Vector2 unit = end.subtract(start).unit();

		Double snapXDown 	= Math.floor(start.getX());
		Double snapXUp 		= Math.ceil(start.getX());
		Double snapYDown 	= Math.floor(start.getY());
		Double snapYUp 		= Math.ceil(start.getY());

		Double maxX, maxY;
		Double y;

		boolean pos_x = unit.getX() > 0;
		boolean neg_x = unit.getX() < 0;
		boolean pos_y = unit.getY() >= 0;
		boolean neg_y = unit.getY() <= 0;

		/*
		 * FOR ALL X != 0 CASE:
		 * 
		 *  compute the maximum X and Y values that
		 * are potential domains for a grid-line intersection
		 */
		if (pos_x || neg_x) {
			maxX = pos_x
				? snapXUp
				: snapXDown;
				
			maxY = (pos_x && neg_y) || (neg_x && neg_y)
				? snapYDown
				: snapYUp;

			/*
			 * If either maxX = start.X or maxY = start.Y, then 
			 * the start point is already on a grid line, so 
			 * bump up the value to the next grid line intersection
			 */
			if (maxX == start.getX())
				maxX += pos_x ? 1 : -1;

			if (maxY == start.getY())
				maxY += pos_y ? 1 : -1;

			/* Evaluate the function with respect to X, solving for Y */
			y = start.solveFunctionOfXForY(end, maxX);

			/*
			 * Y GRID-LINE CASE:
			 * 
			 * If Y could not be solved, or it's value is not on the
			 * same y-level as the start point, then the grid intersection
			 * occurs on a Y grid-line axis. So, solve the equation
			 * for X and return the result
			 */
			if (y == null || (Math.floor(y) != snapYDown) && (Math.ceil(y) != snapYUp)) {
				y = start.solveFunctionOfXForX(end, maxY);

				/*
				 * If Y still doesn't exist, then there is no grid-line intersection
				 * (both start and end points are in the same cell)
				 */
				if (y == null) return null;
				return new Vector2(y, maxY);
			} 
			
			/*
			 * X GRID-LINE CASE:
			 * 
			 * If Y was solved and is the same integer value as the Y value
			 * of the starting point, then the intersection occurs on the
			 * X grid-line axis.
			 */
			return new Vector2(maxX, y);
		}

		/*
		 * X = 0 CASE:
		 * 
		 * If X = 0, then the line is not a function and the equation
		 * cannot be solved. In such a case, the grid-line intersection
		 * occurs directly above or below the starting point, depending
		 * on `pos_y`
		 */
		return new Vector2(
			start.getX(),
			pos_y ? Math.ceil(start.getY()) : Math.floor(start.getY())
		);
	}

	public ArrayList<Cell> getCellPath(Vector2 from, Vector2 to) {
		CellPathCollection path = new CellPathCollection(from, to);
		Iterator<Cell> pathIterator = path.iterator();

		while (pathIterator.hasNext()) {
			pathIterator.next();
		}

		return path.getCellPath();
	}

	public Iterator<Cell> getCellPathIterator(Vector2 from, Vector2 to) {
		return new CellPathCollection(from, to).iterator();
	}
	
	private class CellPathCollection {
		private ArrayList<Cell> cellPath = new ArrayList<>();
		private Vector2 from;
		private Vector2 to;

		public CellPathCollection(Vector2 from, Vector2 to) {
			this.from = from;
			this.to = to;
		}

		public Iterator<Cell> iterator() {
			return new CellPathIterator();
		}

		public ArrayList<Cell> getCellPath() {
			return cellPath;
		}
		
		private class CellPathIterator implements Iterator<Cell> {
			@Override
			public boolean hasNext() {
				return from != null;
			}

			@Override
			public Cell next() {
				if (!hasNext()) return null;
				from = getGridInterceptPoint(from, to);
				Console.println("NEXT LINE: ", from);

				return getCell(from);
			}
		}
	}
}
