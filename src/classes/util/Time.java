package src.classes.util;

public class Time {
	public static long secondsToNano(double seconds) {
		return (long) (seconds * 1_000_000_000L);
	}

	public static double nanoToSeconds(long nano) {
		return (double) nano / 1_000_000_000L;
	}

	public static double nanoToMillisecond(long nano) {
		return nano / 1_000_000.0;
	}
}
