package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import server.IParkingGarage;
import server.ParkingGarage;

public class ParkingGarageView extends JFrame implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2882585847373762806L;

	private static ParkingGarageController controller;

	private static String garageOccupancyPrefix = "Garage Occupancy: ";
	static JButton entryButton;
	static JButton exitButton;
	static JButton adminPaymentButton;
	static JButton reportsButton;
	private JLabel occupancyLabel;

	private static String maxOccupancyPrefix = "Garage Status: ";
	private static String VACANCYLABEL = "VACANCY";
	private static String FULLLABEL = "FULL";
	private JLabel maxOccupancyLabel;

	// Specify the look and feel to use. Valid values:
	// null (use the default), "Metal", "System", "Motif", "GTK+"
	final static String LOOKANDFEEL = null;
	private String name;

	public ParkingGarageView(String name, String url) {
		// Create and set up the window.
		super("Parking Garage Application");

		// Make sure we have nice window decorations.
		setDefaultLookAndFeelDecorated(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Component contents = createComponents();
		getContentPane().add(contents, BorderLayout.CENTER);
		// Display the window.
		pack();
		setVisible(true);
		
		this.name = name;
		IParkingGarage garage = null;
		try {
			garage = (IParkingGarage) Naming.lookup(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			garage.attach(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controller = new ParkingGarageController(url);
		controller.attachButtons(entryButton, exitButton, adminPaymentButton, reportsButton);
	}

	public String getName() {
		return this.name;
	}

	public Component createComponents() {
		/*
		 * An easy way to put space between a top-level container and its
		 * contents is to put the contents in a JPanel that has an "empty"
		 * border.
		 */
		JPanel pane = new JPanel(new GridBagLayout());

		occupancyLabel = new JLabel(garageOccupancyPrefix);
		maxOccupancyLabel = new JLabel(maxOccupancyPrefix + VACANCYLABEL);

		createButtons();

		createPanel(pane);

		showParkingGarageScene(true);

		return pane;
	}

	public void update(int occupancy, boolean vacancyStatus) {
		occupancyLabel.setText(garageOccupancyPrefix + String.valueOf(occupancy));
		if (vacancyStatus) {
			maxOccupancyLabel.setText(maxOccupancyPrefix + VACANCYLABEL);
		} else {
			maxOccupancyLabel.setText(maxOccupancyPrefix + FULLLABEL);
		}
		pack();
	}

	private void createButtons() {
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

	private void showParkingGarageScene(boolean status) {
		occupancyLabel.setVisible(status);
		entryButton.setVisible(status);
		exitButton.setVisible(status);
		adminPaymentButton.setVisible(status);
	}

	private void createPanel(JPanel pane) {
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
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        new ParkingGarageView(args[2], "rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");
		    }
		});
	}

}