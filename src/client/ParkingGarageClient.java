package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ParkingGarageClient implements ParkingGarageObserver {
	private static ParkingGarageController controller;

	private static String garageOccupancyPrefix = "Garage Occupancy: ";
	static JButton entryButton;
	static JButton exitButton;
	static JButton adminPaymentButton;
	static JButton reportsButton;
	private static JLabel occupancyLabel;

	private static String maxOccupancyPrefix = "Garage Status: ";
	private static String VACANCYLABEL = "VACANCY";
	private static String FULLLABEL = "FULL";
	private static JLabel maxOccupancyLabel;

	// Specify the look and feel to use. Valid values:
	// null (use the default), "Metal", "System", "Motif", "GTK+"
	final static String LOOKANDFEEL = null;

	public static Component createComponents() {
		occupancyLabel = new JLabel(garageOccupancyPrefix);
		maxOccupancyLabel = new JLabel(maxOccupancyPrefix + VACANCYLABEL);
		/*
		 * An easy way to put space between a top-level container and its
		 * contents is to put the contents in a JPanel that has an "empty"
		 * border.
		 */
		JPanel pane = new JPanel(new GridBagLayout());

		createButtons();

		createPanel(pane);

		showParkingGarageScene(true);

		return pane;
	}
	
	@Override
	public void update(int occupancy, boolean vacancyStatus) {
		occupancyLabel.setText(garageOccupancyPrefix + occupancy);
		if (vacancyStatus) {
			maxOccupancyLabel.setText(maxOccupancyPrefix + VACANCYLABEL);
		} else {
			maxOccupancyLabel.setText(maxOccupancyPrefix + FULLLABEL);
		}
	}
	
	private static void createButtons() {
		// Create Entry Button
		entryButton = new JButton("Entrance Garage");
		entryButton.setMnemonic(KeyEvent.VK_I);
		
		// Create Exit Button
		exitButton = new JButton("Exit Garage");
		exitButton.setMnemonic(KeyEvent.VK_I);
		
		// Create Admin Button
		adminPaymentButton = new JButton("Admin Payment");
		adminPaymentButton.setMnemonic(KeyEvent.VK_I);

		// Create Reports Button
		reportsButton = new JButton("Print Reports");
		reportsButton.setMnemonic(KeyEvent.VK_I);
	}

	private static void showParkingGarageScene(boolean status) {
		occupancyLabel.setVisible(status);
		entryButton.setVisible(status);
		exitButton.setVisible(status);
		adminPaymentButton.setVisible(status);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private void createAndShowGUI() {
		
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Parking Garage Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Component contents = createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		controller.attachButtons(entryButton, exitButton, adminPaymentButton, reportsButton);
		controller.attachObserver(this);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private static void createPanel(JPanel pane) {
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		pane.add(maxOccupancyLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		pane.add(occupancyLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		pane.add(entryButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		pane.add(exitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		pane.add(adminPaymentButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		pane.add(reportsButton, constraints);

		pane.setBorder(BorderFactory.createEmptyBorder(30, // top
														80, // left
														30, // bottom
														80)); // right
	}

	public void main(String[] args) {
		controller = new ParkingGarageController("rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}


}