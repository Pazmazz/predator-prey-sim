package classes.entity;

public class IntVector2 extends Vector2 {
	final public int X;
	final public int Y;

	public IntVector2(int x, int y) {
		super(x, y);
		this.X = x;
		this.Y = y;
	}

	public Vector2 getCenter() {
		return new Vector2(
			this.X - 0.5,
			this.Y - 0.5
		);
	}

	@Override
	public String toString() {
		return "<%s, %s>".formatted(X, Y);
	}
}
