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

    public int getAntCount(){
        return antCount;
    }

    @Override
    public void move(Cell currentCell, CellGrid grid, Turn turn){
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
            this.breed(adjCells, turn);
        }
    }

    @Override
    public void breed(Cell[] adjCells, Turn turn){
        for (Cell adjCell : adjCells) {
            if (adjCell.isInBounds() && adjCell.isEmpty()) {
                currentCell.setOccupant(new Ant());
                numOfAntBreeds++;
                turn.setAntBreedCount();
                break;
            }
        }
    }

    @Override
    public String toString(){
        return "Ant";
    }
}
