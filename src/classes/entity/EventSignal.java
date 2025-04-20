package classes.entity;

import java.util.ArrayList;

import interfaces.OneWayCallback;

public class EventSignal {
	final private ArrayList<OneWayCallback> connections = new ArrayList<>();

	public void connect(OneWayCallback handler) {
		connections.add(handler);
	}

	public void fire(Object... args) {
		for (OneWayCallback handler : connections) {
			handler.call(args);
		}
	}
}
