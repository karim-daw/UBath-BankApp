package se2.groupb.server.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se2.groupb.server.NewBank;
import se2.groupb.server.repository.AccountRepositoryImpl;

public class AccountServiceImplTest {

    private NewBank bank;
    private AccountRepositoryImpl accountRepository;
    private AccountService accountService;

    @Before
    public void setUp() {
        bank = NewBank.getBank(); // static instance of the bank
        accountRepository = Mockito.mock(AccountRepositoryImpl.class);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void testDebitWithValidInputs() {
        UUID accountID = UUID.randomUUID();
        Account account = new Account(accountID, "Checking", "Karim Doe", new BigDecimal("100.00"));

        Mockito.when(accountRepository.findByID(accountID)).thenReturn(account);
        Mockito.when(accountRepository.update(Mockito.any(Account.class))).thenReturn(true);

        boolean result = accountService.debit(accountID, BigDecimal.valueOf(50));

        assertTrue(result);
        assertEquals(0, BigDecimal.valueOf(50).compareTo(account.getBalance()));
    }

    @Test
    public void testDebitWithInvalidAccountID() {
        UUID accountID = UUID.randomUUID();
        Account account = new Account(accountID, "Checking", "Karim Doe", new BigDecimal("100.00"));

        boolean result = accountService.debit(null, BigDecimal.valueOf(50));
        assertFalse(result);

        Mockito.when(accountRepository.findByID(accountID)).thenReturn(account);
        result = accountService.debit(accountID, BigDecimal.valueOf(50));
        assertFalse(result);
    }

    @Test
    public void testDebitWithInvalidAmount() {
        UUID accountID = UUID.randomUUID();
        Account account = new Account(accountID, "Checking", "Karim Doe", new BigDecimal("100.00"));
        Mockito.when(accountRepository.findByID(accountID)).thenReturn(account);

        boolean result = accountService.debit(accountID, null);
        assertFalse(result);

        result = accountService.debit(accountID, BigDecimal.valueOf(-50));
        assertFalse(result);

        result = accountService.debit(accountID, BigDecimal.valueOf(0));
        assertFalse(result);
    }

    @Test
    public void testDebitWithInsufficientBalance() {
        UUID accountID = UUID.randomUUID();
        Account account = new Account(accountID, "Checking", "Karim Doe", new BigDecimal("100.00"));
        Mockito.when(accountRepository.findByID(accountID)).thenReturn(account);

        boolean result = accountService.debit(accountID, BigDecimal.valueOf(150));
        assertFalse(result);
        assertEquals(0, BigDecimal.valueOf(100).compareTo(account.getBalance()));
    }

    @Test
    public void testDebitWithWithdrawalException() {
        UUID accountID = UUID.randomUUID();
        Account account = Mockito.spy(new Account(accountID, "Checking", "Karim Doe", new BigDecimal("100.00")));
        Mockito.when(accountRepository.findByID(accountID)).thenReturn(account);
        Mockito.doThrow(new RuntimeException("withdrawal failed")).when(account)
                .withdraw(Mockito.any(BigDecimal.class));

        boolean result = accountService.debit(accountID, BigDecimal.valueOf(50));
        assertFalse(result);
        assertEquals(0, BigDecimal.valueOf(100).compareTo(account.getBalance()));
    }

}
