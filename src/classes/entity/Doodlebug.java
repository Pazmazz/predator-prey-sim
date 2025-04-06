package classes.entity;

import classes.abstracts.Bug;

public class Doodlebug extends Bug {

    int starvationTracker = 0;

    public Doodlebug() {
        idNum = (int) (Math.random() * 1000);
    }

    @Override
    public void move(Cell currentCell, CellGrid grid) {
        Cell[] adjCells = grid.getCellsAdjacentTo(currentCell);

        for (Cell adjCell : adjCells) {
            if (adjCell.isOccupantEatable(currentCell)) {

                //
                // Accounting for removal of 'moveOccupantTo' method,
                // this does the same thing.
                //
                adjCell.removeOccupant();
                assignCell(adjCell);

                //
                // currentCell.removeOccupant();
                // currentCell.moveOccupantTo(adjCell);
                // this.currentCell = adjCell;
                //
                starvationTracker = 0;
                break;
            } else if (adjCell.isInBounds() && adjCell.isEmpty()) {
                assignCell(adjCell);
                //
                // currentCell.moveOccupantTo(adjCell);
                // this.currentCell = adjCell;
                //
                starvationTracker++;
                break;
            }
        }
        movementCounter++;

        if (starvationTracker == 3) {
            currentCell.removeOccupant();
        }

        if (movementCounter == 8) {
            movementCounter = 0;
            this.breed(adjCells);
        }
    }

    @Override
    public void breed(Cell[] adjCells) {
        for (Cell adjCell : adjCells) {
            if (adjCell.isInBounds() && adjCell.isEmpty()) {
                adjCell.setOccupant(new Doodlebug());
                //
                // currentCell.setOccupant(new Doodlebug());
                //
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Doodlebug";
    }
}
