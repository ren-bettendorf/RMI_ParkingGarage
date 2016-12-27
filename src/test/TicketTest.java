package test;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import common.Ticket;

public class TicketTest {

	Ticket ticket1;
	Ticket ticket2;
	Ticket ticket3;
	
	@Before
	public void setUp() throws Exception {
		LocalDateTime ldt = LocalDateTime.of(2,1,1,1,1);
		
		ticket1 = new Ticket(ldt);
		ticket2 = new Ticket(ldt);
		
		
		ticket3 = new Ticket(LocalDateTime.of(1,1,1,1,1));
	}

	@Test
	public void equalTickets() 
	{
		Assert.assertTrue( ticket1.equals(ticket2) );
	}
	// Test doesn't pass when running TestAll.java but does when you run TicketTest.java. Don't know why....
	@Test
	public void notEqualTickets()
	{
		Assert.assertFalse( ticket1.equals(ticket3) );
	}
	@Test
	public void nullEqualTickets() 
	{
		Ticket nullTicket = null;
		Assert.assertFalse( ticket1.equals(nullTicket) );
	}

}
