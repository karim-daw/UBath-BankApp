package se2.groupb.server.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import se2.groupb.server.NewBank;
import se2.groupb.server.account.Account;

public class AccountRepositoryImplTest {

    private NewBank bank;
    private AccountRepositoryImpl accountRepository;
    private Account account1;
    private Account account2;

    @Before
    public void setUp() {
        bank = NewBank.getBank(); // static instance of the bank
        accountRepository = new AccountRepositoryImpl(bank.getAccounts());
        System.out.println(accountRepository);
        account1 = new Account(UUID.randomUUID(), "Checking", "John Doe");
        account2 = new Account(UUID.randomUUID(), "Savings", "Jane Doe", new BigDecimal("1000.00"));

    }

    @Test
    public void testSaveNewAccount() {
        System.out.println(account1);
        assertTrue(accountRepository.save(account1));
        assertEquals(account1, accountRepository.findByID(account1.getAccountID()));
    }

    @Test
    public void testSaveExistingAccount() {
        accountRepository.save(account1);
        assertFalse(accountRepository.save(account1));
    }

    @Test
    public void testSaveNullAccount() {
        assertFalse(accountRepository.save(null));
    }

    @Test
    public void testSaveWithNullValues() {
        // Test saving an account with null values for required fields
        Account account = new Account(null, null, null);
        assertFalse(accountRepository.save(account));
    }

    @Test
    public void testSaveWithZeroBalance() {
        // Test saving an account with a zero initial balance
        Account account = new Account(UUID.randomUUID(), "Checking", "Test Account", BigDecimal.ZERO);
        assertTrue(accountRepository.save(account));
    }

    @Test
    public void testSaveWithNegativeBalance() {
        // Test saving an account with a negative initial balance
        Account account = new Account(UUID.randomUUID(), "Checking", "Test Account", BigDecimal.valueOf(-100));
        assertTrue(accountRepository.save(account));
    }

    @Test
    public void testSaveWithDuplicateCustomerID() {
        // Test saving two accounts with the same ID
        UUID CustomerID = UUID.randomUUID();

        Account account1 = new Account(CustomerID, "Checking", "Test Account 1");
        Account account2 = new Account(CustomerID, "Savings", "Test Account 2");
        assertTrue(accountRepository.save(account1));
        assertTrue(accountRepository.save(account2));
    }

    @Test
    public void testFindByIDWithMultipleAccounts() {
        // Test finding an account by ID when there are multiple accounts in the
        // repository
        Account account1 = new Account(UUID.randomUUID(), "Checking", "Test Account 1");
        Account account2 = new Account(UUID.randomUUID(), "Savings", "Test Account 2");
        assertTrue(accountRepository.save(account1));
        assertTrue(accountRepository.save(account2));
        assertEquals(account1, accountRepository.findByID(account1.getAccountID()));
        assertEquals(account2, accountRepository.findByID(account2.getAccountID()));
    }

    @Test
    public void testSaveWithExistingAccounts() {
        // Test saving an account when there are already accounts in the repository
        Account account1 = new Account(UUID.randomUUID(), "Checking", "Test Account 1");
        Account account2 = new Account(UUID.randomUUID(), "Savings", "Test Account 2");
        accountRepository.save(account1);
        assertTrue(accountRepository.save(account2));
        assertEquals(account1, accountRepository.findByID(account1.getAccountID()));
        assertEquals(account2, accountRepository.findByID(account2.getAccountID()));
    }

    @Test
    public void testFindByAccountNumber() {
        // Arrange
        Account account1 = new Account(UUID.randomUUID(), "Checking", "Test Account 1");
        accountRepository.save(account1);

        String accountNumber1 = account1.getAccountNumber();
        Account foundAccount = accountRepository.findByAccountNumber(accountNumber1);
        assertTrue(account1.getAccountID().equals(foundAccount.getAccountID()));

    }

    @Test
    public void testFindByAccountNumberReturnsNullWhenNotFound() {
        // Arrange
        Account account1 = new Account(UUID.randomUUID(), "Checking", "Test Account 1");
        accountRepository.save(account1);

        // Act
        Account foundAccount = accountRepository.findByAccountNumber("4444");

        // Assert
        assertNull(foundAccount);
    }

    @Test
    public void testFindByAccountNumberReturnsNullWhenAccountNumberIsNull() {

        // Act
        Account result = accountRepository.findByAccountNumber(null);

        // Assert
        assertNull(result);
    }

}
