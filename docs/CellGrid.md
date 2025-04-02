_**@Author:** [William J. Horn](https://github.com/william-horn)_

# _class_ `CellGrid`

The `CellGrid` class creates a virtual 2D grid, bounded by a given `<x, y>` dimension. Instead of implementing a 2D array, this grid uses a `HashMap` and maps `Unit2` points to their corresponding `Cell` objects.

This choice was made for two reasons:

1. This allows `CellGrid` to store cell data in points that are off the grid, in cases where we may want cell data to exist outside of rendered or functional view. Such cells are marked with a special `CellType` called `OUT_OF_BOUNDS` that can be checked for.

2. It's more convenient to use the `HashMap` API to query the grid for a given cell than constantly indexing a 2D array.

## Getting Started

First, the following imports are needed to utilize this class:

- `Unit2`

When ready, you can create a `CellGrid` instance by calling the constructor and passing the dimensions of the grid as an `Unit2`:

```java
import classes.entity.Unit2;

// Create a new 10x10 grid
CellGrid grid = new CellGrid(new Unit2(10, 10));
```

By default, the virtual grid is empty and does not contain any points or `Cell` objects. `Cell` objects are only created when you query the virtual grid using `CellGrid.getCell()`

### `CellGrid.getCell()`

- **Parameters:** `<Unit2 position>` - _The 2D position of the requested cell_

- **Returns:** `<Cell cell>`

This method will always return a `Cell` object, even if the queried cell position is empty or out of bounds. This is to provide metadata with each query, making it much easier for us to handle unsavory grid conditions.

#### Example:

```java
import classes.entity.Unit2;
CellGrid grid = new CellGrid(new Unit2(10, 10));

// Purposely try to get a cell out-of-bounds
Cell cell0 = grid.getCell(new Unit2(-1, 0));

// Get a normal cell at position <3, 5>
Cell cell1 = grid.getCell(new Unit2(3, 5));

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

Both cases return a `Cell` object even though `cell0` is out-of-bounds. To check for out-of-bounds cells, just use `Cell.isOutOfBounds()` or `Cell.getType()`. In cases where you want to check if the position is out of bounds before creating a `Cell` object, you can use `CellGrid.isInBounds(Cell)` or `Cell.isInBounds()`.

### `CellGrid.collectCell()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to attempt to collect (free from memory)_
  - **Overload 2:** `<Cell cell>` - _The Cell object to collect_

- **Returns:** `<Cell collectedCell>`

Since the cell grid is a HashMap, `Cell` objects will not be freed from memory if all references to them are lost. This is because the HashMap itself keeps the `Cell` referenced in memory, which can lead to a memory leak if not handled. Thus, `CellGrid.collectCell()` can be called to free up unused cells (cells with no `occupant` in them)

#### Example:

```java
// Figure 1.
Cell cell = grid.getCell(new Unit2(0, 0));
Console.println(cell == grid.getCell(new Unit2(0, 0))); // => true

// Figure 2.
grid.collectCell(cell);
Console.println(cell == grid.getCell(new Unit2(0, 0))); // => false
```

Normally, `CellGrid.getCell()` will return the exact same `Cell` object when getting the same position, like in _Figure 1_. However, since `cell` has no occupant set and we call `CellGrid.collectCell()` in _Figure 2_ before printing the next comparison, `cell` has now been freed up and the next call to `CellGrid.getCell()` generates an entirely new `Cell` object.

As mentioned before, the cell will not be collected if it contains an `occupant`:

#### Example:

```java
import classes.entity.Ant;

// Figure 1.
Cell cell = grid.getCell(new Unit2(0, 0));
cell.setOccupant(new Ant()); // Set an occupant
Console.println(cell == grid.getCell(new Unit2(0, 0))); // => true

// Figure 2.
grid.collectCell(cell);
Console.println(cell == grid.getCell(new Unit2(0, 0)) // => true
```

Now that an `occupant` was set, the cell will not be garbage collected. If you want to collect _all_ collectable cells, you can use `CellGrid.collectCells()`

### `CellGrid.collectCells()`

- **Parameters:** _None_

- **Returns:** `void`

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(0, 0));
Cell cell1 = grid.getCell(new Unit2(0, 1));
Cell cell2 = grid.getCell(new Unit2(0, 2));

cell0.setOccupant(new Ant());

grid.collectCells(); // Frees all cells except for cell0
```

### `CellGrid.isInBounds()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to check_
  - **Overload 2:** `<Cell cell>` - _The Cell object to check_

- **Returns:** `<boolean isInBounds>`

Determines if a given `Cell` or `Unit2` is within the `CellGrid` dimensions specified in the constructor.

#### Example:

```java
Console.println(grid.isInBounds(new Unit2(-3, 0)));
Console.println(grid.isInBounds(new Unit2(0, 0)));
```

#### Output:

<blockquote>
false
<br/>
true
</blockquote>

As with all `CellGrid` methods that take a `Unit2` parameter (with the exception of `CellGrid.getCell()`), you can also pass a `Cell` object to this method:

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(-3, 0));
Cell cell1 = grid.getCell(new Unit2(0, 0));

Console.println(grid.isInBounds(cell0));
Console.println(grid.isInBounds(cell1));
```

#### Output:

<blockquote>
false
<br/>
true
</blockquote>

### `CellGrid.getCellTopOf()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to find the cell above it_
  - **Overload 2:** `<Cell cell>` - _The Cell object to look above_

- **Returns:** `<Cell cell>`

Returns the `Cell` object that exists above a specified cell.

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(5, 5));
Cell cell1 = grid.getCell(new Unit2(5, 4));

Console.println(grid.getCellTopOf(cell1)); // => Cell<5, 5>
```

### `CellGrid.getCellBottomOf()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to find the cell below it_
  - **Overload 2:** `<Cell cell>` - _The Cell object to look below_

- **Returns:** `<Cell cell>`

Returns the `Cell` object that exists below a specified cell.

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(3, 6));
Cell cell1 = grid.getCell(new Unit2(3, 7));

Console.println(grid.getCellBottomOf(cell0)); // => Cell<3, 7>
```

### `CellGrid.getCellLeftOf()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to find the cell left of it_
  - **Overload 2:** `<Cell cell>` - _The Cell object to look left from_

- **Returns:** `<Cell cell>`

Returns the `Cell` object that exists left of a specified cell.

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(2, 2));
Cell cell1 = grid.getCell(new Unit2(1, 2));

Console.println(grid.getCellLeftOf(cell0)); // => Cell<1, 2>
```

### `CellGrid.getCellRightOf()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to find the cell right of it_
  - **Overload 2:** `<Cell cell>` - _The Cell object to look right from_

- **Returns:** `<Cell cell>`

Returns the `Cell` object that exists right of a specified cell.

#### Example:

```java
Cell cell0 = grid.getCell(new Unit2(8, 5));
Cell cell1 = grid.getCell(new Unit2(9, 5));

Console.println(grid.getCellLeftOf(cell0)); // => Cell<9, 5>
```

### `CellGrid.getCellsAdjacentTo()`

- **Parameters:**

  - **Overload 1:** `<Unit2 position>` - _The 2D position of the cell to return the adjacent cells of_
  - **Overload 2:** `<Cell cell>` - _The Cell object to get the adjacent cells of_

- **Returns:** `<Cell[] cells>`

Returns the `Cell` object that exists left to a specified cell.

#### Example:

```java
Cell cell = grid.getCell(new Unit2(0, 0));

Cell[] adjCells = grid.getCellsAdjacentTo(cell);

for (Cell adjCell : adjCells) {
	Console.println(adjCell);
	Console.println(adjCell.getType());
}
```

<blockquote>
Cell<-1, 0>
<br/>
OUT_OF_BOUNDS

<br/>
Cell<1, 0>
<br/>
NORMAL

<br/>
Cell<0, 1>
<br/>
NORMAL

<br/>
Cell<0, -1>
<br/>
OUT_OF_BOUNDS
</blockquote>
