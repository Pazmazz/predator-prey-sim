package classes.entity;

import classes.entity.Game.SimulationState;

public class GameState implements Cloneable {
	private String serializedGameGrid;

	private SimulationState simulationState;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
