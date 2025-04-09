package classes.entity;

public class Turn {
    public static int turnTracker = 0;
    int killCount = 0;

    int beginningAntCount;
    int endAntCount;

    int beginningDoodblebugCount;
    int endDoodlebugCount;

    int antBreedCount;
    int doodlebugBreedCount;
    
    Turn(){
        turnTracker++;
    }

    //Setters and Getters
    public void setKillCount(){
        killCount++;
    }
    
    public void setAntBreedCount(){
        antBreedCount++;
    }

    public void setDoodlebugBreedCount() {
        doodlebugBreedCount++;
    }

    public void setBeginningAntCount(int beginningAntCount){
        this.beginningAntCount = beginningAntCount;
    }

    public void setEndAntCount(int endAntCount){
        this.endAntCount = endAntCount;
    }

    public void setBeginningDoodlebugCount(int beginningDoodblebugCount){
        this.beginningDoodblebugCount = beginningDoodblebugCount;
    }

    public void setEndDoodlebugCount(int endDoodlebugCount){
        this.endDoodlebugCount = endDoodlebugCount;
    }

    public String getAntInstanceChange(){
        if(beginningAntCount > endAntCount){
            return "The ant population decreased from " + beginningAntCount + " to " + endAntCount;
        } else if (endAntCount > beginningAntCount){
            return "The ant population increased from " + beginningAntCount + " to " + endAntCount;
        } else {
            return "The ant population stayed the same";
        }
    }

    public String getDoodlebugInstanceChange(){
        if(beginningDoodblebugCount > endDoodlebugCount){
            return "The ant population decreased from " + beginningDoodblebugCount + " to " + endDoodlebugCount;
        } else if (endDoodlebugCount > beginningDoodblebugCount){
            return "The ant population increased from " + beginningDoodblebugCount + " to " + endDoodlebugCount;
        } else {
            return "The ant population stayed the same";
        }
    }

    public String getTurnKillCount(){
        return "Doodlebug killed " + killCount + " ants";
    }

    public int getAntBreedCount(){
        return this.antBreedCount;
    }

    public int getDoodlebugBreedCount(){
        return this.doodlebugBreedCount;
    }
}
