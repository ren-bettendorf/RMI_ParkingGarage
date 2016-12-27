package client;

import javax.swing.SwingUtilities;

public class ParkingGarageClient {
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				try {

					createAndShowGUI(args[2], "rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		});

	}

	public static void createAndShowGUI(String name, String url) throws Exception {
		new ParkingGarageView(name, url);
	}

}
