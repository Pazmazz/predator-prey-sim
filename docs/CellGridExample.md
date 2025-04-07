# CellGrid Example:

Here is an example of creating a CellGrid, placing an occupant in a cell, and moving that occupant around from cell to cell.

## Step 1: Create the `CellGrid` and place an occupant in a cell

```java
CellGrid grid = new CellGrid(new Unit2(10, 10));
Cell cell = grid.getCell(new Unit2(4, 4));

cell.setOccupant(new Ant());
```

## Step 2: Create a loop that moves the ant around randomly every second

```java
CellGrid grid = new CellGrid(new Unit2(10, 10));
Cell cell = grid.getCell(new Unit2(4, 4));

// Assign an ant to a cell
Ant ant0 = new Ant();
ant0.assignCell(grid.getCell(1, 1));

while (true) {
	// Get adjacent cells to the current ant cell
	Cell[] adjCells = grid.getCellsAdjacentTo(ant0.getCell());

	// Add some code here to randomly shuffle the elements in the adjacent cell array
	// ...

	/*
	 * Iterate over cell array and stop at the first available
	 * cell that can hold an occupant
	 */
	for (Cell adjCell : adjCells) {
		if (adjCell.isInBounds() && adjCell.isEmpty()) {
			// Deprecated method:
			// cell.moveOccupantTo(adjCell);
			// cell = adjCell;

			// Do this instead now:
			ant0.assignCell(adjCell);

			// This is also valid
			ant0.removeCell();
			adjCell.setOccupant(ant0);

			// Exit the loop once the cell is assigned
			break;
		}
	}

	wait(1000); // Wait one second
}
```
