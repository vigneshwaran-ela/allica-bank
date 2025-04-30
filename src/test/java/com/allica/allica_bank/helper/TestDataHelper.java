package com.allica.allica_bank.helper;

import com.allica.allica_bank.entity.Customer;
import com.allica.allica_bank.entity.Retailer;

public class TestDataHelper {

    /**
     * Creates and returns a Customer entity linked to a retailer ID.
     * This is useful for setting up data in tests.
     */
    public static Customer createCustomer(String loginName, Long retailerId) {
        Customer customer = new Customer();
        customer.setLoginName(loginName);
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setDob("1990-01-01");

        // Set a dummy retailer with only ID (Hibernate will fetch full relation)
        Retailer retailer = new Retailer();
        retailer.setId(retailerId);
        customer.setRetailer(retailer);

        return customer;
    }
}
