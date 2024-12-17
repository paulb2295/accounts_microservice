package com.bpx.accounts_microservice.services;

import com.bpx.accounts_microservice.dtos.AccountDTO;
import com.bpx.accounts_microservice.dtos.CustomerDTO;
import com.bpx.accounts_microservice.entities.Account;
import com.bpx.accounts_microservice.entities.Customer;
import com.bpx.accounts_microservice.repositories.AccountsRepository;
import com.bpx.accounts_microservice.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountsServiceImpl accountsService;

    //JUnit Test for createAccount method
    @Test
    void givenCustomerDTOObject_whenCreateAccount_thenCreateAccount() {
        //given - precondition or setup
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Mark Harmon");
        customerDTO.setEmail("mark@mail.com");
        customerDTO.setMobileNumber("1234567890");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(1L);
        savedCustomer.setName("Mark Harmon");
        savedCustomer.setEmail("mark@mail.com");
        savedCustomer.setMobileNumber("1234567890");

        Account account = new Account();
        account.setCustomerId(savedCustomer.getCustomerId());
        account.setAccountNumber(12345174920L);
        account.setAccountType("Savings");
        account.setBranchAddress("123 NewYork");

        given(customerRepository.findByMobileNumber("1234567890"))
                .willReturn(Optional.empty());
        given(customerRepository.save(any(Customer.class)))
                .willReturn(savedCustomer);
        given(accountsRepository.save(any(Account.class)))
                .willReturn(account);
        //when -action/behavior to be tested
        accountsService.createAccount(customerDTO);
        //then - verify the output
        verify(customerRepository, times(1)).findByMobileNumber("1234567890");
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountsRepository, times(1)).save(any(Account.class));
    }

    //JUnit Test for fetchAccount method
    @Test
    void givenPhoneNumber_whenFetchAccount_thenRetrieveCustomerDTO() {
        //given - precondition or setup
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Mark Harmon");
        customer.setEmail("mark@mail.com");
        customer.setMobileNumber("1234567890");

        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        account.setAccountNumber(12345174920L);
        account.setAccountType("Savings");
        account.setBranchAddress("123 NewYork");
        given(customerRepository.findByMobileNumber(any(String.class)))
                .willReturn(Optional.of(customer));
        given(accountsRepository.findByCustomerId(any(Long.class)))
                .willReturn(Optional.of(account));
        //when -action/behavior to be tested
        CustomerDTO customerDTO = accountsService.fetchAccount("1234567890");
        //then - verify the output
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getAccountDto().getAccountNumber()).isEqualTo(account.getAccountNumber());
    }

    //JUnit Test for updateAccount method
    @Test
    void givenCustomerDTO_whenUpdateAccount_thenTrue() {
        //given - precondition or setup
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Mark Harmon");
        customerDTO.setEmail("mark@mail.com");
        customerDTO.setMobileNumber("1234567890");
        customerDTO.setAccountDto(
                new AccountDTO(
                        12345174920L,
                        "Savings",
                        "123 NewYork"
                )
        );

        Account account = new Account();
        account.setCustomerId(1L);
        account.setAccountNumber(12345174920L);
        account.setAccountType("Savings");
        account.setBranchAddress("123 NewYork");

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Mark Harmon");
        customer.setEmail("mark@mail.com");
        customer.setMobileNumber("1234567890");

        given(accountsRepository.findById(any(Long.class)))
                .willReturn(Optional.of(account));
        given(accountsRepository.save(any(Account.class)))
                .willReturn(account);
        given(customerRepository.findById(any(Long.class)))
                .willReturn(Optional.of(customer));
        given(customerRepository.save(any(Customer.class)))
                .willReturn(customer);
        //when -action/behavior to be tested
        boolean isUpdated = accountsService.updateAccount(customerDTO);
        //then - verify the output
        assertThat(isUpdated).isTrue();
    }

    //JUnit Test for deleteAccount method
    @Test
    void givenPhoneNumber_whenDeleteAccount_thenReturnTrue() {
        //given - precondition or setup
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Mark Harmon");
        customer.setEmail("mark@mail.com");
        customer.setMobileNumber("1234567890");

        given(customerRepository.findByMobileNumber(any(String.class)))
                .willReturn(Optional.of(customer));
        willDoNothing().given(accountsRepository).deleteByCustomerId(customer.getCustomerId());
        willDoNothing().given(customerRepository).deleteById(customer.getCustomerId());
        //when -action/behavior to be tested
        boolean isDeleted = accountsService.deleteAccount("1234567890");
        //then - verify the output
        assertThat(isDeleted).isTrue();
    }
}
