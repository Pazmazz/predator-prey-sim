package classes.entity;

public class CellOccupant {
	private boolean isEatable = false;
	private Cell currentCell;

	public CellOccupant() {

	}
	
	public void setEatable(boolean eatable) {
		this.isEatable = eatable;
	}

	public boolean isEatable() {
		return this.isEatable;
	}

	public void setCell(Cell targetCell, boolean updateCell) {
		this.currentCell = targetCell;
		if (updateCell) targetCell.setOccupant(this, false);
	}
	
	public void setCell(Cell targetCell) {
		setCell(targetCell, true);
	}

	public Cell getCell() {
		return this.currentCell;
	}

	public boolean hasCell() {
		return this.currentCell != null;
	}
}
