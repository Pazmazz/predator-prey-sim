package classes.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Time;
import interfaces.Property;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.ImageSet;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Ant extends Bug<Ant> {
	final private static HashMap<Ant, Ant> ants = new HashMap<>();
	final private Game game = Game.getInstance();

	public Ant() {
		this.setAvatar(ImageSet.BASE_ANT);
		this.setEatable(true);
		this.setMovementSpeed(game.getSettings().getAntMovementSpeed());
		this.getBreedingMeter().setMax(3);

		// properties
		if (game.getSettings().getAntBreedingEnabled())
			this.getBreedingMeter().onMaxValueReached.connect(e -> this.breed());
	}

	@Override
	public boolean move() {
		ValueMeter breedingMeter = this.getBreedingMeter();
		CellGrid grid = game.getState().getGameGrid();
		ArrayList<Cell> adjCells = grid.getCellsAdjacentTo(this.getAssignedCell());
		Cell randCell = grid.getRandomAvailableCellFrom(adjCells);
		boolean moved = breedingMeter.increment() == breedingMeter.getMax();

		if (randCell != null) {
			this.assignCell(randCell);
			return true;
		}
		return moved;
	}

	@Override
	public String toString() {
		return String.format(Console.filterConsoleColors(
				"$text-green Ant$text-reset #%s"),
				getId());
	}

	@Override
	public String serialize() {
		return "Ant{}";
	}

	@Override
	public Ant newInstance() {
		return new Ant();
	}

	@Override
	public String getTooltipString() {
		Cell cell = this.getAssignedCell();
		Unit2 unit = cell.getUnit2();
		return new StringBuilder(this.getNameWithId())
				.append("<span style='font-size:10px;color:white;'>")
				.append("<br>Time alive: <span style='color:#bf00ff;'>")
				.append(Time.formatTime(this.getTimeInSimulationInSeconds()))
				.append("</span><br>Generation: <span style='color:#bf00ff;'>")
				.append(this.getGenerationMeter().getValue())
				.append("</span><br>Cell: <span style='color:#44D0FF;'>Cell&lt;")
				.append(unit.getX())
				.append(", ")
				.append(unit.getY())
				.append(">")
				.append("</span>")
				.toString();
	}

	@Override
	public void removeFromCell() {
		super.removeFromCell();
		ants.remove(this);
	}
}
