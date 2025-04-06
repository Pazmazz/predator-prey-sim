_**@Author:** [William J. Horn](https://github.com/william-horn)_

# _class_ `Game`

The `Game` class is responsible for creating a fully independant instance of the application. It will instantiate it's own main thread, create a mutable copy of `GameSettings`, and harbor an internal state independant of other game instances.

## Getting Started

A new game instance can be created by simply calling the class constructor:

```java
Game game = new Game();
```

All other modifications such as changing game settings, pausing/resuming the game loop, or reading the game state, is now handled directly from the `game` object.

When the game object is created, a window will immediately spawn and the GUI will be assembled and rendered. In the future, we will most likely change this so that nothing is rendered until a method is called to initiate the rendering. However, the game loop will not start automatically. In order to begin running the game, you must call the `start()` method:

```java
Game game = new Game();
game.start(); // Start the game loop
```

## Game State

Before `game.start()` is called, the `game` object goes through 2 game states upon instantiation. The first state is the default value assigned as `GameState.INITIAL`.

After the constructor has finished executing, the internal game state will then immediately update to `GameState.LOADED`. The game state is immediately set to `GameState.RUNNING` as soon as `game.start()` is called.

You can access the current state of the game with the following methods:

### `game.isInitial()`

Returns **true** if the game state is `GameState.INITIAL`

### `game.isLoaded()`

Returns **true** is the game state is `GameState.LOADED`

### `gameState.isRunning()`

Returns **true** if the game state is `GameState.RUNNING`

### `gameState.isPaused()`

Returns **true** if the game state is `GameState.PAUSED`

### `gameState.isThreadRunning()`

Returns **true** if the game loop is still alive (if the game is in `GameState.RUNNING` **or** `GameState.PAUSED` state)

### `gameState.isTerminated()`

Returns **true** if the game state is `GameStart.TERMINATED` (happens when game is closing)

## API

These are all of the current ways to interact with the `game` object through the API:

### `game.start()`

Starts the game by running the main game loop. Also changes game state from `GameState.LOADED` to `GameState.RUNNING`

### `game.getSessionId()`

Returns a `String` containing a **UUID** that represents a unique identifier for the current game instance

### `game.getSettings()`

Returns an instance of the `GameSettings` class that the `game` object initialized upon instantiation.

### `game.getScreen()`

Returns the game window GUI.

### `game.terminate()`

Stops the game loop and terminates the game application.
