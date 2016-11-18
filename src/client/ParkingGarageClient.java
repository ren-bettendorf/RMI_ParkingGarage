package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ParkingGarageClient {
	static ParkingGarageController controller;
	
	private static String garageOccupancyPrefix = "Cars in garage: ";
	private static JButton entryButton;
	private static JButton exitButton;
	private static JButton adminButton;
	private static JLabel occupancyLabel;

	private static String maxOccupancyPrefix = "Please enter maximum cars in garage: ";
	private static JLabel maxOccupancyLabel;
	private static JButton maxOccupancyButton;
	private static String errorStringPrefix = "Error: ";
	private static JLabel errorLabel;

	private static JLabel entranceTicketLabel;
	private static JLabel entranceGateStatusLabel;
	private static JLabel exitGateStatusLabel;

	// Specify the look and feel to use. Valid values:
	// null (use the default), "Metal", "System", "Motif", "GTK+"
	final static String LOOKANDFEEL = null;

	public static Component createComponents() {
		occupancyLabel = new JLabel(garageOccupancyPrefix);
		maxOccupancyLabel = new JLabel(maxOccupancyPrefix);
		errorLabel = new JLabel(errorStringPrefix);
		entranceTicketLabel = new JLabel("");
		entranceGateStatusLabel = new JLabel("CLOSED");
		exitGateStatusLabel = new JLabel("CLOSED");
		/*
		 * An easy way to put space between a top-level container and its
		 * contents is to put the contents in a JPanel that has an "empty"
		 * border.
		 */
		JPanel pane = new JPanel(new GridBagLayout());

		createEntryButton();
		createExitButton();
		createAdminButton();

		GridBagConstraints constraints = new GridBagConstraints();

		showParkingGarageScene(true);


		constraints.gridx = 0;
		constraints.gridy = 0;
		pane.add(occupancyLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		pane.add(entranceGateStatusLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		pane.add(exitGateStatusLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		pane.add(entryButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		pane.add(exitButton, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		pane.add(adminButton, constraints);

		// constraints.gridx = 0;
		// constraints.gridy = 2;
		// pane.add(errorLabel, constraints);
		// errorLabel.setVisible(false);

		constraints.gridx = 1;
		constraints.gridy = 3;
		pane.add(entranceTicketLabel, constraints);

		pane.setBorder(BorderFactory.createEmptyBorder(30, // top
				80, // left
				30, // bottom
				80)); // right

		return pane;
	}


	private static void createEntryButton() {
		entryButton = new JButton("Entrance Gate");
		entryButton.setMnemonic(KeyEvent.VK_I);
		entryButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				    controller.addCarToGarage();
				    entranceTicketLabel.setText("Car arrives");
				    
				  } 
				} );
	}

	private static void createExitButton() {
		exitButton = new JButton("Exit Gate");
		exitButton.setMnemonic(KeyEvent.VK_I);
		exitButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				// Add button action
				  } 
				} );
	}

	private static void createAdminButton() {
		adminButton = new JButton("Admin Options");
		adminButton.setMnemonic(KeyEvent.VK_I);
		adminButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				// Add button action
				  } 
				} );
	}

	private static void showParkingGarageScene(boolean status) {
		occupancyLabel.setVisible(status);
		if (status) {
			updateOccupancyLabel();
		}
		entranceGateStatusLabel.setVisible(status);
		entryButton.setVisible(status);
		exitButton.setVisible(status);
		exitGateStatusLabel.setVisible(status);
		adminButton.setVisible(status);
	}

	private static void updateOccupancyLabel() {
		occupancyLabel.setText(garageOccupancyPrefix + controller.getGarageOccupancyStatus());
	}

	public void actionPerformed(ActionEvent e) {
		errorLabel.setVisible(false);
		if (e.getSource() == maxOccupancyButton) {
		} else if (e.getSource() == entryButton) {
		} else if (e.getSource() == exitButton) {
		} else if (e.getSource() == adminButton) {
		}
	}
	

	private static void initLookAndFeel() {
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} else if (LOOKANDFEEL.equals("GTK+")) { // new in 1.4.2
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} else {
				System.err.println("Unexpected value of LOOKANDFEEL specified: " + LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				System.err.println("Couldn't find class for specified look and feel:" + lookAndFeel);
				System.err.println("Did you include the L&F library in the class path?");
				System.err.println("Using the default look and feel.");
			} catch (UnsupportedLookAndFeelException e) {
				System.err.println("Can't use the specified look and feel (" + lookAndFeel + ") on this platform.");
				System.err.println("Using the default look and feel.");
			} catch (Exception e) {
				System.err.println("Couldn't get specified look and feel (" + lookAndFeel + "), for some reason.");
				System.err.println("Using the default look and feel.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Set the look and feel.
		initLookAndFeel();

		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Parking Garage Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Component contents = createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args)  {
		controller = new ParkingGarageController("rmi://"+ args[0] + ":" + args[1] + "/ParkingGarageServer");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	 
}