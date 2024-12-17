package com.bpx.accounts_microservice.repositories;

import com.bpx.accounts_microservice.audit.AuditAwareImpl;
import com.bpx.accounts_microservice.entities.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AuditAwareImpl.class)
public class AccountRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setCustomerId(1L);
        account.setAccountNumber(12345174920L);
        account.setAccountType("Savings");
        account.setBranchAddress("123 NewYork");
    }

    //JUnit Test for save account operation
    @Test
    void givenAccountObject_whenSave_thenReturnSavedAccount() {
        //given - precondition or setup

        //when -action/behavior to be tested
        Account savedAccount = accountsRepository.save(account);
        //then - verify the output
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount).isEqualTo(account);
    }

    //JUnit Test for findById (account) operation
    @Test
    void givenAccountId_whenFindById_thenReturnAccount() {
        //given - precondition or setup
        Account savedAccount = accountsRepository.save(account);
        //when -action/behavior to be tested
        Account retrievedAccount = accountsRepository.findById(savedAccount.getAccountNumber()).get();
        //then - verify the output
        assertThat(retrievedAccount).isNotNull();
        assertThat(retrievedAccount).isEqualTo(savedAccount);
    }

    //JUnit Test for findByCustomerId operation
    @Test
    void givenCustomerId_whenFindByCustomerId_thenReturnAccount() {
        //given - precondition or setup
        Account savedAccount = accountsRepository.save(account);
        long customerId = savedAccount.getCustomerId();
        //when -action/behavior to be tested
        Account retrievedAccount = accountsRepository.findByCustomerId(customerId).get();
        //then - verify the output
        assertThat(retrievedAccount).isNotNull();
        assertThat(retrievedAccount).isEqualTo(savedAccount);
    }

    //JUnit Test for deleteByCustomerId operation
    @Test
    void givenCustomerId_whenDeleteByCustomerId_thenRemoveAccount() {
        //given - precondition or setup
        Account savedAccount = accountsRepository.save(account);
        long customerId = savedAccount.getCustomerId();
        //when -action/behavior to be tested
        accountsRepository.deleteByCustomerId(customerId);
        Optional<Account> retrievedAccount = accountsRepository.findById(savedAccount.getAccountNumber());
        //then - verify the output
        assertThat(retrievedAccount).isEmpty();
    }
}
