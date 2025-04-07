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

	/**
	 * Creates a new {@code Cell} object. A cell object by itself does not belong to
	 * a {@link classes.entity.CellGrid}. If the intention is to use this cell in
	 * accordance with a {@code CellGrid}, then you sould use
	 * {@link classes.entity.CellGrid#getCell(Unit2)} instead.
	 * 
	 * <p>
	 * New {@code Cell} objects have these default fields:
	 * <ul>
	 * <li><i>type:</i> {@code NORMAL}</li>
	 * <li><i>vacandy:</i> {@code EMPTY}</li>
	 * </ul>
	 * 
	 * <p>
	 * Accessing these types can be done with:
	 * 
	 * <pre>
	 * <code>
	 * new Cell().getType();
	 * new Cell().getVacancy();
	 * </code>
	 * </pre>
	 * 
	 * @param unit the {@code Unit2} location identity of the cell
	 */
	public Cell(Unit2 unit) {
		this.unit = unit;
		this.position = new Vector2(
				unit.getX() - unit.signedUnit().getX() * 0.5,
				unit.getY() - unit.signedUnit().getY() * 0.5);

		this.cellType = CellType.NORMAL;
		this.cellVacancy = CellVacancy.EMPTY;
	}

	/**
	 * No-arg constructor for creating a new {@code Cell}. Creates a new
	 * {@code Cell} object with a default unit of {@code new Unit2(1, 1)}
	 * 
	 * <p>
	 * In other words, these are equivalent:
	 * 
	 * <pre>
	 * <code>
	 * Cell cell0 = new Cell();
	 * Cell cell1 = new Cell(new Unit(1, 1));
	 * </code>
	 * </pre>
	 * 
	 * @see classes.entity.Unit2
	 * @see #Cell(Unit2)
	 */
	public Cell() {
		this(new Unit2());
	}

	/**
	 * <h4>This method should not be used outside of the {@code CellOccupant}
	 * class.</h4>
	 * Use {@link #setOccupant(CellOccupant)} instead.
	 * <p>
	 * 
	 * Aggregates {@code cellOccupant} to this cell as long as the current
	 * cell
	 * does not already have an occupant.
	 * 
	 * <p>
	 * An optional argument {@code occupantAggregatesCell} is provided which
	 * dictates whether or not the occupant should incorperate the cell
	 * object
	 * into itself. Used for preventing a callback loop between the cell's
	 * {@code setOccupant} method and the occupant's {@code setCell} method,
	 * since they both call each other.
	 *
	 * @param cellOccupant           the aggregated occupant to nest within the cell
	 * @param occupantAggregatesCell whether the occupant should aggregate the cell
	 *                               object
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
	 * Sets a {@code CellOccupant} to this cell, and the {@code CellOccupant}
	 * aggregates this cell into itself so they are both mutually connected to each
	 * other.
	 * 
	 * @param cellOccupant the occupant to assign to the cell
	 *
	 * @see #setOccupant(CellOccupant, boolean)
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
	 * @return true if the occupant is eatable
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
	 * object in order to move it's occupant. Instead, the occupant should be the
	 * one in charge of moving. As of now, the {@code Cell} class can exist
	 * independantly of the {@code CellGrid} class. Therefore, it should make no
	 * reference to the cell grid because it is not a composition.
	 * <p>
	 * <b>Note:</b>
	 * This may change in the future, because there is no real reason for the
	 * {@code Cell} class to exist independantly when it is only used in tandem
	 * with {@code CellGrid}. As development continues, I think it makes more sense
	 * for the {@code Cell} class to exist as a private inner-class of the
	 * {@code CellGrid}. More on this later.
	 * 
	 * @param the target {@code Cell} object to move this cell's occupant to
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

	/**
	 * Get the position of this cell as a {@code Vector2} <i>coordinate point</i> on
	 * the grid, centered on the cell.
	 * <p>
	 * <b>Consider:</b>
	 * 
	 * <pre>
	 * <code>
	 * Cell cell = new Cell(new Unit2(5, 5));
	 * Console.println(cell.getPosition()); 
	 * </code>
	 * </pre>
	 * 
	 * Output: {@code Vector2<4.5, 4.5>}
	 * 
	 * @return the centered coordinate point of the cell as a {@code Vector2}
	 */
	public Vector2 getPosition() {
		return this.position;
	}

	/**
	 * Get the location identity (unit) of this cell. This is the same {@code Unit2}
	 * value that was passed to the {@code Cell} object's constructor, either
	 * directly or through {@link classes.entity.CellGrid#getCell}
	 * 
	 * @return the {@code Unit2} identity of this cell
	 */
	public Unit2 getUnit2() {
		return this.unit;
	}

	/**
	 * Get the type of this cell.
	 * 
	 * <p>
	 * Cell types can be the following {@link Cell.CellType} enums:
	 * <ul>
	 * <li>{@code NORMAL} - <i>A normal cell that is within the bounds of the
	 * grid</i></li>
	 * <li>{@code OUT_OF_BOUNDS} - <i>A cell that exists outside of the grid's
	 * boundary</i></li>
	 * <li>{@code GARBAGE_COLLECTED} - <i>A cell that is currently queued up the be
	 * garbage collected</i></li>
	 * </ul>
	 * 
	 * @return the {@link Cell.CellType} enum of this cell
	 */
	public CellType getType() {
		return this.cellType;
	}

	/**
	 * Get this cell's vacancy. Vacancy is determined by the following
	 * {@link Cell.CellVacancy} enums:
	 * 
	 * <ul>
	 * <li>{@code EMPTY} - <i>If the cell is empty (has no occupant)</i></li>
	 * <li>{@code OCCUPIED} - <i>If a cell is occupied (has an occupant)</i></li>
	 * </ul>
	 * 
	 * @return the {@link Cell.CellVacancy} enum of this cell
	 */
	public CellVacancy getVacancy() {
		return this.cellVacancy;
	}

	/**
	 * Checks if this cell is empty (has no occupants).
	 * 
	 * @return true if the cell has no occupant
	 */
	public boolean isEmpty() {
		return this.cellVacancy == CellVacancy.EMPTY;
	}

	/**
	 * Checks if this cell has an occupant.
	 * 
	 * @return true if the cell has an occupant
	 */
	public boolean hasOccupant() {
		return this.cellVacancy == CellVacancy.OCCUPIED;
	}

	/**
	 * Checks if this cell is out of the grid's boundaries.
	 * <p>
	 * <b>Note:</b> By default, a {@code Cell} object has <b>no</b> relationship
	 * with a {@code CellGrid}. This field is only set when
	 * {@link classes.entity.CellGrid#getCell(Unit2)} is called, since that method
	 * will manually call {@link #setType(CellType)} on this cell once it has been
	 * created.
	 * 
	 * @return true if the cell is out of the grid's boundary
	 */
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
