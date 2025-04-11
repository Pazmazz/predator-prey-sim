package classes.abstracts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import classes.entity.Null;

public abstract class Properties {
	private Map<Property, Object> properties = new ConcurrentHashMap<>();

	public Object getProperty(Property property) {
		return properties.get(property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Property property, Class<T> classType) {
		Object result = properties.get(property);

		if (result instanceof Null)
			return null;

		if (classType.isInstance(result))
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

	public Map<Property, Object> getProperties() {
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
