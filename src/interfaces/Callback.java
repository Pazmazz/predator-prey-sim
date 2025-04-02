package interfaces;

public interface Callback {
	Object call(Object... args);
	Double computeDouble(Double a);
	Double computeDoubles(Double a, Double b);
}
