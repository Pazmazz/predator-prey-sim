package exceptions;

public class NoCellFoundException extends RuntimeException {

    public NoCellFoundException() {
        super("No cell object was found");
    }
}
