/*
 * @written 4/2/2025
 */
package classes.abstracts;

import classes.entity.CellGrid.Cell;

import java.util.ArrayList;
import java.util.HashMap;

import classes.entity.Null;
import classes.entity.Vector2;
import exceptions.CellIsOccupiedException;
import exceptions.NoCellFoundException;
import interfaces.Serializable;

/**
 * A superclass representing the most general entity that is allowed to be
 * considered an occupant in a {@code Cell} object.
 *
 * <p>
 * Any subclass that extends {@code Entity} is eligible to be set as an
 * occupant in a cell using {@code cell.setOccupant}.
 */
public abstract class REFACTOR_Entity<T> {
	// cells the entity is taking up
	final private HashMap<Cell, Boolean> cells = new HashMap<>();

	// cells the entity is taking up, but this is a critical cell (part of the core
	// entity) if all cell nodes are destroyed, the entity is destroyed.
	final private HashMap<Cell, Boolean> cellNodes = new HashMap<>();

	final private ArrayList<String> avatar = new ArrayList<>();

	public REFACTOR_Entity() {
		avatar.add("Cell{Unit2{1, 1}}");
		avatar.add("Cell{Unit2{1, 2}}");
		avatar.add("Cell{Unit2{2, 1}}");
	}

	public void assignCell(Cell targetCell, boolean withAggregation) {
		if (targetCell == null)
			throw new NoCellFoundException();
	}

	public void assignCell(Cell targetCell) {
		assignCell(targetCell, true);
	}

	public Vector2[] getBoundingBox() {

	}

}
