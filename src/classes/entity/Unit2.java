package classes.entity;

import java.util.ArrayList;
import java.util.List;

import classes.abstracts.Vector;

public class Unit2 extends Vector<Integer> {
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
	protected Vector<Integer> newVector(List<Integer> components) {
		return new Unit2(components.get(0), components.get(1));
	}
}
