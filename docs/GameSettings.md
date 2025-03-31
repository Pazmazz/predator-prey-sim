_**@Author:** [William J. Horn](https://github.com/william-horn)_

# _class_ `GameSettings`

The `GameSettings` class serves as a semi-singleton class, in that only one instance of it is created per `Game` instance. It holds most configurable data about the application and it is **accessible** and **mutable** during runtime.

## Getting Started

This class only has one no-arg constructor, and thus can be created simply as:

```java
GameSettings settings = new GameSettings();
```

## API

### `settings.getTitle()`

Returns the title of the game that displays in the application window

- **Return type:** `String`

### `settings.getHeaderText()`

Returns the text that gets displayed in the main header of the application's content window

- **Return type:** `String`

### `settings.getCellSize()`

Returns the number in square pixels of the size of each cell displayed in the simulation grid.

- **Return type:** `int`

### `settings.getScreenWidth()`

Returns the number in pixels of the horizontal size of the application window

- **Return type:** `int`

### `settings.getScreenHeight()`

Returns the number in pixels of the vertical size of the application window

- **Return type:** `int`

### `settings.getSimulation()`

Returns a singleton instance of the `Simulation` class, which itself holds methods for further inquiring simulation sub-settings.

- **Return type:** `Simulation`
