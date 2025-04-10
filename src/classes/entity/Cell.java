/*
 * @written 3/31/2025
 */
package classes.entity;

import classes.abstracts.Entity;
import classes.abstracts.Properties.Property;
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
	private Entity cellOccupant;

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
	 * accordance with a {@code CellGrid}, then you should use
	 * {@link classes.entity.CellGrid#getCell(Unit2)} instead.
	 * 
	 * <p>
	 * New {@code Cell} objects have these default fields:
	 * <ul>
	 * <li><i>type:</i> {@code NORMAL}</li>
	 * <li><i>vacancy:</i> {@code EMPTY}</li>
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
	 * <h4>This method should not be used outside of the {@code Entity}
	 * class.</h4>
	 * Use {@link #setOccupant(Entity)} instead.
	 * <p>
	 * 
	 * Aggregates {@code cellOccupant} to this cell as long as the current
	 * cell
	 * does not already have an occupant.
	 * 
	 * <p>
	 * An optional argument {@code withAggregation} is provided which
	 * dictates whether or not the occupant should incorporate the cell
	 * object
	 * into itself. Used for preventing a callback loop between the cell's
	 * {@code setOccupant} method and the occupant's {@code setCell} method,
	 * since they both call each other.
	 *
	 * @param cellOccupant    the aggregated occupant to nest within the cell
	 * @param withAggregation whether the occupant should aggregate the cell
	 *                        object
	 *
	 * @throws CellIsOccupiedException  if the current cell already has an
	 *                                  occupant
	 * @throws OccupantHasCellException if the cellOccupant already belongs to
	 *                                  another cell
	 * @throws NoOccupantFoundException if {@code cellOccupant} is null
	 */
	public void setOccupant(Entity cellOccupant, boolean withAggregation) {
		if (cellOccupant == null)
			throw new NoOccupantFoundException();

		if (hasOccupant() && this.cellOccupant != cellOccupant)
			throw new CellIsOccupiedException(this, cellOccupant);

		Cell occupantCell = cellOccupant.getProperty(
				Property.ASSIGNED_CELL,
				Cell.class);

		if (withAggregation) {
			if (cellOccupant.hasCell() && occupantCell != this)
				throw new OccupantHasCellException();

			cellOccupant.assignCell(this, false);
		}

		this.cellOccupant = cellOccupant;
		setVacancy(CellVacancy.OCCUPIED);
	}

	/**
	 * Sets a {@code Entity} to this cell, and the {@code Entity}
	 * aggregates this cell into itself so they are both mutually connected to each
	 * other.
	 * 
	 * @param cellOccupant the occupant to assign to the cell
	 * @see #setOccupant(Entity, boolean)
	 */
	public void setOccupant(Entity cellOccupant) {
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
		Entity _cellOccupant = cell.getOccupant();
		return _cellOccupant.getProperty(Property.IS_EATABLE, Boolean.class);
	}

	/**
	 * Moves the occupant from the current cell, to a new target cell. Does not
	 * account for target cell being occupied.
	 * 
	 * <p>
	 * This was deprecated because it doesn't make much sense to reference a cell
	 * object in order to move it's occupant. Instead, the occupant should be the
	 * one in charge of moving. As of now, the {@code Cell} class can exist
	 * independently of the {@code CellGrid} class. Therefore, it should make no
	 * reference to the cell grid because it is not a composition.
	 * <p>
	 * <b>Note:</b>
	 * This may change in the future, because there is no real reason for the
	 * {@code Cell} class to exist independently when it is only used in tandem
	 * with {@code CellGrid}. As development continues, I think it makes more sense
	 * for the {@code Cell} class to exist as a private inner-class of the
	 * {@code CellGrid}. More on this later.
	 * 
	 * @param the target {@code Cell} object to move this cell's occupant to
	 *
	 * @deprecated use {@link classes.abstracts.Entity#assignCell(Cell)} now
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
	public Entity getOccupant() {
		return this.cellOccupant;
	}

	/**
	 * Removes the current occupant in the cell. Does not check if the cell
	 * already has an occupant; this must be done manually with
	 * {@code hasOccupant}
	 *
	 * @return the removed occupant
	 * @throws NoOccupantFoundException if calling this method when the cell has
	 *                                  no occupant
	 */
	public Entity removeOccupant(boolean withAggregation) {
		if (!hasOccupant())
			throw new NoOccupantFoundException();

		Entity occupant = this.cellOccupant;
		this.cellOccupant = null;
		setVacancy(CellVacancy.EMPTY);

		if (withAggregation)
			occupant.removeFromCell(false);

		return occupant;
	}

	public Entity removeOccupant() {
		return removeOccupant(true);
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

	/**
	 * Checks if this cell is within the grid's boundaries.
	 * <p>
	 * <b>Note:</b> By default, a {@code Cell} object has <b>no</b> relationship
	 * with a {@code CellGrid}.
	 * 
	 * @see #isOutOfBounds()
	 * @return true if the cell is within boundaries of it's grid
	 */
	public boolean isInBounds() {
		return this.cellType == CellType.NORMAL;
	}

	/**
	 * Checks if this cell is currently queued up for garbage collection. Cells are
	 * only queued for GC once {@link classes.entity.CellGrid#collectCell} has been
	 * called on them.
	 * 
	 * @see classes.entity.CellGrid#collectCell
	 * @see classes.entity.CellGrid#collectCells
	 * 
	 * @return true if the cell is staged for garbage collection
	 */
	public boolean isCollected() {
		return this.cellType == CellType.GARBAGE_COLLECTED;
	}

	/**
	 * Checks if this cell is eligible to be garbage collected, but is not currently
	 * queued up to be GC'd.
	 * <p>
	 * For example, if a {@code Cell} object has no occupant, it may be eligible for
	 * garbage collection.
	 * 
	 * @see #isCollected
	 * @see classes.entity.CellGrid#collectCell
	 * @see classes.entity.CellGrid#collectCells
	 * 
	 * @return true if this cell is eligible for garbage collection
	 */
	public boolean isCollectable() {
		return isEmpty() || this.cellOccupant == null;
	}

	/**
	 * Sets the {@link CellType} of this cell.
	 * 
	 * @see #getType()
	 * @param cellType the {@link CellType} to set the cell to
	 */
	public void setType(CellType cellType) {
		this.cellType = cellType;
	}

	/**
	 * Sets the {@link CellVacancy} of this cell
	 * 
	 * @see #getVacancy()
	 * @param cellVacancy the {@link CellVacancy} to set the cell to
	 */
	public void setVacancy(CellVacancy cellVacancy) {
		this.cellVacancy = cellVacancy;
	}

	/**
	 * Prints a blob of cell fields to the console
	 */
	public void printInfo() {
		Console.println(toString());
		printInfoItem("Type", getType().toString());
		printInfoItem("Vacancy", getVacancy().toString());

		if (hasOccupant())
			printInfoItem("Occupant", getOccupant().toString());
	}

	/**
	 * Print info related to the cell using the {@link #printInfo} format and
	 * console colors
	 * 
	 * @param item    the prefix of the information
	 * @param content the content of the information
	 */
	public void printInfoItem(String item, String content) {
		Console.println("- $text-yellow %s: $text-reset %s".formatted(item, content));
	}

	@Override
	public String toString() {
		return String.format(
				Console.withConsoleColors("$text-green Cell$text-reset <%s, %s>"),
				unit.getX(),
				unit.getY());
	}
}
