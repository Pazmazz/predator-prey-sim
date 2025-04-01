package classes.entity;

import classes.abstracts.Vector;
import java.util.ArrayList;
import java.util.List;

public class Unit2 extends Vector {
	private List<Integer> components;

	public Unit2() {
		this(0, 0);
	}

	public Unit2(int x, int y) {
		components = new ArrayList<>();
		components.add(x);
		components.add(y);
	}

	@Override
	protected List<Integer> getComponents() {
		return components;
	}

	@Override
	public int get(int index) {
		return super.getInt();
	}

	@Override
	protected Vector newVector(List<?> components) {
		return new Unit2((int) components.get(0), (int) components.get(1));
	}
}
