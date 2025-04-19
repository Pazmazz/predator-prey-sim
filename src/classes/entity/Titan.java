package classes.entity;

import java.util.ArrayList;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.util.Console;
import classes.entity.CellGrid.Cell;

public class Titan extends Bug<Titan> {

	private Entity<?> target;
	private Game game = Game.getInstance();

	public Titan() {
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Titan");
	}

	@Override
	public void move() {
	}

	@Override
	public void breed() {
	}

	@Override
	public String toString() {
		return String.format(Console.filterConsoleColors(
				"$text-green Ant$text-reset #%s"),
				this.getId());
	}

	@Override
	public String serialize() {
		return "Titan{}";
	}

	@Override
	public Titan newInstance() {
		return new Titan();
	}
}
