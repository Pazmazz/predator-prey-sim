package test;

import classes.entity.Unit2;
import classes.entity.Vector2;
import java.util.HashMap;
import java.util.Map;

interface Property {

}

class Position implements Property {
	public int x;
	public int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Thing {
	private final Map<Class<? extends Property>, Property> properties = new HashMap<>();

	public <T extends Property> void setProperty(T property) {
		properties.put(property.getClass(), property);
	}

	public <T extends Property> T getProperty(Class<T> clazz) {
		return clazz.cast(properties.get(clazz));
	}

	public <T extends Property> boolean hasComponent(Class<T> clazz) {
		return properties.containsKey(clazz);
	}

	public <T extends Property> void removeComponent(Class<T> clazz) {
		properties.remove(clazz);
	}
}

public class Test {

	public static void thing() {

	}
}
