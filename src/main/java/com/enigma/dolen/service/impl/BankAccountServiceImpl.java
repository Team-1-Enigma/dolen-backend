package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.BankAccountDTO;
import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.BankAccount;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.repository.BankAccountRepository;
import com.enigma.dolen.service.BankAccountService;
import com.enigma.dolen.service.TravelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TravelService travelService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public BankAccountResponse createBankAccount(BankAccountDTO bankAccountDTO) {

        Travel travel = travelService.getTravelByIdForOther(bankAccountDTO.getTravelId());

        BankAccount bankAccount = BankAccount.builder()
                .travel(travel)
                .name(bankAccountDTO.getName())
                .aliasName(bankAccountDTO.getAliasName())
                .bankName(bankAccountDTO.getBankName())
                .accountNumber(bankAccountDTO.getAccountNumber())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        bankAccountRepository.saveAndFlush(bankAccount);

        return toBankAccountResponse(bankAccount);
    }

    private static BankAccountResponse toBankAccountResponse(BankAccount bankAccount) {
        BankAccountResponse bankAccountResponse = BankAccountResponse.builder()
                .id(bankAccount.getId())
                .travelDTO(TravelDTO.builder()
                        .id(bankAccount.getTravel().getId())
                        .userId(bankAccount.getTravel().getUser().getId())
                        .name(bankAccount.getTravel().getName())
                        .contactInfo(bankAccount.getTravel().getContactInfo())
                        .address(bankAccount.getTravel().getAddress())
                        .build())
                .name(bankAccount.getName())
                .aliasName(bankAccount.getAliasName())
                .bankName(bankAccount.getBankName())
                .accountNumber(bankAccount.getAccountNumber())
                .build();
        return bankAccountResponse;
    }

    @Override
    public BankAccountResponse getBankAccountById(String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        return toBankAccountResponse(bankAccount);
    }

    @Override
    public BankAccountResponse updateBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount existingBankAccount = bankAccountRepository.findById(bankAccountDTO.getId()).orElse(null);
        if (existingBankAccount == null){
            return null;
        }
        BankAccount saveBankAccount = BankAccount.builder()
                .id(existingBankAccount.getId())
                .travel(existingBankAccount.getTravel())
                .name(bankAccountDTO.getName())
                .aliasName(bankAccountDTO.getAliasName())
                .bankName(bankAccountDTO.getBankName())
                .accountNumber(bankAccountDTO.getAccountNumber())
                .updatedAt(LocalDateTime.now())
                .isActive(existingBankAccount.getIsActive())
                .build();
        bankAccountRepository.saveAndFlush(saveBankAccount);

        return toBankAccountResponse(saveBankAccount);
    }

    @Override
    public BankAccountResponse deleteBankAccount(String id) {
        BankAccount bankAccountToDelete = bankAccountRepository.findById(id).orElse(null);
        if(bankAccountToDelete == null){
            return null;
        }
        bankAccountToDelete.setIsActive(false);
        bankAccountRepository.save(bankAccountToDelete);
        return toBankAccountResponse(bankAccountToDelete);
    }
}
