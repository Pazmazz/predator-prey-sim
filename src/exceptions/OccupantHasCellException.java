package exceptions;

public class OccupantHasCellException extends RuntimeException {

    public OccupantHasCellException() {
        super("The occupant has already been assigned another cell; cannot set occupant to this cell");
    }
}
