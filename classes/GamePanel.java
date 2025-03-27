package classes;

import javax.swing.JPanel;
import java.awt.Dimension;

public class GamePanel extends JPanel {
	final int unitTileSize = 16;
	final int tileScale = 3;
	final int tileSize = unitTileSize * tileScale;

	final int screenTilesX = 20;
	final int screenTilesY = 20;
	final int screenWidth = screenTilesX * tileSize;
	final int screenHeight = screenTilesY * tileSize;

	public GamePanel() {
		this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
	}
}
