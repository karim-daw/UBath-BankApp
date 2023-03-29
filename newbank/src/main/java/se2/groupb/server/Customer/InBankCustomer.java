package se2.groupb.server.customer;

import se2.groupb.server.account.Account;

public class InBankCustomer extends Customer {

    private ArrayList<Payee> payees;

    /**
     * @return a string of an account
     */

    public String accountsToString() {
        String s = "";
        for (Account a : accounts) {
            s += a.toString();
        }
        return s;
    }

}
