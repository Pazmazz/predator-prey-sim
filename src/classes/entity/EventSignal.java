package classes.entity;

import java.util.ArrayList;

import interfaces.OneWayCallback;

public class EventSignal {
	final private ArrayList<OneWayCallback> connections = new ArrayList<>();
	final private ArrayList<Object[]> queuedEventDispatches = new ArrayList<>();
	private boolean dispatchQueueEnabled = false;

	// TODO: Add documentation
	public void connect(OneWayCallback handler) {
		if (this.dispatchQueueEnabled) {
			for (Object[] data : this.queuedEventDispatches) {
				handler.call(data);
			}
		}
		connections.add(handler);
	}

	public void fire(Object... args) {
		for (OneWayCallback handler : connections) {
			handler.call(args);
		}
		if (this.dispatchQueueEnabled) {
			this.queuedEventDispatches.add(args);
			if (this.queuedEventDispatches.size() > 1000) {
				throw new Error(
						"WARNING: DISPATCH QUEUE EXCEEDED 1000 REQUESTS - MAKE SURE TO DISABLE QUEUEING WHEN NO LONGER NEEDED, OR DISABLE THIS ERROR MESSAGE IF THIS WAS INTENTIONAL");
			}
		}
	}

	public void clearDispatchQueue() {
		this.queuedEventDispatches.clear();
	}

	/**
	 * Enables or disables dispatch queueing.
	 * 
	 * <p>
	 * Dispatch queueing is when an event signal is dispatched before a connection
	 * is established. If this case, normally the connected handler will just be
	 * ignored since the event was dispatched before it's connection. However, if
	 * {@code dispatchQueueEnabled} is {@code true}, then the handler inside
	 * {@code connect} will immediately execute all previous dispatches with their
	 * respective callback data. This is a powerful feature, but when dispatch
	 * queueing is no longer needed, it MUST be disabled to prevent all subsequent
	 * dispatches from queueing infinitely.
	 * 
	 * @param enabled {@code true} to enable dispatch queueing, {@code false}
	 *                otherwise.
	 */
	public void setDispatchQueueEnabled(boolean enabled) {
		this.dispatchQueueEnabled = enabled;
		if (!enabled) {
			this.clearDispatchQueue();
		}
	}

	public ArrayList<OneWayCallback> getConnections() {
		return this.connections;
	}
}
