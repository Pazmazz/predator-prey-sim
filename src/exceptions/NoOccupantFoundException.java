package exceptions;

public class NoOccupantFoundException extends RuntimeException {

    public NoOccupantFoundException() {
        super("No occupant was found");
    }
}
