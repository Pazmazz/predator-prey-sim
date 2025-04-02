package exceptions;

public class VectorMismatchException extends RuntimeException {
	public VectorMismatchException(String methodName) {
		super("Vector." + methodName + "() requires that both Vectors have equal dimensions");
	}
}
