package exceptions;

public class CellIsOccupiedException extends RuntimeException {

	public CellIsOccupiedException() {
		super("Cell is already occupied - cannot assign cell");
	}
}
