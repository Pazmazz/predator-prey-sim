/*
 * @written 3/31/2025
 */
package classes.entity;

import classes.util.Console;
import exceptions.CellIsOccupiedException;
import exceptions.NoOccupantFoundException;
import exceptions.OccupantHasCellException;

/**
 * This class allows you to interface with individual {@code Cell} components
 * that make up the virtual grid, which contains important metadata about that
 * specific cell location.
 *
 * <p>
 * Each cell object contains important data such as if something is currently
 * occupying the cell, what type of cell it is ({@code CellType} enum), and
 * setters/getters/update methods for interacting with the cell.
 */
public class Cell {

	final private Unit2 unit;
	final private Vector2 position;
	private CellType cellType;
	private CellVacancy cellVacancy;
	private CellOccupant cellOccupant;

	public enum CellType {
		OUT_OF_BOUNDS,
		NORMAL,
		GARBAGE_COLLECTED,
	}

	public enum CellVacancy {
		EMPTY,
		OCCUPIED,
	}

	public Cell(Unit2 unit) {
		this.unit = unit;
		this.position = new Vector2(
				unit.getX() - unit.signedUnit().getX() * 0.5,
				unit.getY() - unit.signedUnit().getY() * 0.5);

		this.cellType = CellType.NORMAL;
		this.cellVacancy = CellVacancy.EMPTY;
	}

	/**
	 * Aggregates {@code cellOccupant} to this cell as long as the current cell
	 * does not already have an occupant.
	 *
	 * <p>
	 * An optional argument {@code occupantAggregatesCell} is provided which
	 * dictates whether or not the occupant should incorperate the cell object
	 * into itself. Used for preventing a callback loop between the cell's
	 * {@code setOccupant} method and the occupant's {@code setCell} method,
	 * since they both call each other.
	 *
	 * @param cellOccupant   the aggregated occupant to nest within the cell
	 * @param updateOccupant whether the occupant should aggregate the cell
	 *                       object
	 *
	 * @throws CellIsOccupiedException  if the current cell already has an
	 *                                  occupant
	 * @throws OccupantHasCellException if the cellOccupant already belongs to
	 *                                  another cell
	 */
	public void setOccupant(CellOccupant cellOccupant, boolean occupantAggregatesCell) {
		if (hasOccupant() && this.cellOccupant != cellOccupant) {
			throw new CellIsOccupiedException();
		}
		if (cellOccupant.hasCell() && cellOccupant.getCell() != this) {
			throw new OccupantHasCellException();
		}
		if (occupantAggregatesCell) {
			cellOccupant.assignCell(this, false);
		}

		this.cellOccupant = cellOccupant;
		setVacancy(CellVacancy.OCCUPIED);
	}

	/**
	 * Overload: {@code setOccupant}
	 *
	 * @param cellOccupant the occupant to set the current cell's occupant to
	 */
	public void setOccupant(CellOccupant cellOccupant) {
		setOccupant(cellOccupant, true);
	}

	/**
	 * Added isOccupantEatable method (by Jaylen)
	 *
	 * This checks to see if the occupant in the specified cell is eatable
	 *
	 * @param cell
	 * @return
	 */
	public boolean isOccupantEatable(Cell cell) {
		CellOccupant _cellOccupant = cell.getOccupant();
		return _cellOccupant.isEatable();
	}

	/**
	 * Moves the occupant from the current cell, to a new target cell. Does not
	 * account for target cell being occupied.
	 * 
	 * <p>
	 * This was deprecated because it doesn't make much sense to reference a cell
	 * object in order to move it's occupant.
	 *
	 * @deprecated use {@link classes.entity.CellOccupant#assignCell(Cell)} now
	 *             instead.
	 */
	@Deprecated
	public void moveOccupantTo(Cell targetCell) {
		targetCell.setOccupant(removeOccupant());
	}

	/**
	 * Retrieves the current occupant in the cell
	 *
	 * @return the current cell's occupant
	 */
	public CellOccupant getOccupant() {
		return this.cellOccupant;
	}

	/**
	 * Removes the current occupant in the cell. Does not check if the cell
	 * already has an occupant; this must be done manually with
	 * {@code hasOccupant}
	 *
	 * @return the removed occupant
	 *
	 * @throws NoOccupantFoundException if calling this method when the cell has
	 *                                  no occupant
	 */
	public CellOccupant removeOccupant() {
		if (!hasOccupant()) {
			throw new NoOccupantFoundException();
		}

		CellOccupant _cellOccupant = this.cellOccupant;
		this.cellOccupant = null;
		setVacancy(CellVacancy.EMPTY);
		return _cellOccupant;
	}

	//
	// Public getters and logic checks
	//
	public Vector2 getPosition() {
		return this.position;
	}

	public Unit2 getUnit2() {
		return this.unit;
	}

	public CellType getType() {
		return this.cellType;
	}

	public CellVacancy getVacancy() {
		return this.cellVacancy;
	}

	public boolean isEmpty() {
		return this.cellVacancy == CellVacancy.EMPTY;
	}

	public boolean hasOccupant() {
		return this.cellVacancy == CellVacancy.OCCUPIED;
	}

	public boolean isOutOfBounds() {
		return this.cellType == CellType.OUT_OF_BOUNDS;
	}

	public boolean isInBounds() {
		return this.cellType == CellType.NORMAL;
	}

	public boolean isCollected() {
		return this.cellType == CellType.GARBAGE_COLLECTED;
	}

	public boolean isCollectable() {
		return isEmpty() || this.cellOccupant == null;
	}

	public void setType(CellType cellType) {
		this.cellType = cellType;
	}

	public void setVacancy(CellVacancy cellVacancy) {
		this.cellVacancy = cellVacancy;
	}

	public void printInfo() {
		Console.println(toString());
		printInfoItem("Type", getType().toString());
		printInfoItem("Vacancy", getVacancy().toString());

		if (hasOccupant()) {
			printInfoItem("Occupant", getOccupant().toString());
		}
	}

	public void printInfoItem(String item, String content) {
		Console.println("- $text-yellow %s: $text-reset %s".formatted(item, content));
	}

	@Override
	public String toString() {
		return String.format("$text-green Cell$text-reset <%s, %s>", unit.getX(), unit.getY());
	}
}
