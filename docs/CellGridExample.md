# CellGrid Example:

Here is an example of creating a CellGrid, placing an occupant in a cell, and moving that occupant around from cell to cell.

## Step 1: Create the `CellGrid` and place an occupant in a cell

```java
CellGrid grid = new CellGrid(new IntVector2(10, 10));
Cell cell = grid.getCell(new IntVector2(4, 4));

cell.setOccupant(new Ant());
```

## Step 2: Create a loop that moves the ant around randomly every second

```java
while (true) {
	// Get adjacent cells to the current ant cell
	Cell[] adjCells = cell.getCellsAdjacentTo(cell);

	for (Cell adjCell : adjCells) {
		if (adjCell.isInBounds() && adjCell.isEmpty()) {
			cell.moveOccupantTo(adjCell);
			cell = adjCell;
			break;
		}
	}

	wait(1000); // Wait one second
}
```
