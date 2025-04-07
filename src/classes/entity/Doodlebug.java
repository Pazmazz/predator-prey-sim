package classes.entity;

import classes.abstracts.Bug;

public class Doodlebug extends Bug {
    int starvationTracker = 0;
    public static int doodlebugCounter = 0;
    public static int killCount = 0;
    public static int numOfDoodlebugBreeds = 0;

    public Doodlebug(){
        idNum = (int)(Math.random() * 1000);
    }
    
    @Override
    public void move(Cell currentCell, CellGrid grid, Turn turn){
        Cell[] adjCells = grid.getCellsAdjacentTo(currentCell);

        for (Cell adjCell : adjCells) {
            if (currentCell.isOccupantEatable(currentCell)){
                currentCell.removeOccupant();
                currentCell.moveOccupantTo(adjCell);
                
                this.currentCell = adjCell;
                starvationTracker = 0;
                killCount++;
                break;
            } else if (adjCell.isInBounds() && adjCell.isEmpty()){
                currentCell.moveOccupantTo(adjCell);
                
                this.currentCell = adjCell;
                starvationTracker++;
                break;
            }
        }
        movementCounter++;

        if(starvationTracker == 3){
            currentCell.removeOccupant();
        }

        if(movementCounter == 8){
            movementCounter = 0;
            this.breed(adjCells, turn);
        }
    }

    @Override
    public void breed(Cell[] adjCells, Turn turn){
        for (Cell adjCell : adjCells) {
            if (adjCell.isInBounds() && adjCell.isEmpty()) {
                currentCell.setOccupant(new Doodlebug());
                numOfDoodlebugBreeds++;
                break;
            }
        }
    }

    @Override
    public String toString(){
        return "Doodlebug";
    }
}
