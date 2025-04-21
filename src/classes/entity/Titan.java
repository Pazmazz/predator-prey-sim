package classes.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.util.Console;
import classes.entity.CellGrid.Cell;
import classes.entity.GameScreen.IMAGE;

public class Titan extends Bug<Titan> {

	public IMAGE avatar = IMAGE.BASE_TITAN;

	private Entity<?> target;
	private Game game = Game.getInstance();

	public Titan() {
		setProperty(Property.IS_EATABLE, false);
		setProperty(Property.VARIANT, "Titan");
	}

	@Override
	public boolean move() {
		return true;
	}

	@Override
	public void breed(Turn turn) {
	}

	@Override
	public String toString() {
		return String.format(Console.filterConsoleColors(
				"$text-red Titan$text-reset #%s"),
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

	@Override
	public IMAGE getAvatar() {
		return this.avatar;
	}

	@Override
	public void setAvatar(IMAGE avatar) {
		this.avatar = avatar;
	}
}
