# Dev log

Updates shared between developers of this project

## Version 0.3.4
##### [William J. Horn](https://github.com/william-horn) - *4/11/2025*

Minor changes.

* **Added** [`Task`](../src/classes/abstracts/FrameProcessor.java) class back, along with the task scheduler integrated into the [`FrameProcessor`](../src/classes/abstracts/FrameProcessor.java)

	* It was added back due to it now being useful for utilizing the game loop to increment move steps.

* **Removed** the `Application` class.

	* It was not being used.

* **Added** more grid-querying methods for getting random cells, available cells, or both. Such methods include:
	* [`grid.getCells()`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L607)

		- *returns all `Cell` objects on the grid as an `ArrayList<Cell>` object*
	* [`grid.getAvailableCells()`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L749)

		- *returns all available (non-occupied and in-bounds) cells on the virtual grid.*
	* [`grid.getRandomCell()`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L759)
		- *returns a random `Cell` object on the virtual grid (does not check if it's available)*

	* [`grid.getRandomCells()`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L782)
		- *returns all `Cell` objects on the grid in random order as an `ArrayList<Cell>` object*

	* [`grid.getRandomAvailableCell()`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L770)
		- *returns a random available `Cell` object on the virtual grid only if it is available (non-occupied and in-bounds)*

	* [`grid.getRandomCells(amount)`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L795)
		- *returns an `ArrayList<Cell>` of random `Cell` objects on the grid, quantified by the `amount` parameter.*

	* [`grid.getRandomCellsFrom(ArrayList<Cell>, amount)`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L697)
		- *returns an `ArrayList<Cell>` of random `Cell` objects from a provided `ArrayList<Cell>` of `Cell` objects.*

	* [`grid.getRandomCellFrom(ArrayList<Cell>)`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L640)
		- *returns a random cell from a provided array list of `Cell` objects (does not check it's available)*

	* [`grid.getAvailableCellsFrom(ArrayList<Cell>)`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L620)
		- *returns all available (non-occupied and in-bounds) cells from a provided array list of `Cell` objects.*
		
	* [`grid.getRandomAvailableCellsFrom(ArrayList<Cell>, amount)`](https://github.com/Pazmazz/predator-prey-sim/blob/main/src/classes/entity/CellGrid.java#L719)
		- *returns an `ArrayList<Cell>` of random available (non-occupied and in-bounds) `Cell` objects within a provided `ArrayList<Cell>`, quantified by the `amount` parameter*
	

* **Changed** method `grid.getAdjacentCells()` to return `ArrayList<Cell>` instead of `Cell[]`.

	* This was so we can utilize the dynamic functionality of the `List` API for slightly more control over generating or iterating over cell lists.

	* This change also applies to derivatives of `getAdjacentCells()`, such as:
		- [`grid.getAvailableCells()`](../src/classes/entity/CellGrid.java)
		- [`grid.getAvailableCellsFrom()`](../src/classes/entity/CellGrid.java)


## Version 0.3.3
##### [William J. Horn](https://github.com/william-horn) - *4/10/2025*

Major changes.

* **Changed** `CellOccupant` class name to [`Entity`](../src/classes/abstracts/Entity.java), and converted it back to an `abstract` class. 

	* This is because `Entity` is a more general term, and it implies that we can extend this class to create many other different types of game objects instead of just something that occupies a cell.

	* This class also now extends [`Properties`](../src/classes/abstracts/Properties.java)

	* **Classes effected by this change:**
		- [Bug](../src/classes/abstracts/Bug.java)
		- [Ant](../src/classes/entity/Ant.java)
		- [Doodlebug](../src/classes/entity/Doodlebug.java)
		- [Cell](../src/classes/entity/Cell.java)

* **Created** [`Properties`](../src/classes/abstracts/Properties.java) class.

	* This is an `abstract` class which is meant to be extended when you want the inheriting class to have access to methods such as `getProperty()` and `setProperty()` for grouping dynamic object data.

* **Created** [`TweenData`](../src/classes/entity/TweenData.java) class. 

	* This is a concrete class that will be used for creating tween animations, and is still in early development.

* **Removed** `TaskScheduler` class until it is needed.

	* **Reason:** It just resulted in more bloated code, and it is a feature we don't need right now.

* **Changed** children of [`Bug`](../src/classes/abstracts/Bug.java) class to now require a `game` parameter to be passed to the constructor. This means stuff like this is no longer valid:

	```java 
	Ant ant0 = new Ant();
	```

	Instead, the `game` object *must* be passed as the first parameter, like this:

	```java
	Ant ant0 = new Ant(game);
	```

	This is because these classes need to reference the game's cell grid, and they can't do so without access to the `game` object. This also gives them the freedom of interacting with the rest of the game state if they need to.

* **Deprecated** `Entity.removeCell()` method now in favor of `Entity.removeFromCell()`. 

	* This just makes more linguistic sense, since the entity inside the cell is just removing itself *from* the cell, it's not removing the cell itself.
	

## Version 0.2.3
##### [William J. Horn](https://github.com/william-horn) - *4/10/2025*

Minor changes.

* **Added** snapshot feature to the `CellGrid` class.

	* Allows us to go back in time during the simulation to earlier game phases. After each game movement, a "snapshot" of the game grid is taken, saving it in history up until a certain threshold.

* **Added** [`Null`](../src/classes/entity/Null.java) class.

	* Used as a `null` property value, since `ConcurrentHashMap` can not store `null` keys or values.

		Ex, we can't do this:

		```java
		setProperty(Property.POSITION, null); // -> throws an error
		```

		But we can now do this:

		```java
		setProperty(Property.POSITION, new Null()); // -> returns null
		```

## Version 0.2.2
##### [William J. Horn](https://github.com/william-horn) - *4/10/2025*

Minor changes. 

* **Removed** issue where `$text-color` tags still showed up in text printed to the console even if `consoleColorsEnabled` was set to `false`.

	* **Solution:** Passed text through a new static `Console` method called `withConsoleColors`, which will remove stray text color tags if `consoleColorsEnabled` is `false`.

* **Fixed** issue with `Console.println` throwing an error when printing a `null` value.
	* **Solution:** The issue was actually with the `Formatter.concatArray` method not checking for null entries inside of the `Object[]` parameter when iterating over it. This has now been accounted for.
