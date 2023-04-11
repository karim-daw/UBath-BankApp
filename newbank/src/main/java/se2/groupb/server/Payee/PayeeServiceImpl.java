package se2.groupb.server.Payee;

import java.util.UUID;


//import se2.groupb.server.customer.Customer;
import se2.groupb.server.repository.PayeeRepositoryImpl;

public class PayeeServiceImpl implements PayeeService {
	
	// fields
    private final PayeeRepositoryImpl payeeRepository;
    
	// Constructor
    public PayeeServiceImpl(PayeeRepositoryImpl payeeRepository) {
        this.payeeRepository = payeeRepository; 
    }
    
    @Override
    public Payee getPayeeByID(UUID payeeID) {
    	return payeeRepository.findByID(payeeID);
    }
    
    // methods
    @Override
    public boolean addNewPayee(UUID customerID, PayeeDTO payeeDTO) {

        // early break
        if (payeeDTO == null) {
            return false;
        }

        String payeeAccountNumber = payeeDTO.getPayeeAccountNumber();
        String payeeBIC = payeeDTO.getPayeeBIC();
        String payeeName = payeeDTO.getPayeeName();

        // add new payee
        Payee newPayee = new Payee(customerID, payeeName, payeeAccountNumber, payeeBIC);
        if (payeeRepository.save(newPayee)) {
            return true;
        }
        return false;

    }

    @Override
    public String displayPayees(UUID customerID) {
        // TODO Auto-generated method stub
        return null;
    }

}
