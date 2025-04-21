package classes.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import classes.abstracts.Bug;
import classes.util.Console;
import classes.util.Time;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.IMAGE;
import classes.settings.GameSettings;

@SuppressWarnings("unused")
public class Ant extends Bug<Ant> {

	public IMAGE avatar = IMAGE.BASE_ANT;

	final private Game game = Game.getInstance();
	final private CellGrid gameGrid = game.getGameGrid();
	final private GameSettings settings = game.getSettings();

	public Ant() {
		// properties

		this.setProperty(Property.IS_EATABLE, true);
		this.setProperty(Property.MOVEMENT_COOLDOWN, settings.getAntMovementCooldown());

		if (settings.getAntBreedingEnabled())
			this.getBreedingMeter().onMaxValueReached.connect(e -> breed());
	}

	@Override
	public boolean move() {
		boolean moved = this.getBreedingMeter().increment() == this.getBreedingMeter().getMax();
		ArrayList<Cell> adjCells = gameGrid.getCellsAdjacentTo(getCell());
		Cell randCell = gameGrid.getRandomAvailableCellFrom(adjCells);

		if (randCell != null) {
			double angle = (randCell.getUnit2Center().subtract(getCell().getUnit2Center())).screenAngle();
			setRotation(angle);
			assignCell(randCell);
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
	public IMAGE getAvatar() {
		return this.avatar;
	}

	@Override
	public void setAvatar(IMAGE avatar) {
		this.avatar = avatar;
	}

	@Override
	public String getTooltipString() {
		Cell cell = this.getCell();
		Unit2 unit = cell.getUnit2();
		return new StringBuilder(this.getNameWithId())
				.append("<span style='font-size:10px;color:white;'>")
				.append("<br>Time alive: <span style='color:#bf00ff;'>")
				.append(Time.formatTime(this.getTimeInSimulationInSeconds()))
				.append("</span><br>Generation: <span style='color:#bf00ff;'>")
				.append(this.getGeneration())
				.append("</span><br>Cell: <span style='color:#44D0FF;'>Cell&lt;")
				.append(unit.getX())
				.append(", ")
				.append(unit.getY())
				.append(">")
				.append("</span>")
				.toString();
	}
}
