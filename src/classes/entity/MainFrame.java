package classes.entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

//ineffective
// class ImagePanel extends JPanel{
//     private Image backgroundImage;
//     public ImagePanel(String filename){
//         backgroundImage = new ImageIcon("GridSpace1").getImage();
//     }
//     @Override
//     protected void paintComponent(Graphics g){
//         super.paintComponent(g);
//         //Drawing image to fill the panel
//         g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
//     }
// }
//ineffective
public class MainFrame {

	private Game game = Game.getInstance();

	public MainFrame() {
		// Main frame
		JFrame frame = new JFrame("Predator Prey Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 1000);
		frame.setLayout(new BorderLayout());

		// Title panel with Label
		JPanel title_panel = new JPanel();
		JLabel textfield = new JLabel();

		frame.getContentPane().setBackground(new Color(50, 50, 50));

		frame.setVisible(true);

		textfield.setBackground(new Color(25, 25, 25));
		textfield.setForeground(new Color(25, 255, 0));
		textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setText("Predator Prey Simulation");
		textfield.setOpaque(true);

		title_panel.setLayout(new BorderLayout());
		title_panel.setPreferredSize(new Dimension(900, 80));
		title_panel.add(textfield, BorderLayout.CENTER);
		// Grid
		JPanel gridPanel = new JPanel();

		gridPanel.setLayout(new GridLayout(20, 20));

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				try {
					BufferedImage image = ImageIO.read(new File("GridSpace1.jpg"));
					ImageIcon icon = new ImageIcon(image);
					// Scaling image
					Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					ImageIcon scaledIcon = new ImageIcon(scaledImage);
					JLabel label = new JLabel(scaledIcon);
					gridPanel.add(label);

				} catch (IOException e) {
					JLabel emptyLabel = new JLabel();
					emptyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					gridPanel.add(emptyLabel);
					e.printStackTrace();
				}
			}
		}
		// Add panels to frame
		frame.add(title_panel, BorderLayout.NORTH);
		frame.add(gridPanel, BorderLayout.CENTER);

		frame.pack();// adjusts frame size to fit components
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
// public class MainFrame implements ActionListener{
// //action listener for button implementation, not needed for imageicon
// //ImageIcon gridImage = new ImageIcon("GridSpace1.jpg");
// ImageIcon originalIcon = new ImageIcon("GridSpace1.jpg");
// JFrame frame = new JFrame();
// JPanel title_panel = new JPanel();
// // JPanel button_panel = new JPanel();
// JLabel textfield = new JLabel();
// // JButton[] buttons = new JButton[9];

// // MainFrame(){
// // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// frame.setSize(800,800);
// frame.getContentPane().setBackground(new Color(50,50,50));
// frame.setLayout(new BorderLayout());
// frame.setVisible(true);

// textfield.setBackground(new Color(25,25,25));
// textfield.setForeground(new Color(25,255,0));
// textfield.setFont(new Font("Ink Free",Font.BOLD,75));
// textfield.setHorizontalAlignment(JLabel.CENTER);
// textfield.setText("Predator Prey Simulation");
// textfield.setOpaque(true);

// title_panel.setLayout(new BorderLayout());
// title_panel.setBounds(0,0,800,100);

// button_panel.setLayout(new GridLayout(5,5));
// button_panel.setBackground(new Color(150,150,150));

// for(int i=0;i<5;i++){
// for(int j=0; j<5;j++){
// buttons[i] = new JButton();
// //48by48 pixel boxes
// //960x960
// Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100,
// Image.SCALE_SMOOTH);
// ImageIcon scaledIcon = new ImageIcon(scaledImage);
// buttons[i].setIcon(scaledIcon);
// buttons[i].setBorderPainted(false);
// buttons[i].setFocusPainted(false);
// buttons[i].setContentAreaFilled(false);
// //buttons[i].setIcon(gridImage);
// button_panel.add(buttons[i]);
// buttons[i].setFont(new Font("MV Boli",Font.BOLD,120));
// buttons[i].setFocusable(false);
// buttons[i].addActionListener(this);

// }

// }

// title_panel.add(textfield);
// frame.add(title_panel,BorderLayout.NORTH);
// frame.add(button_panel);

// // @Override
// // public void actionPerformed(ActionEvent e){
// // // TODO Auto-generate method stub
// // }
// // }
