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
import classes.entity.CellGrid.CellGridAxis;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import classes.util.Math2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class CellGrid {
	final private Unit2 size;
	final private HashMap<String, Cell> virtualGrid = new HashMap<>();

	public enum CellGridAxis {
		X,
		Y,
		XY,
		X_GRID,
		Y_GRID,
		XY_GRID,
		NONE,
		ENDPOINT,
	}

	public enum GridDirection {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

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

	/*
	 * outOfBounds()
	 * 
	 * Returns the negation of isInBounds()
	 */
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
		Cell cell = this.virtualGrid.get(unit.toString());

		if (cell != null)
			return cell;

		cell = new Cell(unit);
		this.virtualGrid.put(unit.toString(), cell);

		if (outOfBounds(unit))
			cell.setType(CellType.OUT_OF_BOUNDS);

		return cell;
	}

	public Cell getCell(Vector2 position) {
		Vector2 quadrant = position.signedUnit();
		Vector2 snapPos = position.floor();

		int snapX = (int) snapPos.getX();
		int snapY = (int) snapPos.getY();

		int x, y;

		boolean posX = quadrant.getX() > 0;
		boolean negX = quadrant.getX() < 0;
		boolean posY = quadrant.getY() >= 0;
		boolean negY = quadrant.getY() <= 0;

		if (posX && posY) {
			y = snapY + 1;
			x = snapX + 1;

		} else if (negX && posY) {
			y = snapY + 1;
			x = snapX;

		} else if (negX && negY) {
			y = snapY;
			x = snapX;

		} else {
			y = snapY;
			x = snapX + 1;
		}

		return getCell(new Unit2(x, y));
	}

	public Cell getCell(Vector2 segment0, Vector2 segment1) {
		return getCell(segment0.midpoint(segment1));
	}

	public Cell collectCell(Unit2 unit) {
		Cell cell = this.virtualGrid.get(unit.toString());

		if (cell == null)
			return null;

		if (cell.isCollectable()) {
			this.virtualGrid.remove(unit.toString());
			cell.setType(CellType.GARBAGE_COLLECTED);
		}

		return cell;
	}

	public Cell collectCell(Cell cell) {
		return collectCell(cell.getUnit2());
	}

	public void collectCells() {
		Iterator<Map.Entry<String, Cell>> gridIterator = this.virtualGrid.entrySet().iterator();
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

		for (Cell adjCell : adjCells)
			adjCell.printInfo();
	}

	public void printCellsAdjacentTo(Cell cell) {
		printCellsAdjacentTo(cell.getUnit2());
	}

	/*
	 * getGridIntercept()
	 *
	 */
	public GridIntercept getGridIntercept(Vector2 start, Vector2 end) {
		if (start.equals(end))
			return new GridIntercept()
					.setAxisOfIntersection(CellGridAxis.ENDPOINT);

		Vector2 unit = end.subtract(start).unit();
		Vector2 signedUnit = unit.signedUnit();

		GridIntercept interceptResult = new GridIntercept();
		interceptResult.setDirection(signedUnit);

		double startX = start.getX();
		double startY = start.getY();
		double endX 	= end.getX();
		double endY 	= end.getY();

		boolean posX 	= unit.getX() > 0;
		boolean posY 	= unit.getY() >= 0;

		double limitX = posX
			? Math.ceil(startX)
			: Math.floor(startX);

		double limitY = posY
			? Math.ceil(startY)
			: Math.floor(startY);

		if (startX == limitX)
			limitX += signedUnit.getX();

		if (startY == limitY)
			limitY += signedUnit.getY();
			
		// Solve parametric equations for t
		double ty = (limitY - startY) / (endY - startY);
		double tx = (limitX - startX) / (endX - startX);

		Vector2 pointOfIntersection;
		CellGridAxis axisOfIntersection;

		// No intercepts (points are inside the cell or reached endpoint)
		if (tx >= 1 && ty >= 1) {
			pointOfIntersection = end;
			axisOfIntersection = CellGridAxis.NONE;

		// X-intercept
		} else if (tx < ty) {
			axisOfIntersection = CellGridAxis.X_GRID;
			pointOfIntersection = new Vector2(
					limitX,
					Math2.lerp(tx, startY, endY));

		// Y-intercept
		} else if (ty < tx) {
			axisOfIntersection = CellGridAxis.Y_GRID;
			pointOfIntersection = new Vector2(
					Math2.lerp(ty, startX, endX),
					limitY);
			
		// X- and Y-intercept
		} else {
			axisOfIntersection = CellGridAxis.XY_GRID;
			pointOfIntersection = new Vector2(
					limitX,
					limitY);
		}
		
		return interceptResult
				.setPointOfIntersection(pointOfIntersection)
				.setCell(getCell(start, pointOfIntersection))
				.setAxisOfIntersection(axisOfIntersection);
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
		final private ArrayList<Cell> cellPath = new ArrayList<>();
		final private Vector2 from;
		final private Vector2 to;

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
			private GridIntercept nextGridIntercept = getGridIntercept(from, to);

			@Override
			public boolean hasNext() {
				return this.nextGridIntercept.exists();
			}

			@Override
			public Cell next() {
				if (!hasNext())
					throw new NoSuchElementException("Dead Cell path");

				GridIntercept thisIntercept = this.nextGridIntercept;
				GridIntercept nextIntercept = getGridIntercept(thisIntercept.getPointOfIntersection(), to);

				if (nextIntercept.hasXY()) {
					Vector2 direction = thisIntercept.getDirection();
					Cell sideCell = direction.getX() < 0
						? getCellLeftOf(thisIntercept.getCell())
						: getCellRightOf(thisIntercept.getCell());

					this.nextGridIntercept = getGridIntercept(sideCell.getPosition(), to);
				} else {
					this.nextGridIntercept = nextIntercept;
				}

				Console.println(thisIntercept);
				return thisIntercept.getCell();
			}
		}
	}

	public class GridIntercept {
		private CellGridAxis axisOfIntersection = CellGridAxis.NONE;
		private Vector2 pointOfIntersection;
		private Vector2 direction;
		private Cell cell;

		public GridIntercept setAxisOfIntersection(CellGridAxis axis) {
			this.axisOfIntersection = axis;
			return this;
		}

		public GridIntercept setPointOfIntersection(Vector2 point) {
			this.pointOfIntersection = point;
			return this;
		}

		public GridIntercept setDirection(Vector2 direction) {
			this.direction = direction;
			return this;
		}

		public GridIntercept setCell(Cell cell) {
			this.cell = cell;
			return this;
		}

		public Vector2 getDirection() {
			return this.direction;
		}

		public CellGridAxis getAxisOfIntersection() {
			return this.axisOfIntersection;
		}

		public Vector2 getPointOfIntersection() {
			return this.pointOfIntersection;
		}

		public Cell getCell() {
			return this.cell;
		}

		public boolean exists() {
			return this.axisOfIntersection != CellGridAxis.ENDPOINT;
		}

		public boolean hasX() {
			return this.axisOfIntersection == CellGridAxis.X_GRID;
		}

		public boolean hasY() {
			return this.axisOfIntersection == CellGridAxis.Y_GRID;
		}

		public boolean hasXY() {
			return this.axisOfIntersection == CellGridAxis.XY_GRID;
		}

		@Override
		public String toString() {
			return String.format(
					"$text-yellow GridIntercept$text-reset <Axis: %s, Point: %s>", 
					this.axisOfIntersection, 
					this.pointOfIntersection);
		}
	}
}
