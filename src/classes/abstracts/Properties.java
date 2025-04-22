package classes.abstracts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import classes.entity.CellGrid.Cell;
import classes.entity.ValueMeter;
import classes.entity.Vector2;
import interfaces.Property;
import interfaces.Serializable;

/*
 * absoluteAvatarPosition:
 * The rendered entity's avatar image position relative to it's parent screen
 * 
 * avatarPositionOffset:
 * The rendered entity's avatar image position relative to it's cell
 */

public abstract class Properties implements Serializable {
	private Map<Property, Object> properties = new ConcurrentHashMap<>();

	// public T getProperty(Property property) {
	// Object result = properties.get(property);

	// }

	public Properties setProperty(Property key, Object value) {
		properties.put(key, value);
		return this;
	}

	public Map<Property, Object> getProperties() {
		return this.properties;
	}

	// public enum Property {
	// POSITION_2D(Vector2.class),
	// ROTATION_RADIANS(Double.class),
	// MOVEMENT_CELLS_PER_SECOND(Double.class),
	// IS_EATABLE(Boolean.class),
	// ASSIGNED_CELL(Cell.class),
	// NAME(String.class),
	// // TYPE,
	// // VARIANT,
	// // HEALTH,
	// // MAX_HEALTH,
	// // MAX_HUNGER,
	// // HUNGER,
	// HEALTH_METER(ValueMeter.class),
	// HUNGER_METER(ValueMeter.class),
	// MOVEMENT_METER(ValueMeter.class),
	// ANTS_EATEN(Integer.class);

	// private Class<?> type;

	// private Property(Class<?> type) {
	// this.type = type;
	// }

	// public Class<?> getType() {
	// return this.type;
	// }
	// }

	private abstract class Property {

	}

	public class Position2D extends Property {

	}

	public class RotationRadians extends Property {

	}
}
