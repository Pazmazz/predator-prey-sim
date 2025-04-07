package classes.abstracts;

import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.CellOccupant;

public abstract class Bug extends CellOccupant {
    public int idNum;
    public int movementCounter = 0;

    public abstract void move(Cell currentCell, CellGrid grid);
    public abstract void breed(Cell[] adjCells);
}
