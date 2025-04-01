/*
 * @Written: 3/29/2025
 * 
 * The root application API from which all subclasses
 * inherit. Responsible for providing universal utility
 * methods and/or cumulative runtime data.
 */

package classes.abstracts;

public abstract class Application {
	public long tick() {
		return System.nanoTime();
	}

	public void wait(double milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
