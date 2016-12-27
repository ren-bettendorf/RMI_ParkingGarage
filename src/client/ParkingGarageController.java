package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import common.Ticket;
import server.ParkingGarage;

public class ParkingGarageController {

	private Ticket lastTicket;
	private ParkingGarage garage;
	private JButton entryButton, exitButton, adminPaymentButton, reportsButton;

	public ParkingGarageController(String url) {
		try {
			this.garage = (ParkingGarage) Naming.lookup(url);

		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
			System.exit(-1);

		} catch (MalformedURLException murle) {
			System.out.println("MalformedURLException");
			System.out.println(murle);
			System.exit(-1);

		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
			System.exit(-1);

		}
	}
	
	

	public boolean addCarToGarage() {
		try {
			setLastTicket(garage.addCarToGarage());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		boolean ticketStatus = true;

		if (lastTicket == null) {
			ticketStatus = false;
		}

		return ticketStatus;
	}

	/**
	 * Removes the car from the garage system while recording exit and payment
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @param payment
	 *            payment the car used
	 */
	public boolean removeCarFromGarage(String ticketID, double amountPaid, LocalDateTime ldt) {
		boolean retStatus = true;

		Ticket ticket = findTicket(ticketID);

		if (ticket != null) {
			try {
				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			retStatus = false;
		}

		return retStatus;
	}

	public boolean checkGarageSpace() {
		boolean status = true;
		try {
			status = garage.checkGarageSpace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	public String getGarageOccupancyStatus() {
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(garage.getCarOccupancy() + " : " + garage.getMaxCarOccupancy());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String runOccuReports(LocalDateTime begin, LocalDateTime end) {
		String sb = null;
		try {
			sb = garage.runOccupationReports(begin, end);
		} catch (RemoteException re) {
			re.printStackTrace();
		}
		return sb;
	}

	public String runReports(LocalDateTime begin, LocalDateTime end) {
		String sb = null;
		try {
			sb = garage.runReports(begin, end);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Checks if the ticket hasn't been paid for and if it has pays for the
	 * ticket
	 * 
	 * @param ticket
	 *            Ticket's payment status to be checked
	 */
	public void payForTicket(Ticket ticket, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCash(ticket, amountPaid, ldt);
				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void payForTicket(String userName, String userAddress, String userPhoneNumber) {
		LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
				LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
		LocalDateTime dateOwed = ldt.plusWeeks(2);
		try {
			garage.addFinancialRecordAdmin(userName, userAddress, userPhoneNumber, 50.00, dateOwed);

			// garage.removeCarFromGarage(ticket);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void payForTicket(Ticket ticket, String ccNumber, LocalDateTime expDate, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCredit(ticket, ccNumber, expDate, amountPaid, ldt);

				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Attempts to pay for ticket and starts checkout process
	 * 
	 * @param ticketID
	 *            uniqueID of ticket to be found
	 * @return boolean true if car checked out, false otherwise
	 */
	public boolean checkTicketPayment(String ticketID) {
		Ticket ticket = findTicket(ticketID);

		if (ticket != null) {
			if (ticket.getPaymentStatus()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Searches garage for the ticket
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @return t Ticket if found otherwise null
	 */
	public Ticket findTicket(String ticketID) {
		ArrayList<Ticket> tickets = null;
		try {
			tickets = garage.getTickets();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Ticket t = null;
		for (Ticket ticket : tickets) {
			if (ticket.getUniqueID().equals(ticketID)) {
				return ticket;
			}
		}

		return t;
	}

	/**
	 * Calculates the amount needed to pay for parking
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @return double amount due for parking
	 */
	public double getAmountDueOnTicket(Ticket ticket) {
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from(ticket.getCheckinTime());
		double amountDue = tempDateTime.until(ldt, ChronoUnit.HOURS) + 1.00;

		return amountDue;
	}

	public Ticket getLastTicket() {
		return lastTicket;
	}

	public void setLastTicket(Ticket ticket) {
		lastTicket = ticket;
	}



	private void createReportsButton() {
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
	
				JOptionPane.showMessageDialog(null, runReports(beginDate, endDate));
			}
		});
	}



	private void createAdminPaymentButton() {
		adminPaymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String userAddress = JOptionPane.showInputDialog("Please enter the address");
				String userName = JOptionPane.showInputDialog("Please enter the name");
				String userPhoneNumber = JOptionPane.showInputDialog("Please enter the phone number");
	
				payForTicket(userName, userAddress, userPhoneNumber);
	
			}
		});
	}




	private void createExitButton() {
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
	
				String ticketID = JOptionPane.showInputDialog("Please enter your ticket number: ");
				Ticket ticket = findTicket(ticketID);
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
	
				} else {
					JOptionPane.showMessageDialog(null, "Sorry something went wrong");
				}
			}
		});
	}



	private void createEntryButton() {
		entryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
	
				if (addCarToGarage()) {
					JOptionPane.showMessageDialog(null, "Gate has space and ticket is dispensed");
					JOptionPane.showMessageDialog(null, "Ticket Number: " + getLastTicket().toString());
					// Just to make copying the ticket number easier for mine
					// and your sanity
					System.out.println(getLastTicket().toString());
					JOptionPane.showMessageDialog(null, "Please enter garage. Gate opens.");
					JOptionPane.showMessageDialog(null, "Gate closes once car enters.");
					
				} else {
					JOptionPane.showMessageDialog(null, "Sorry but garage is full");
				}
			}
		});
	}
	
	private void createCreditPayment(Ticket ticket) {
		
		// Make sure ticket hasn't been paid for yet
		if (ticket != null) {
			double amountDue = getAmountDueOnTicket(ticket);
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
				payForTicket(ticket, ccNumber, expDate, amountPaid);
				if (amountPaid > amountDue) {
					JOptionPane.showMessageDialog(null, "Refunded: " + (amountPaid - amountDue));
				}
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(null, "Something went wrong with the payment. Try again.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Sorry something went wrong");
		}
	}



	private void createCashPayment(Ticket ticket) {
	
		// Make sure ticket hasn't been paid for yet
		if (ticket != null) {
			double amountDue = getAmountDueOnTicket(ticket);
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
				payForTicket(ticket, amountPaid);
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



	public void attachObserver(ParkingGarageObserver obs) {
		garage.attach(obs);		
	}



	public void attachButtons(JButton entryButton, JButton exitButton, JButton adminPaymentButton,
			JButton reportsButton) {
		this.entryButton = entryButton;
		createEntryButton();
		this.exitButton = exitButton;
		createExitButton();
		this.adminPaymentButton = adminPaymentButton;
		createAdminPaymentButton();
		this.reportsButton = reportsButton;
		createReportsButton();
	}
}
