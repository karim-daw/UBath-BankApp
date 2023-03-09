package newbank.tests.server;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import newbank.server.CustomerID;
import newbank.server.NewBank;

public class NewBankTest {

    private NewBank newBank;
    private CustomerID cID;

    @Before
    public void initialize() {
        newBank = new NewBank();
        cID = new CustomerID("Christina");
    }

    @Test
    public void testCheckLogInDetails() {

    }

    @Test
    public void testGetCustomers() {

    }

    @Test
    public void testProcessRequest() {

    }

    @Test
    public void testShowMyAccounts() {
        String testStr = "Savings: 1500.0\n";
        assertEquals(testStr, newBank.showMyAccounts(cID));
    }
}
