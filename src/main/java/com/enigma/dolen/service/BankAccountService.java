package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.BankAccountDTO;
import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.entity.BankAccount;

public interface BankAccountService {
    BankAccountResponse createBankAccount(BankAccountDTO bankAccountDTO);
    BankAccountResponse getBankAccountById(String id);
    BankAccountResponse updateBankAccount(BankAccountDTO bankAccountDTO);
    BankAccountResponse deleteBankAccount(String id);
}
