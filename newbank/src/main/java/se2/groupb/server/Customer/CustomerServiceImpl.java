package se2.groupb.server.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import se2.groupb.server.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * method that changes the password
     * old password need to be enter
     * then a new password, twice
     * 
     * @param customer
     * @param requestInputs
     * @return
     */

    @Override
    public String changePassword(CustomerDTO customerDTO, String[] requestInputs) {
        // check if the command is correct
        // return infinite loop of null, why ?
        int inputLength = requestInputs.length;
        if (inputLength < 4) {
            return "FAIL. Please enter your old password and twice your new password after the command.";
        }

        String oldPassword = requestInputs[1];
        String newPassword = requestInputs[2];
        String confirmNewPassword = requestInputs[3];
        Customer customer = customerRepository.findByCustomerID(customerDTO.getCustomerID());

        // check if the old password is correct
        if (!customer.getPassword().equals(oldPassword)) {
            return "FAIL. The old password is incorrect.";
        }

        // check if the two new password inputs match.
        if (!newPassword.equals(confirmNewPassword)) {
            return "FAIL. Password confirmation does not match.";
        }

        else {
            customer.setPassword(newPassword);
            return "SUCCESS new password is: " + customer.getPassword();
        }
    }

    /**
     * displays accounts as a list
     * 
     * @param customer
     * @return
     */

    @Override
    public String showMyAccounts(CustomerDTO customerDTO) {

        UUID customerID = customerDTO.getCustomerID();

        // create a list that will be displayed
        List<String> accountList = new ArrayList<String>();
        Customer customer = this.customerRepository.findByCustomerID(customerID);

        accountList = customer.accountsToList();
        String s = "";
        for (String a : accountList) {
            s += a.toString() + "\n";
        }
        return s;

    }

}
