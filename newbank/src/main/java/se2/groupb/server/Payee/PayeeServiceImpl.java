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
    
    @Override
    public boolean addNewPayee(PayeeDTO payeeDTO) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Payee getPayeeByDTO(PayeeDTO payeeDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    

    @Override
    public Payee getPayeeByName(String payeeName) {
        // TODO Auto-generated method stub
        return null;
    }

}
