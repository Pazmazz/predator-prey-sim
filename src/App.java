/*
 * @author Alex, Grier, Jaylen, Will
 * @written 3/28/2025
 */

import classes.abstracts.Application;
import classes.entity.Game;
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
        game.start();
        /**
         * TEST CODE:
         */
        // Vector2 v0 = new Vector2(7, 3);
        // Vector2 v1 = new Vector2(-1, 1);
        // CellGrid grid = new CellGrid(new Unit2(10, 10));

        // Iterator<Cell> itr = grid.getCellPathIterator(v0, v1);
        // while (itr.hasNext()) {
        //     Console.println(itr.next());
        //     Console.br();
        // }
    }
}
