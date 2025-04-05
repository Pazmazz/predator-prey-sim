package classes.entity;

import classes.abstracts.Bug;
import classes.entity.Cell;
import classes.entity.CellGrid;

public class Ant extends Bug {
    
    public Ant(){
        idNum = (Math.random() * 1000);
    }

    public void movementChecks(){
        //TODO: Code for movement checks
    }

    public void onEaten(){

    }

    @Override
    public String toString(){
        return "Ant";
    }
}
