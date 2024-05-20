package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.BankAccountDTO;
import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.BankAccount;
import com.enigma.dolen.model.entity.Travel;

public interface BankAccountService {
    BankAccountResponse createBankAccount(TravelDTO travelDTO, Travel travel);
    BankAccountResponse getBankAccountById(String id);
    BankAccountResponse updateBankAccount(BankAccountDTO bankAccountDTO);
    BankAccountResponse deleteBankAccount(String id);
}
