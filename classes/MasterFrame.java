/*
 * @Author(s):
 * @Written: 3/28/2025
 * 
 * class MasterFrame:
 * 
 * An extension of the JPanel class with default settings applied.
 * This class is responsible for creating a full-screen-sized default 
 * container frame with a paint buffer right out the box.
 */

package classes;

import javax.swing.JPanel;
import java.awt.Dimension;

public class MasterFrame extends JPanel {
	public MasterFrame() {
		this.setDoubleBuffered(true);
		this.setPreferredSize(
			new Dimension(
				GameSettings.SCREEN_WIDTH, 
				GameSettings.SCREEN_HEIGHT
			)
		);
	}
}
