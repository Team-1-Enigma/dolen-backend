package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.BankAccount;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.repository.BankAccountRepository;
import com.enigma.dolen.service.BankAccountService;
import com.enigma.dolen.service.TravelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private TravelService travelService;

    @Autowired
    @Lazy
    public void setTravelService(TravelService travelService) {
        this.travelService = travelService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<BankAccountResponse> createBankAccount(Travel travel, TravelRequest travelRequest) {

        Travel existingTravel = travelService.getTravelByIdForOther(travel.getId());

        List<BankAccountResponse> result = new ArrayList<>();
        for(BankAccount value : travelRequest.getBankAccounts()){
            BankAccount bankAccount = BankAccount.builder()
                    .travel(existingTravel)
                    .name(value.getName())
                    .aliasName(value.getAliasName())
                    .bankName(value.getBankName())
                    .accountNumber(value.getAccountNumber())
                    .createdAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            bankAccountRepository.saveAndFlush(bankAccount);

            result.add(toBankAccountResponse(bankAccount));
        }


        return result;
    }

    @Override
    public List<BankAccountResponse> addBankAccount(String travelId, AddBankAccountRequest addBankAccountRequest) {

        Travel existingTravel = travelService.getTravelByIdForOther(travelId);
        if (existingTravel == null){
            return null;
        }

        List<BankAccountResponse> bankAccountResponses = new ArrayList<>();
        for(BankAccount value : addBankAccountRequest.getBankAccounts()) {
            BankAccount bankAccount = BankAccount.builder()
                    .travel(existingTravel)
                    .name(value.getName())
                    .aliasName(value.getAliasName())
                    .bankName(value.getBankName())
                    .accountNumber(value.getAccountNumber())
                    .createdAt(LocalDateTime.now())
                    .isActive(true)
                    .build();
            bankAccountRepository.save(bankAccount);

            bankAccountResponses.add(toBankAccountResponse(bankAccount));
        }

        return bankAccountResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BankAccountResponse updateBankAccount(String travelId, String accountId, BankAccountDTO bankAccountDTO) {
        Travel existingTravel = travelService.getTravelByIdForOther(travelId);
        if (existingTravel == null){
            return null;
        }
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null){
            return null;
        }
        for(BankAccount value : existingTravel.getBankAccounts()) {
            if(value.getId().equals(bankAccount.getId())){
                BankAccount account = value.builder()
                        .travel(existingTravel)
                        .id(value.getId())
                        .name(bankAccountDTO.getName())
                        .aliasName(bankAccountDTO.getAliasName())
                        .bankName(bankAccountDTO.getBankName())
                        .accountNumber(bankAccountDTO.getAccountNumber())
                        .isActive(value.getIsActive())
                        .createdAt(value.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .build();
                bankAccountRepository.saveAndFlush(account);

                return toBankAccountResponse(account);
            }
        }

        return null;
    }

    private static BankAccountResponse toBankAccountResponse(BankAccount bankAccount) {
        BankAccountResponse bankAccountResponse = BankAccountResponse.builder()
                .id(bankAccount.getId())
                .name(bankAccount.getName())
                .aliasName(bankAccount.getAliasName())
                .bankName(bankAccount.getBankName())
                .accountNumber(bankAccount.getAccountNumber())
                .isActive(bankAccount.getIsActive())
                .createdAt(bankAccount.getCreatedAt())
                .updatedAt(bankAccount.getUpdatedAt())
                .build();
        return bankAccountResponse;
    }



    @Override
    public List<BankAccountResponse> getAllBankAccountByTravelId(String travelId) {
        Travel existingTravel = travelService.getTravelByIdForOther(travelId);
        if (existingTravel == null){
            return null;
        }

        List<BankAccount> bankAccounts = new ArrayList<>();
        for (BankAccount bankAccount : existingTravel.getBankAccounts()){
            bankAccounts.add(bankAccount);
        }

        List<BankAccountResponse> bankAccountResponses = bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getIsActive() == true)
                .map(bankAccount -> toBankAccountResponse(bankAccount)
        ).collect(Collectors.toList());

        return bankAccountResponses;
    }

    @Override
    public BankAccountResponse deleteBankAccount(String travelId, String accountId) {
        Travel existingTravel = travelService.getTravelByIdForOther(travelId);
        if (existingTravel == null){
            return null;
        }

        BankAccount bankAccountToDelete = bankAccountRepository.findById(accountId).orElse(null);
        for (BankAccount value : existingTravel.getBankAccounts()){
            if(value.getId() == bankAccountToDelete.getId()){
                bankAccountToDelete.setIsActive(false);
                bankAccountRepository.save(bankAccountToDelete);
                return toBankAccountResponse(bankAccountToDelete);
            }
        }

        return null;
    }

    @Override
    public BankAccountResponse deleteBankAccountForTravel(String id) {
        BankAccount account = bankAccountRepository.findById(id).orElse(null);
        if (account == null) {
            return null;
        }

        account.setIsActive(false);
        bankAccountRepository.save(account);
        return toBankAccountResponse(account);
    }


}
