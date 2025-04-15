package interfaces;

import java.util.HashMap;

public interface Serializable<T> {
	T construct(HashMap<String, Object> data);

	String serialize();
}
