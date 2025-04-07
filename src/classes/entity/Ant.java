package classes.entity;

import classes.abstracts.Bug;

public class Ant extends Bug {
    public static int antCount = 0;
    public static int numOfAntBreeds = 0;

    public Ant(){
        idNum = (int)(Math.random() * 1000);
        this.setEatable(isEatable());
        antCount++;
    }

    @Override
    public void move(Cell currentCell, CellGrid grid){
        Cell[] adjCells = grid.getCellsAdjacentTo(currentCell);

        for (Cell adjCell : adjCells) {
            if (adjCell.isInBounds() && adjCell.isEmpty()) {
                currentCell.moveOccupantTo(adjCell);
                this.currentCell = adjCell;
                break;
            }
        }
        movementCounter++;

        if(movementCounter == 3){
            movementCounter = 0;
            this.breed(adjCells);
        }
    }

    @Override
    public void breed(Cell[] adjCells){
        for (Cell adjCell : adjCells) {
            if (adjCell.isInBounds() && adjCell.isEmpty()) {
                currentCell.setOccupant(new Ant());
                numOfAntBreeds++;
                break;
            }
        }
    }

    @Override
    public String toString(){
        return "Ant";
    }
}
