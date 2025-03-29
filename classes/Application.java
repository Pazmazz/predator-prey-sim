package classes;

public class Application {
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
