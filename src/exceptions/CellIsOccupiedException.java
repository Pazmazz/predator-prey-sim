package exceptions;

public class CellIsOccupiedException extends RuntimeException {

    public CellIsOccupiedException() {
        super("Cell is occupied");
    }
}
