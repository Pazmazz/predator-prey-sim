package test;

import java.util.HashMap;

import classes.abstracts.Entity;
import classes.entity.Unit2;
import classes.entity.Vector2;
import exceptions.NoOccupantFoundException;

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
@SuppressWarnings("unused")
public class REFACTOR_Cell {

	// metadata
	final private HashMap<CellState, Boolean> states = new HashMap<>();
	final private HashMap<Entity<?>, Boolean> occupants = new HashMap<>();
	final private Unit2 unit;
	final private Vector2 unitPosition;

	private int importance = 0; // importance of zero or less is eligible for GC (based on sum of state weights)
	private boolean garbageCollected;
	private boolean inBounds;

	// properties
	private String color;
	private String ascii;
	private boolean visable;
	private int occupantWeightLimit; // based on sum of occupant weights
	private int occupantLimit;
	private boolean occupiable;
	private boolean locked; // cannot GC

	public enum CellState {
		PATH_CELL(1);

		private int weight;

		CellState(int weight) {
			this.weight = weight;
		}

		public int getWeight() {
			return this.weight;
		}
	}

	public REFACTOR_Cell(Unit2 unit) {
		this.unit = unit;
		this.unitPosition = new Vector2(
				unit.getX() - 0.5,
				unit.getY() - 0.5);
	}

	public REFACTOR_Cell(HashMap<String, String> inputStream) {

	}

	public void addOccupant(Entity<?> occupant, boolean withAggregation) {
		if (occupant == null)
			throw new NoOccupantFoundException();
	}

	public void addOccupant(Entity<?> occupant) {
		addOccupant(occupant, true);
	}

	public boolean isOccupied() {
		return this.occupants.size() > 0;
	}

	public boolean containsEntity(Entity<?> occupant) {
		return this.occupants.get(occupant);
	}

	public Vector2 getUnitPosition() {
		return this.unitPosition;
	}

	public Unit2 getUnit() {
		return this.unit;
	}

	public boolean isCollectable() {
		return isCollectable(0);
	}

	public boolean isCollectable(int importance) {
		return this.importance <= importance;
	}

	public REFACTOR_Entity<?> removeOccupant(boolean withAggregation) {
		if (!hasOccupant())
			throw new NoOccupantFoundException();
	}

	public REFACTOR_Entity<?> removeOccupant() {
		return removeOccupant(true);
	}

	public boolean isEmpty() {

	}

	public boolean isInBounds() {
		return this.cellType == CellType.NORMAL;
	}

	public boolean isCollected() {
		return this.cellType == CellType.GARBAGE_COLLECTED;
	}

	@Override
	public String serialize() {
		StringBuilder out = new StringBuilder(
				getClass().getSimpleName());

		out.append("{");
		out.append(getUnit2().serialize());
		out.append(", ");
		out.append(hasOccupant()
				? getOccupant().serialize()
				: "null");
		out.append("}");
		return out.toString();
	}

	@Override
	public String toString() {
		return String.format(
				Console.withConsoleColors("$text-green Cell$text-reset <%s, %s>"),
				unit.getX(),
				unit.getY());
	}
}