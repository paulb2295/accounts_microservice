package com.bpx.accounts_microservice.repositories;

import com.bpx.accounts_microservice.audit.AuditAwareImpl;
import com.bpx.accounts_microservice.entities.Customer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import(AuditAwareImpl.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Mark");
        customer.setEmail("mark@bpx");
        customer.setMobileNumber("0745123456");
    }

    //JUnit Test for save customer operation
    @Test
    void givenCustomerEntity_whenSave_thenReturnSavedCustomer() {
        //given - precondition or setup

        //when -action/behavior to be tested
        Customer savedCustomer = customerRepository.save(customer);

        //then - verify the output
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerId()).isGreaterThan(0);
    }

    //JUnit Test for findById operation
    @Test
    void givenCustomerId_whenFindById_thenReturnCustomer() {
        //given - precondition or setup
        Customer savedCustomer = customerRepository.save(customer);
        //when -action/behavior to be tested
        Customer retrievedCustomer = customerRepository.findById(savedCustomer.getCustomerId()).get();
        //then - verify the output
        assertThat(retrievedCustomer).isNotNull();
        assertThat(retrievedCustomer).isEqualTo(savedCustomer);
    }

    //JUnit Test for findByPhoneNumber operation
    @Test
    void givenCustomerPhoneNumber_whenFindByPhoneNumber_thenReturnCustomer() {
        //given - precondition or setup
        Customer savedCustomer = customerRepository.save(customer);
        //when -action/behavior to be tested
        Customer retrievedCustomer = customerRepository.findByMobileNumber(savedCustomer.getMobileNumber()).get();
        //then - verify the output
        assertThat(retrievedCustomer).isNotNull();
        assertThat(retrievedCustomer).isEqualTo(savedCustomer);
    }

    //JUnit Test for deleteyById operation
    @Test
    void givenCustomerId_whenDeleteById_thenRemoveCustomer() {
        //given - precondition or setup
        Customer savedCustomer = customerRepository.save(customer);
        //when -action/behavior to be tested
        customerRepository.deleteById(savedCustomer.getCustomerId());
        Optional<Customer> retrievedCustomer = customerRepository.findById(savedCustomer.getCustomerId());
        //then - verify the output
        assertThat(retrievedCustomer).isEmpty();

    }

}
