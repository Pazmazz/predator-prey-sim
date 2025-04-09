/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import classes.abstracts.Bug;
import classes.abstracts.FrameProcessor;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.CellOccupant;
import classes.entity.Game;
import classes.entity.Properties;
import classes.entity.TweenData;
import classes.entity.Properties.Property;
import classes.entity.Unit2;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Math2;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Movement step in the simulation should be
 * written in this class's step method.
 * 
 * This frame can be thought of as a "game state update" process. It's goal is
 * to update all game state variables in the data model.
 */
public class MovementFrame extends FrameProcessor {

	private CellGrid grid = game.getGameGrid();
	private HashMap<CellOccupant, HashMap<Property, TweenData>> propertyBuffer = new HashMap<>();

	public MovementFrame(Game game, SimulationType simulationFrame) {
		super(game, simulationFrame);
	}

	public void bufferProperties(CellOccupant entity, Properties newProperties) {

	}

	@Override
	public void step(double deltaTimeSeconds) {
		if (propertyBuffer.isEmpty())
			return;

		Iterator<Entry<CellOccupant, HashMap<Property, TweenData>>> bufferIterator = propertyBuffer
				.entrySet()
				.iterator();

		while (bufferIterator.hasNext()) {
			Entry<CellOccupant, HashMap<Property, TweenData>> bufferEntry = bufferIterator.next();
			CellOccupant entity = bufferEntry.getKey();
			Properties entityProperties = entity.getProperties();
			HashMap<Property, TweenData> tweeningProperties = bufferEntry.getValue();

			Iterator<Entry<Property, TweenData>> tweenIterator = tweeningProperties
					.entrySet()
					.iterator();

			while (tweenIterator.hasNext()) {
				Entry<Property, TweenData> tweenEntry = tweenIterator.next();
				TweenData tweenData = tweenEntry.getValue();
				Property property = tweenEntry.getKey();

				if (tweenData.getStartTime() == -1)
					tweenData.setStartTime(timeBeforeStep());

				long elapsedTime = timeBeforeStep() - tweenData.getStartTime();
				double alpha = elapsedTime / tweenData.getDuration();

				if (tweenData.getStartValue() instanceof Number) {
					entityProperties.set(
							property,
							Math2.lerp((double) tweenData.getStartValue(), (double) tweenData.getEndValue(), alpha));
				}
			}
		}
	}
}
