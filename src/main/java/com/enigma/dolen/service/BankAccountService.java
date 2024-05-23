package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Travel;

import java.util.List;

public interface BankAccountService {
    List<BankAccountResponse> createBankAccount(Travel travel, TravelRequest travelRequest);
    List<BankAccountResponse> addBankAccount(String travelId, AddBankAccountRequest addBankAccountRequest);
    BankAccountResponse updateBankAccount(String travelId, String accountId, BankAccountDTO bankAccountDTO);
    List<BankAccountResponse> getAllBankAccountByTravelId(String travelId);
    BankAccountResponse deleteBankAccount(String travelId, String accountId);
    BankAccountResponse deleteBankAccountForTravel(String id);
}
