package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import common.Ticket;

public class ParkingGarageClient {
	static ParkingGarageController controller;

	private static String garageOccupancyPrefix = "Garage Occupancy: ";
	private static JButton entryButton;
	private static JButton exitButton;
	private static JButton adminPaymentButton;
	private static JButton reportsButton;
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

		createEntryButton();
		createExitButton();
		createAdminPaymentButton();
		createReportsButton();

		createPanel(pane);

		showParkingGarageScene(true);

		return pane;
	}

	private static void createEntryButton() {
		entryButton = new JButton("Entrance Garage");
		entryButton.setMnemonic(KeyEvent.VK_I);

		entryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if (controller.addCarToGarage()) {
					JOptionPane.showMessageDialog(null, "Gate has space and ticket is dispensed");
					JOptionPane.showMessageDialog(null, "Ticket Number: " + controller.getLastTicket().toString());
					// Just to make copying the ticket number easier for mine
					// and your sanity
					System.out.println(controller.getLastTicket().toString());
					JOptionPane.showMessageDialog(null, "Please enter garage. Gate opens.");
					JOptionPane.showMessageDialog(null, "Gate closes once car enters.");
				} else {
					JOptionPane.showMessageDialog(null, "Sorry but garage is full");
				}

				updateOccupancyLabel();
			}
		});
	}

	private static void createExitButton() {
		exitButton = new JButton("Exit Garage");
		exitButton.setMnemonic(KeyEvent.VK_I);

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String ticketID = JOptionPane.showInputDialog("Please enter your ticket number: ");
				Ticket ticket = controller.findTicket(ticketID);
				if (ticket != null) {
					Object[] options = { "Cash", "Credit" };
					int paymentResponse = JOptionPane.showOptionDialog(null, "Please select a payment option", null,
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

					if (paymentResponse == 0) {
						try {
							createCashPayment(ticket);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Something went wrong with the payment");
							return;
						}
					} else if (paymentResponse == 1) {
						try {
							createCreditPayment(ticket);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Something went wrong with the payment");
							return;
						}
					}

					JOptionPane.showMessageDialog(null, "Payment Accepted. Opening Exit Gate");
					JOptionPane.showMessageDialog(null, "Car exits garage");
					JOptionPane.showMessageDialog(null, "Exit gate closes. Ready for next exit");

					updateOccupancyLabel();
				} else {
					JOptionPane.showMessageDialog(null, "Sorry something went wrong");
				}
			}
		});
	}

	private static void createCashPayment(Ticket ticket) {

		// Make sure ticket hasn't been paid for yet
		if (ticket != null) {
			double amountDue = controller.getAmountDueOnTicket(ticket);
			double amountPaid = -1.00;
			try {
				amountPaid = Double
						.parseDouble(JOptionPane.showInputDialog("Amount Due: " + amountDue + "\nPlease enter cash: "));
				if (amountPaid <= 0) {
					throw new IllegalArgumentException( "Sorry but payment can't be less than or equal to 0");
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Sorry something went wrong with your payment please try again.");
				return;
			}
			try {
				controller.payForTicket(ticket, amountPaid);
				if (amountPaid > amountDue) {
					JOptionPane.showMessageDialog(null, "Refunded: " + (amountPaid - amountDue));
				}
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, "Something went wrong with the payment. Try again.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Sorry something went wrong");
			return;
		}

	}

	private static void createCreditPayment(Ticket ticket) {

		// Make sure ticket hasn't been paid for yet
		if (ticket != null) {
			double amountDue = controller.getAmountDueOnTicket(ticket);
			double amountPaid = -1.00;
			String ccNumber = "";
			try {
				amountPaid = Double
						.parseDouble(JOptionPane.showInputDialog("Amount Due: " + amountDue + "\nPlease enter cash: "));
				if (amountPaid <= 0) {
					throw new IllegalArgumentException("Sorry but payment can't be less than or equal to 0");				
				}
				ccNumber = JOptionPane.showInputDialog("Please enter credit card number: ");
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Sorry something went wrong with your payment please try again.");
				return;
			}

			SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
			LocalDateTime expDate = null;

			try {
				Date date = df.parse(JOptionPane.showInputDialog("Please expiration date (MM/yyyy): "));
				expDate = LocalDateTime.of(date.getYear() + 1900, date.getMonth() + 1, 1, 0, 0);
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(null, "Trouble : " + exc.toString());
				return;
			}
			try {
				controller.payForTicket(ticket, ccNumber, expDate, amountPaid);
				if (amountPaid > amountDue) {
					JOptionPane.showMessageDialog(null, "Refunded: " + (amountPaid - amountDue));
				}
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, "Something went wrong with the payment. Try again.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Sorry something went wrong");
		}
		updateOccupancyLabel();
	}

	private static void createAdminPaymentButton() {
		adminPaymentButton = new JButton("Admin Payment");
		adminPaymentButton.setMnemonic(KeyEvent.VK_I);

		adminPaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String userAddress = JOptionPane.showInputDialog("Please enter the address");
				String userName = JOptionPane.showInputDialog("Please enter the name");
				String userPhoneNumber = JOptionPane.showInputDialog("Please enter the phone number");

				controller.payForTicket(userName, userAddress, userPhoneNumber);

			}
		});
	}

	private static void createReportsButton() {
		reportsButton = new JButton("Print Reports");
		reportsButton.setMnemonic(KeyEvent.VK_I);
		reportsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				LocalDateTime beginDate = null;
				LocalDateTime endDate = null;
				try {
					Date date = df.parse(JOptionPane.showInputDialog("Please expiration date (MM/dd/yyyy): "));
					beginDate = LocalDateTime.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), 0, 0);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(null, "Trouble : " + exc.toString());
					return;
				}

				try {
					Date date = df.parse(JOptionPane.showInputDialog("Please expiration date (MM/dd/yyyy): "));
					endDate = LocalDateTime.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), 0, 0);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(null, "Trouble : " + exc.toString());
					return;
				}

				JOptionPane.showMessageDialog(null, controller.runReports(beginDate, endDate));
			}
		});
	}

	private static void showParkingGarageScene(boolean status) {
		occupancyLabel.setVisible(status);
		if (status) {
			updateOccupancyLabel();
		}
		entryButton.setVisible(status);
		exitButton.setVisible(status);
		adminPaymentButton.setVisible(status);
	}

	private static void updateOccupancyLabel() {
		occupancyLabel.setText(garageOccupancyPrefix + controller.getGarageOccupancyStatus());
		if (!controller.checkGarageSpace()) {
			maxOccupancyLabel.setText(maxOccupancyPrefix + VACANCYLABEL);
		} else {

			maxOccupancyLabel.setText(maxOccupancyPrefix + FULLLABEL);
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

	public static void main(String[] args) {
		controller = new ParkingGarageController("rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}