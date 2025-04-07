/*
 * @written 3/30/2025
 * Documentation can be found here: https://github.com/Pazmazz/predator-prey-sim/blob/will-feature-1/docs/CellGrid.md
 */
package classes.entity;

import classes.entity.Cell.CellType;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import exceptions.NoCellFoundException;
import classes.util.Math2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The primary API for interacting with the virtual game grid. Implements a
 * HashMap for mapping all cell coordinates to their corresponding cell objects
 * instead of using a 2D array for scalability and ease-of-access.
 */
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

	/**
	 * Checks if given `Unit2` dimensions are within the boundaries of the grid.
	 *
	 * @param unit The `Unit2` that is checked for being contained within the
	 *             boundaries of the grid
	 *
	 * @return true if a given cell unit exists within the bounds of the
	 *         specified grid size
	 * @see #isInBounds(Unit2)
	 */
	public boolean isInBounds(Unit2 unit) {
		return !(unit.getX() <= 0
				|| unit.getX() > this.size.getX()
				|| unit.getY() <= 0
				|| unit.getY() > this.size.getY());
	}

	/**
	 * Checks if a given {@code Vector2} <i>coordinate point</i> is within the
	 * boundaries of the grid. Unlike {@code Unit2}, the point's components may be
	 * {@code double} values.
	 *
	 * @param position The {@code Vector2} position that is checked for being within
	 *                 bounds of the grid
	 * @return true if a given position exists within the bounds of the
	 *         specified grid size
	 * 
	 * @see #isInBounds(Unit2)
	 * @see #isInBounds(Cell)
	 */
	public boolean isInBounds(Vector2 position) {
		return !(position.getX() < 0
				|| position.getX() > this.size.getX()
				|| position.getY() < 0
				|| position.getY() > this.size.getY());
	}

	/**
	 * Checks if a given {@code Cell} object is within the boundaries of the grid.
	 * 
	 * @param cell The {@code Cell} object that is checked for being within
	 *             bounds of the grid size.
	 * @return true if the cell object is within the grid size boundary
	 * 
	 * @see #isInBounds(Vector2)
	 * @see #isInBounds(Unit2)
	 * @see #isInBounds(Cell)
	 */
	public boolean isInBounds(Cell cell) {
		return isInBounds(cell.getUnit2());
	}

	/**
	 * Finds the grid-axis intercepts between two points and returns a
	 * {@code GridIntercept} object describing the points of intersection, which
	 * axis the line crossed, and the {@code Cell} object it passed through.
	 *
	 * @param start The starting point
	 * @param end   The ending point
	 *
	 * @return {@code GridIntercept} Object that contains metadata about what
	 *         the line between {@code start} and {@code end} intersected with
	 * @see #getGridIntercept(Vector2, Vector2)
	 */
	public GridIntercept getGridIntercept(Vector2 start, Vector2 end) {
		if (start.equals(end)) {
			return new GridIntercept().setAxisOfIntersection(CellGridAxis.ENDPOINT);
		}

		Vector2 signedUnit = end.subtract(start).signedUnit();
		GridIntercept interceptResult = new GridIntercept();
		interceptResult.setDirection(signedUnit);

		double startX = start.getX();
		double startY = start.getY();
		double endX = end.getX();
		double endY = end.getY();

		boolean posX = signedUnit.getX() > 0;
		boolean posY = signedUnit.getY() >= 0;

		double limitX = posX
				? Math.ceil(startX)
				: Math.floor(startX);

		double limitY = posY
				? Math.ceil(startY)
				: Math.floor(startY);

		if (startX == limitX) {
			limitX += signedUnit.getX();
		}

		if (startY == limitY) {
			limitY += signedUnit.getY();
		}

		double ty = (limitY - startY) / (endY - startY);
		double tx = (limitX - startX) / (endX - startX);

		Vector2 pointOfIntersection;
		CellGridAxis axisOfIntersection;

		// No intercepts
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

	/**
	 * The primary root method for retrieving a cell object from the virtual
	 * grid. All other overloaded methods of {@code getCell} internally call
	 * this method as a final destination.
	 *
	 * <p>
	 * If no cell object exists at the location {@code unit}, then one will be
	 * created immediately and stored within the {@code virtualGrid}.
	 *
	 * <p>
	 * If the requested cell is out of bounds, a cell object will still be
	 * created but will contain the private field enum {@code cellType} which
	 * will be set to {@code OUT_OF_BOUNDS}
	 *
	 * @param unit the cell label represented by its location on the grid
	 *
	 * @return {@code Cell} object containing metadata about the cell
	 * 
	 * @see #getCell()
	 * @see #getCell(Unit2)
	 * @see #getCell(Vector2)
	 * @see #getCell(Vector2, Vector2)
	 */
	public Cell getCell(Unit2 unit) {
		Cell cell = this.virtualGrid.get(unit.toString());
		if (cell != null) {
			return cell;
		}

		cell = new Cell(unit);
		this.virtualGrid.put(unit.toString(), cell);
		if (outOfBounds(unit)) {
			cell.setType(CellType.OUT_OF_BOUNDS);
		}
		return cell;
	}

	/**
	 * Get a {@code Cell} object from the virtual grid that lies on the point
	 * {@code position}.
	 *
	 * @param position any 2D position on the grid
	 * @return {@code Cell} object containing metadata about the cell
	 * 
	 * @see #getCell()
	 * @see #getCell(Unit2)
	 * @see #getCell(Vector2)
	 * @see #getCell(Vector2, Vector2)
	 */
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

	/**
	 * Gets the {@code Cell} object that lies on the midpoint of a segment of
	 * two given points.
	 *
	 * @param p0 the first point
	 * @param p1 the second point
	 *
	 * @return {@code Cell} object on the segment
	 * 
	 * @see #getCell()
	 * @see #getCell(Unit2)
	 * @see #getCell(Vector2)
	 * @see #getCell(Vector2, Vector2)
	 */
	public Cell getCell(Vector2 p0, Vector2 p1) {
		return getCell(p0.midpoint(p1));
	}

	/**
	 * Gets a {@code Cell} object at unit {@code (1, 1)} (mostly for
	 * one-off testing purposes)
	 *
	 * @return a {@code Cell} object at {@code (1, 1)}
	 * 
	 * @see #getCell()
	 * @see #getCell(Unit2)
	 * @see #getCell(Vector2)
	 * @see #getCell(Vector2, Vector2)
	 */
	public Cell getCell() {
		return getCell(new Unit2(1, 1));
	}

	/**
	 * The root method that checks if a given {@code Unit2} is outside the
	 * boundary of the grid size.
	 *
	 * @param unit the cell label of {@code Unit2} to check if outside bounds
	 * @return true if the {@code Unit2} object is out of bounds
	 * 
	 * @see #outOfBounds(Unit2)
	 */
	public boolean outOfBounds(Unit2 unit) {
		return !isInBounds(unit);
	}

	/**
	 * Checks if the given {@code Vector2} <i>coordinate point</i> is out of the
	 * grid bounrary.
	 *
	 * @param position position vector to check for being out out of bounds
	 * @return true if the {@code position} is out of bounds
	 * 
	 * @see #outOfBounds(Unit2)
	 * @see #outOfBounds(Cell)
	 */
	public boolean outOfBounds(Vector2 position) {
		return !isInBounds(position);
	}

	/**
	 * Checks if the given {@code Cell} object is within the grid boundary.
	 *
	 * @param cell the cell object to check for being out of bounds
	 * @return true if the {@code cell} is out of bounds
	 * 
	 * @see #outOfBounds(Unit2)
	 * @see #outOfBounds(Vector2)
	 */
	public boolean outOfBounds(Cell cell) {
		return !isInBounds(cell);
	}

	/**
	 * Collects a {@code Cell} object at a given {@code Unit2} location. Once
	 * collected, the cell instance will be removed from the virtual grid and be
	 * assigned a {@code cellType} of
	 * {@link classes.entity.Cell.CellType#GARBAGE_COLLECTED}
	 *
	 * <p>
	 * {@code Cell} objects will only be collected if they do NOT contain an
	 * occupant. Once an occupant is removed and there is no replacement, the
	 * cell will be eligible for GC.
	 *
	 * @param unit the cell at the specified {@code unit} to collect
	 * @return the collected {@code Cell} object if one prevously existed
	 * @throws NoCellFoundException if no cell was found at the given unit
	 * 
	 * @see classes.entity.Cell#isCollectable
	 * @see classes.entity.Cell#isCollected
	 * @see #collectCell(Unit2)
	 */
	public Cell collectCell(Unit2 unit) {
		Cell cell = this.virtualGrid.get(unit.toString());

		if (cell == null) {
			throw new NoCellFoundException();
		}

		if (cell.isCollectable()) {
			this.virtualGrid.remove(unit.toString());
			cell.setType(CellType.GARBAGE_COLLECTED);
		}

		return cell;
	}

	/**
	 * Collects the given {@code Cell} object if that cell is collectable.
	 *
	 * @param cell the {@code Cell} object to collect
	 * @return the collected {@code Cell} object
	 * @throws NoCellFoundException if the cell doesn't exist on the virtual grid
	 * 
	 * @see #collectCell(Unit2)
	 * @see #collectCell(Cell)
	 */
	public Cell collectCell(Cell cell) {
		return collectCell(cell.getUnit2());
	}

	/**
	 * Iterates over the entire virtual grid and frees up all non-used
	 * {@code Cell} objects (i.e cells that do not contain any occupants in
	 * them).
	 * 
	 * @see classes.entity.Cell#isCollectable
	 * @see classes.entity.Cell#isCollected
	 * @see classes.entity.CellGrid#collectCell
	 * @see #collectCells()
	 */
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

	/**
	 * Gets the {@code Cell} directly above a given {@code Unit2} location on
	 * the grid.
	 *
	 * @param unit the {@code Unit2} location to get the cell above it
	 * @return the {@code Cell} above the given {@code unit}
	 * @see #getCellTopOf(Unit2)
	 */
	public Cell getCellTopOf(Unit2 unit) {
		int y = unit.getY() == -1 ? 2 : 1;
		return getCell(unit.add(new Unit2(0, y)));
	}

	/**
	 * Gets the {@code Cell} directly below a given {@code Unit2} location on
	 * the grid.
	 *
	 * @param unit the {@code Unit2} location to get the cell below it
	 * @return the {@code Cell} below the given {@code unit}
	 * @see #getCellBottomOf(Unit2)
	 */
	public Cell getCellBottomOf(Unit2 unit) {
		int y = unit.getY() == 1 ? -2 : -1;
		return getCell(unit.add(new Unit2(0, y)));
	}

	/**
	 * Gets the {@code Cell} directly to the left of a given {@code Unit2}
	 * location on the grid.
	 *
	 * @param unit the {@code Unit2} location to get the cell left of it
	 * @return the {@code Cell} left of the given {@code unit}
	 * @see #getCellLeftOf(Unit2)
	 */
	public Cell getCellLeftOf(Unit2 unit) {
		int x = unit.getX() == 1 ? -2 : -1;
		return getCell(unit.add(new Unit2(x, 0)));
	}

	/**
	 * Gets the {@code Cell} directly to the right of a given {@code Unit2}
	 * location on the grid.
	 *
	 * @param unit the {@code Unit2} location to get the cell right of it
	 * @return the {@code Cell} right of the given {@code unit}
	 * @see #getCellRightOf(Unit2)
	 */
	public Cell getCellRightOf(Unit2 unit) {
		int x = unit.getX() == -1 ? 2 : 1;
		return getCell(unit.add(new Unit2(x, 0)));
	}

	/**
	 * Get the cell above an existing {@code Cell} object
	 * 
	 * @param cell the {@code Cell} object to find the cell above
	 * @return the cell above the given {@code cell} object
	 * @see #getCellTopOf(Unit2)
	 * @see #getCellTopOf(Cell)
	 */
	public Cell getCellTopOf(Cell cell) {
		return getCellTopOf(cell.getUnit2());
	}

	/**
	 * Get the cell below an existing {@code Cell} object
	 * 
	 * @param cell the {@code Cell} object to find the cell below
	 * @return the cell below the given {@code cell} object
	 * @see #getCellBottomOf(Unit2)
	 * @see #getCellBottomOf(Cell)
	 */
	public Cell getCellBottomOf(Cell cell) {
		return getCellBottomOf(cell.getUnit2());
	}

	/**
	 * Get the cell left of an existing {@code Cell} object
	 * 
	 * @param cell the {@code Cell} object to find the left cell of
	 * @return the cell left of the given {@code cell} object
	 * @see #getCellLeftOf(Unit2)
	 * @see #getCellLeftOf(Cell)
	 */
	public Cell getCellLeftOf(Cell cell) {
		return getCellLeftOf(cell.getUnit2());
	}

	/**
	 * Get the cell right of an existing {@code Cell} object
	 * 
	 * @param cell the {@code Cell} object to find the right cell of
	 * @return the cell right of the given {@code cell} object
	 * @see #getCellRightOf(Unit2)
	 * @see #getCellRightOf(Cell)
	 */
	public Cell getCellRightOf(Cell cell) {
		return getCellRightOf(cell.getUnit2());
	}

	/**
	 * @param position
	 * @see #getCellTopOf(Unit2)
	 * @see #getCellTopOf(Vector2)
	 */
	public Cell getCellTopOf(Vector2 position) {
		return getCellTopOf(getCell(position));
	}

	/**
	 * @param position
	 * @see #getCellBottomOf(Unit2)
	 * @see #getCellBottomOf(Vector2)
	 */
	public Cell getCellBottomOf(Vector2 position) {
		return getCellBottomOf(getCell(position));
	}

	/**
	 * @param position
	 * @see #getCellLeftOf(Unit2)
	 * @see #getCellLeftOf(Vector2)
	 */
	public Cell getCellLeftOf(Vector2 position) {
		return getCellLeftOf(getCell(position));
	}

	/**
	 * @param position
	 * @see #getCellRightOf(Unit2)
	 * @see #getCellRightOf(Vector2)
	 */
	public Cell getCellRightOf(Vector2 position) {
		return getCellRightOf(getCell(position));
	}

	/**
	 * Gets all {@code Cell} objects directly adjacent to a given target
	 * {@code Cell} cell, {@code Unit2} unit2, or {@code Vector2} position.
	 *
	 * <p>
	 * This method will always return four {@code Cell} objects, representing
	 * the four adjacent grid cells, even if they are out of bounds or occupied.
	 * Because of this, we will have to check cell conditions using
	 * {@code cell.isEmpty()} and such, manually.
	 *
	 * @param unit the unit of the cell to get the adjacent cells of
	 *
	 * @return fixed array of all four adjacent cells
	 * 
	 * @see #getCellsAdjacentTo(Cell)
	 * @see #getCellsAdjacentTo(Unit2)
	 */
	public Cell[] getCellsAdjacentTo(Unit2 unit) {
		Cell[] cells = new Cell[4];
		cells[0] = getCellTopOf(unit);
		cells[1] = getCellBottomOf(unit);
		cells[2] = getCellLeftOf(unit);
		cells[3] = getCellRightOf(unit);
		return cells;
	}

	/**
	 * Get the cells adjacent to a given {@code Cell} object.
	 * 
	 * @param cell the cell to find the adjacent cells to
	 * @return an array of {@code Cell} objects representing the cells adjacent to
	 *         {@code cell}
	 * @see #getCellsAdjacentTo(Unit2)
	 * @see #getCellsAdjacentTo(Cell)
	 */
	public Cell[] getCellsAdjacentTo(Cell cell) {
		return getCellsAdjacentTo(cell.getUnit2());
	}

	/**
	 * Get the size of the grid as its instantiated {@code Unit2} value
	 *
	 * @return the size of the grid as a {@code Unit2} object
	 * @see #getSize()
	 */
	public Unit2 getSize() {
		return size;
	}

	/**
	 * Debug methods for testing/displaying/formatting data related to the game
	 * grid.
	 *
	 * @param unit the unit of the cell to print the adjacent cell data of
	 * @see #printCellsAdjacentTo(Unit2)
	 */
	public void printCellsAdjacentTo(Unit2 unit) {
		Cell[] adjCells = getCellsAdjacentTo(unit);

		for (Cell adjCell : adjCells) {
			adjCell.printInfo();
		}
	}

	/**
	 * Overload: {@code printCellsAdjacentTo}
	 *
	 * @param cell the {@code Cell} object to print relative adjacent cells from
	 * @see #printCellsAdjacentTo(Cell)
	 */
	public void printCellsAdjacentTo(Cell cell) {
		printCellsAdjacentTo(cell.getUnit2());
	}

	/**
	 * Find and calculate grid cell objects forming a line across the two points
	 * {@code from} and {@code to}.
	 *
	 * @param from the starting point
	 * @param to   the ending point
	 *
	 * @return array list of cell objects to be returned all at once
	 * 
	 * @see #getCellPath(Vector2, Vector2)
	 * @see #getCellPathIterator(Vector2, Vector2)
	 */
	public ArrayList<Cell> getCellPath(Vector2 from, Vector2 to) {
		CellPathCollection path = new CellPathCollection(from, to);
		Iterator<Cell> pathIterator = path.iterator();

		while (pathIterator.hasNext()) {
			pathIterator.next();
		}

		return path.getCellPath();
	}

	/**
	 * Create an iterator for a generated cell path between some {@code Vector2}
	 * point {@code from} and {@code to}. Each call to the iterator will
	 * generate the next {@code Cell} object that is intersected by the line
	 * created between the two points.
	 *
	 * @param from the starting point
	 * @param to   the ending point
	 *
	 * @return the iterator for generating the cell path
	 * 
	 * @see #getCellPath(Vector2, Vector2)
	 * @see #getCellPathIterator(Vector2, Vector2)
	 */
	public Iterator<Cell> getCellPathIterator(Vector2 from, Vector2 to) {
		return new CellPathCollection(from, to).iterator();
	}

	/**
	 * Inner-class for creating a new collection of {@code Cell} objects that
	 * make up a cell path. Contains the ArrayList which stores the generated
	 * cells, and the iterator class for creating the iterator.
	 *
	 * <p>
	 * When a new {@code CellPathCollection} is created, the {@code cellPath}
	 * ArrayList will be empty since no path cells have been generated yet.
	 * Every call to {@code CellPathCollection.iterator().next()} will compute
	 * the next path cell and add it to the {@code cellPath} ArrayList.
	 */
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
			private ArrayList<Cell> cellQueue = new ArrayList<>();

			@Override
			public boolean hasNext() {
				return this.nextGridIntercept.exists();
			}

			@Override
			public Cell next() {
				if (!hasNext()) {
					throw new NoSuchElementException("Dead Cell path");
				}

				if (cellQueue.size() > 0) {
					return cellQueue.remove(0);
				}

				GridIntercept thisIntercept = this.nextGridIntercept;
				GridIntercept nextIntercept = getGridIntercept(thisIntercept.getPointOfIntersection(), to);
				this.nextGridIntercept = nextIntercept;

				if (nextIntercept.hasXY()) {
					Vector2 direction = nextIntercept.getDirection();
					Cell sideCell = direction.getX() < 0
							? getCellLeftOf(thisIntercept.getCell())
							: getCellRightOf(thisIntercept.getCell());

					cellQueue.add(sideCell);
				} else {
					this.nextGridIntercept = nextIntercept;
				}

				Console.debugPrint(DebugPriority.MEDIUM, thisIntercept);
				cellPath.add(thisIntercept.getCell());
				return thisIntercept.getCell();
			}
		}
	}

	/**
	 * Inner-class for creating grid intercept metadata for whenever some line
	 * crosses a grid axis.
	 */
	public class GridIntercept {

		private CellGridAxis axisOfIntersection = CellGridAxis.NONE;
		private Vector2 pointOfIntersection;
		private Vector2 direction;
		private Cell cell;

		//
		// Public setters and getters
		//
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
					"$text-yellow GridIntercept$text-reset <Axis: $text-purple %s$text-reset , Point: %s>",
					this.axisOfIntersection,
					this.pointOfIntersection);
		}
	}
}
