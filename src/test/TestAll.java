package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@SuiteClasses({ CashPaymentTest.class, CreditPaymentTest.class, RecordManagerTest.class, TicketTest.class })
public class TestAll {
	// Execution begins at main(). In this test class, we will execute
	// a text test runner that will tell you if any of your tests fail.
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	// The suite() method is helpful when using JUnit 3 Test Runners or Ant.
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestAll.class);
	}
}
