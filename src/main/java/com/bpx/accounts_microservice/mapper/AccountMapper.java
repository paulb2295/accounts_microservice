package com.bpx.accounts_microservice.mapper;

import com.bpx.accounts_microservice.dtos.AccountDTO;
import com.bpx.accounts_microservice.entities.Account;

public class AccountMapper {

    public static AccountDTO mapToAccountDto(Account account, AccountDTO accountsDto) {
        accountsDto.setAccountNumber(account.getAccountNumber());
        accountsDto.setAccountType(account.getAccountType());
        accountsDto.setBranchAddress(account.getBranchAddress());
        return accountsDto;
    }

    public static Account mapToAccount(AccountDTO accountsDto, Account account) {
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
        return account;
    }
}
