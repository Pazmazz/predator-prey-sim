# Dev log

Updates shared between developers of this project

## Version 0.3.3
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

## Version 0.3.2
##### [William J. Horn](https://github.com/william-horn) - *4/10/2025*

Minor changes. 

* **Removed** issue where `$text-color` tags still showed up in text printed to the console even if `consoleColorsEnabled` was set to `false`.

	* **Solution:** Passed text through a new static `Console` method called `withConsoleColors`, which will remove stray text color tags if `consoleColorsEnabled` is `false`.

* **Fixed** issue with `Console.println` throwing an error when printing a `null` value.
	* **Solution:** The issue was actually with the `Formatter.concatArray` method not checking for null entries inside of the `Object[]` parameter when iterating over it. This has now been accounted for.

## Version 0.2.2
##### [William J. Horn](https://github.com/william-horn) - *4/10/2025*

Major changes.

* **Changed** `CellOccupant` class name to [`Entity`](../src/classes/abstracts/Entity.java), and converted it to an `abstract` class. 

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
	