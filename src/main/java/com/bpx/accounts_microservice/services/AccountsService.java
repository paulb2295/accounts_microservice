package com.bpx.accounts_microservice.services;

import com.bpx.accounts_microservice.dtos.CustomerDTO;

public interface AccountsService {

    void createAccount(CustomerDTO customerDto);

    CustomerDTO fetchAccount(String mobileNumber);

    boolean updateAccount (CustomerDTO customerDTO);

    boolean deleteAccount(String mobileNumber);
}
