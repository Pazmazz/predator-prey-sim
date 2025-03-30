package classes.entity;

public class Vector2 {
	public int X;
	public int Y;

	public Vector2(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	@Override
	public String toString() {
		return "<%s, %s>".formatted(X, Y);
	}
}
