/*
 * @Written: 3/31/2025
 * 
 * class Cell:
 * 
 * This class allows you to interface with the virtual
 * grid by adding occupants to a cell, checking grid
 * conditions, finding other relative cell positions,
 * etc.
 */
package classes.entity;

import classes.util.Console;

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
	
	public void setOccupant(CellOccupant cellOccupant, boolean updateOccupant) {
		if (isOccupied())
			throw new Error(
					String.format(
							"Cell<%s, %s> tried to set a new occupant without removing its current occupant",
							this.getUnit2().getX(),
							this.getUnit2().getY()));
						

		this.cellOccupant = cellOccupant;
		setVacancy(CellVacancy.OCCUPIED);

		if (updateOccupant)
			cellOccupant.setCell(this, false);
	}

	public void setOccupant(CellOccupant cellOccupant) {
		setOccupant(cellOccupant, true);
	}

	public CellOccupant getOccupant() {
		return this.cellOccupant;
	}

	public CellOccupant removeOccupant() {
		CellOccupant _cellOccupant = this.cellOccupant;
		this.cellOccupant = null;
		setVacancy(CellVacancy.EMPTY);
		return _cellOccupant;
	}

	public void moveOccupantTo(Cell targetCell) {
		targetCell.setOccupant(removeOccupant());
	}

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

	public boolean isOccupied() {
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

		if (isOccupied()) {
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
