package sampleshop.core.inport;

import nitrox.dlc.domain.types.Driver;
import sampleshop.core.domain.customer.AddNewCustomer;
import sampleshop.core.domain.customer.ChangeCreditCard;
import sampleshop.core.domain.customer.ChangeCustomerAddress;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.outport.OrdersByCustomer;

import java.util.List;
import java.util.Optional;

/**
 * The OrderDriver contains all operations driving the application
 * in association with {@link Customer}s.
 *
 * @author Mario Herb
 */
public interface CustomerDriver extends Driver {

    /**
     * Add a new Customer
     */
    Customer add(AddNewCustomer addNewCustomer);

    /**
     * Change address of existing Customer
     */
    Optional<Customer> changeAddress(ChangeCustomerAddress changeCustomerAddress);

    /**
     * Change credit card information of existing Customer
     */
    Optional<Customer> changeCreditCard(ChangeCreditCard changeCreditCard);

    /**
     * Find list of existing Customers
     */
    List<Customer> find(int offset, int limit);

    /**
     * Find list of existing Customers with filter option
     */
    List<OrdersByCustomer> reportOrders(String customerFilter, int offset, int limit);
}
