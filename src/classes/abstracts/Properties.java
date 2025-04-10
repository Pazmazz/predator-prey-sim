package classes.abstracts;

import java.util.concurrent.ConcurrentHashMap;

public abstract class Properties {
	private ConcurrentHashMap<Property, Object> properties = new ConcurrentHashMap<>();

	public Object getProperty(Property property) {
		return properties.get(property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Property property, Class<T> classType) {
		Object result = properties.get(property);

		if (classType.isInstance(result) || result == null)
			return (T) result;

		throw new Error(String.format(
				"Bad call to getProperty(): Cannot cast %s to %s",
				result,
				classType));
	}

	public Properties setProperty(Property key, Object value) {
		properties.put(key, value);
		return this;
	}

	public ConcurrentHashMap<Property, Object> getProperties() {
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
