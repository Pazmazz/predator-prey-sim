/*
 * @Written: 3/29/2025
 * 
 * The root application API from which all subclasses
 * inherit. Responsible for providing universal utility
 * methods and/or cumulative runtime data.
 */

package classes;

public abstract class Application {
	public double tick() {
		return System.currentTimeMillis();
	}

	public void wait(double milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
