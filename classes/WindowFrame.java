package classes;

import javax.swing.JFrame;

public class WindowFrame extends JFrame {
	public WindowFrame(String title) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle(title);
		this.setVisible(true);
	}

	public WindowFrame() {
		this("Hunting with Hanson");
	}
}
