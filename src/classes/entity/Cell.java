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

import classes.abstracts.CellOccupant;
import classes.util.Console;

public class Cell {
	private Unit2 unit;
	private Vector2 position;
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

	public enum CellDirection {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
	}

	public Cell(Unit2 unit) {
		this.unit = unit;
		this.cellType = CellType.NORMAL;
		this.cellVacancy = CellVacancy.EMPTY;
	}

	public CellOccupant getOccupant() {
		return cellOccupant;
	}

	public void removeOccupant() {
		this.cellOccupant = null;
		setVacancy(CellVacancy.EMPTY);
	}

	public void moveOccupantTo(Cell cell) {
		cell.setOccupant(getOccupant());
		removeOccupant();
	}

	public Unit2 getPosition() {
		return position;
	}

	public CellType getType() {
		return cellType;
	}

	public CellVacancy getVacancy() {
		return cellVacancy;
	}

	public boolean isEmpty() {
		return getVacancy() == CellVacancy.EMPTY;
	}

	public boolean isOccupied() {
		return getVacancy() == CellVacancy.OCCUPIED;
	}

	public boolean isOutOfBounds() {
		return getType() == CellType.OUT_OF_BOUNDS;
	}

	public boolean isInBounds() {
		return getType() == CellType.NORMAL;
	}

	public boolean isCollected() {
		return getType() == CellType.GARBAGE_COLLECTED;
	}

	public boolean isCollectable() {
		return isEmpty() || getOccupant() == null;
	}

	public void setType(CellType cellType) {
		this.cellType = cellType;
	}

	public void setVacancy(CellVacancy cellVacancy) {
		this.cellVacancy = cellVacancy;
	}

	public void setOccupant(CellOccupant cellOccupant) {
		this.cellOccupant = cellOccupant;
		setVacancy(CellVacancy.OCCUPIED);
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
		return "$text-green Cell$text-reset " + position.toString();
	}
}
