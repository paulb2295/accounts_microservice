package com.bpx.accounts_microservice.controllers;

import com.bpx.accounts_microservice.dtos.AccountDTO;
import com.bpx.accounts_microservice.dtos.CustomerDTO;
import com.bpx.accounts_microservice.services.AccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(AccountsController.class)

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountsService accountsService;


    //JUnit Test for Create Account REST API
    @Test
    void givenCustomerDTOObject_whenCreateAccount_thenStatusCreated() throws Exception {
        //given - precondition or setup
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Mark Harmon");
        customerDTO.setEmail("mark@mail.com");
        customerDTO.setMobileNumber("1234567890");
        willDoNothing().given(accountsService).createAccount(any(CustomerDTO.class));
        //when -action/behavior to be tested
        ResultActions response = mockMvc.perform(post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
        //then - verify the output
        response.andExpect(status().isCreated());
    }

    //JUnit Test for Fetch Account REST API
    @Test
    void givenPhoneNumber_whenFetchAccount_thenReturnCustomerDTO() throws Exception {
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
        given(accountsService.fetchAccount("1234567890"))
                .willReturn(customerDTO);
        //when -action/behavior to be tested
        ResultActions response = mockMvc.perform(get("/api/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .param("mobileNumber", "1234567890"));
        //then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customerDTO.getName())))
                .andExpect(jsonPath("$.accountDto.accountNumber", is(customerDTO.getAccountDto().getAccountNumber())));
    }

    //JUnit Test for Update Account REST API
    @Test
    void givenCustomerDTO_whenUpdateAccount_thenStatusOk() throws Exception {
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
        given(accountsService.updateAccount(any(CustomerDTO.class)))
                .willReturn(true);
        //when -action/behavior to be tested
        ResultActions response = mockMvc.perform(put("/api/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)));
        //then - verify the output
        response.andExpect(status().isOk());
    }

    //JUnit Test for Delete Account REST API
    @Test
    void givenMobileNumber_whenDeleteAccount_thenStatusOk() throws Exception {
        //given - precondition or setup
        given(accountsService.deleteAccount("1234567890"))
                .willReturn(true);
        //when -action/behavior to be tested
        ResultActions response = mockMvc.perform(delete("/api/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("mobileNumber", "1234567890"));
        //then - verify the output
        response.andExpect(status().isOk());
        verify(accountsService).deleteAccount("1234567890");
    }
}
