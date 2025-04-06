package exceptions;

public class VectorArgumentIsNullException extends RuntimeException {
	public VectorArgumentIsNullException() {
		super("BaseVector expected, got null");
	}
}
