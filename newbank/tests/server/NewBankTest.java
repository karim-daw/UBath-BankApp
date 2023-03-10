package newbank.tests.server;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import newbank.server.NewBank;

public class NewBankTest {

    private NewBank newBank;
    private CustomerID cID;
    private HashMap<String, Customer> dummyCustomers;

    @Before
    public void initialize() {
        newBank = new NewBank();
        dummyCustomers = newBank.getCustomers();
        cID = new CustomerID("Christina");
    }

    public HashMap<String, Customer> getDummyCustomers() {
        return dummyCustomers;
    }

    /**
     * debugging helper function that adds dummy data to a hashmap
     */
    private void addTestData() {
        Customer bhagy = new Customer();
        bhagy.addAccount(new Account("Main", 1000.0));
        bhagy.addAccount(new Account("Savings", 200.0));
        getDummyCustomers().put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(new Account("Savings", 1500.0));
        getDummyCustomers().put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(new Account("Checking", 250.0));
        getDummyCustomers().put("John", john);
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
        String savingsString = "Savings: 1500.0";

        assertEquals(savingsString, newBank.showMyAccounts(cID));
    }
}
