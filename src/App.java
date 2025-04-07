/*
 * @author Alex, Grier, Jaylen, Will
 * @written 3/28/2025
 */

import classes.abstracts.Application;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;

/**
 * The entry-point file for the application
 */
public class App extends Application {

    public static void main(String[] args) {
        Console.setDebugModeEnabled(true);
        // Console.hideDebugPriority(DebugPriority.LOW);
        Console.setConsoleColorsEnabled(true);

        Game game = new Game();
        // game.start();
        //
        // TEST CODE:
        //
        CellGrid grid = game.getGameGrid();

        Cell cell0 = grid.getCell(new Unit2(1, 1));
        Cell cell1 = grid.getCell(new Unit2(1, 2));
				cell1.setOccupant(new Ant());

        Console.println(grid.getCellTopOf(cell0).getOccupant());
				Console.println(grid.getCellRightOf(cell0));

				Console.println(grid.getCellLeftOf(cell0));
				

        // cell0.moveOccupantTo(cell1);
    }
}
