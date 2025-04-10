package classes.abstracts;

import java.util.HashMap;

public abstract class Properties {
	private HashMap<Property, Object> properties = new HashMap<>();

	public Object getProperty(Property property) {
		return properties.get(property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Property property, Class<T> classType) {
		Object result = properties.get(property);

		if (classType.isInstance(result))
			return (T) result;

		throw new Error(String.format(
				"Cannot cast %s to %s",
				result.getClass(),
				classType));
	}

	public Properties setProperty(Property key, Object value) {
		properties.put(key, value);
		return this;
	}

	public HashMap<Property, Object> getProperties() {
		return this.properties;
	}

	public enum Property {
		POSITION,
		ROTATION,
		MOVEMENT_SPEED,
		IS_EATABLE,
		ASSIGNED_CELL,
	}
}
