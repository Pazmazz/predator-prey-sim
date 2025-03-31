_**@Author:** [William J. Horn](https://github.com/william-horn)_

# _class_ `CellGrid`

The `CellGrid` class creates a virtual 2D grid, bounded by a given `<x, y>` dimension. Instead of implementing a 2D array, this grid uses a `HashMap` and maps `IntVector2` points to their corresponding `Cell` objects.

This choice was made for two reasons:

1. This allows `CellGrid` to store cell data in points that are off the grid, in cases where we may want cell data to exist outside of rendered or functional view. Such cells are marked with a special `CellType` called `OUT_OF_BOUNDS` that can be checked for.

2. It's more convenient to use the `HashMap` API to query the grid for a given cell than constantly indexing a 2D array.

## Getting Started

First, the following imports are needed to utilize this class:

- `IntVector2`

When ready, you can create a `CellGrid` instance by calling the constructor and passing the dimensions of the grid as an `IntVector2`:

```java
import classes.entity.IntVector2;

// Create a new 10x10 grid
CellGrid grid = new CellGrid(new IntVector2(10, 10));
```

By default, the virtual grid is empty and does not contain any points or `Cell` objects. `Cell` objects are only created when you query the virtual grid using `CellGrid.getCell()`

### `CellGrid.getCell()`

- **Parameters:** `<IntVector2 position>` - _The 2D position of the requested cell_

- **Returns:** `<Cell cell>`

This method will always return a `Cell` object, even if the queried cell position is empty or out of bounds. This is to provide metadata with each query, making it much easier for us to handle unsavory grid conditions.

#### Example:

```java
import classes.entity.IntVector2;
CellGrid grid = new CellGrid(new IntVector2(10, 10));

// Purposely try to get a cell out-of-bounds
Cell cell0 = grid.getCell(new IntVector2(-1, 0));

// Get a normal cell at position <3, 5>
Cell cell1 = grid.getCell(new IntVector2(3, 5));

// Print to console
Console.println(cell0);
Console.println(cell1);
```

#### Output:

<blockquote>
Cell<-1, 0>
<br/>
Cell<3, 5>
</blockquote>

Both cases return a `Cell` object even though `cell0` is out-of-bounds. To check for out-of-bounds cells, just use `Cell.isOutOfBounds()` or `Cell.getType()`. In cases where you want to check if the position is out of bounds before creating a `Cell` object, you can use `CellGrid.isInBounds()`

## `CellGrid.collectCell()`

- **Parameters:** `<IntVector2 position>` - _The 2D position of the cell to attempt to collect (free from memory)_

- **Returns:** `<Cell collectedCell>`

Since the cell grid is a HashMap, `Cell` objects will not be freed from memory if all references to them are lost. This is because the HashMap itself keeps the `Cell` referenced in memory, which can lead to a memory leak if not handled. Thus, `CellGrid.collectCell()` can be called to free up unused cells (cells with no `occupant` in them)

#### Example:

```java
// Figure 1.
Cell cell = grid.getCell(new IntVector2(0, 0));
Console.println(cell == grid.getCell(new IntVector2(0, 0))); // => true

// Figure 2.
grid.collectCell(cell);
Console.println(cell == grid.getCell(new IntVector2(0, 0))); // => false
```

Normally, `CellGrid.getCell()` will return the exact same `Cell` object when getting the same position, like in _Figure 1_. However, since `cell` has no occupant set and we call `CellGrid.collectCell()` in _Figure 2_ before printing the next comparison, `cell` has now been freed up and the next call to `CellGrid.getCell()` generates an entirely new `Cell` object.

As mentioned before, the cell will not be collected if it contains an `occupant`:

#### Example:

```java
import classes.entity.Ant;

// Figure 1.
Cell cell = grid.getCell(new IntVector2(0, 0));
cell.setOccupant(new Ant()); // Set an occupant
Console.println(cell == grid.getCell(new IntVector2(0, 0))); // => true

// Figure 2.
grid.collectCell(cell);
Console.println(cell == grid.getCell(new IntVector2(0, 0)) // => true
```

Now that an `occupant` was set, the cell will not be garbage collected.

## `CellGrid.isInBounds()`

- **Parameters:** `<IntVector2 position>` - _The 2D position of the cell to check_

- **Returns:** `<boolean isInBounds>`

#### Example:

```java
Console.println(grid.isInBounds(new IntVector2(-3, 0)));
Console.println(grid.isInBounds(new IntVector2(0, 0)));
```

#### Output:

<blockquote>
false
<br/>
true
</blockquote>

As with all `CellGrid` methods that take a `IntVector2` parameter (with the exception of `CellGrid.getCell()`), you can also pass a `Cell` object to this method:

#### Example:

```java
Cell cell0 = grid.getCell(new IntVector2(-3, 0));
Cell cell1 = grid.getCell(new IntVector2(0, 0));

Console.println(grid.isInBounds(cell0));
Console.println(grid.isInBounds(cell1));
```

#### Output:

<blockquote>
false
<br/>
true
</blockquote>
