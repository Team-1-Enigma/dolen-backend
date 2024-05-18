package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.BankAccountDTO;
import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.BankAccount;
import com.enigma.dolen.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<?> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO){
        BankAccountResponse bankAccountResponse = bankAccountService.createBankAccount(bankAccountDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Bank account created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(bankAccountResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBankAccountById(@PathVariable String id){
        BankAccountResponse bankAccountResponse = bankAccountService.getBankAccountById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Bank account found")
                        .statusCode(HttpStatus.OK.value())
                        .data(bankAccountResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateBankAccount(@RequestBody BankAccountDTO bankAccountDTO){
        BankAccountResponse bankAccountResponse = bankAccountService.updateBankAccount(bankAccountDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("updated bank account")
                        .statusCode(HttpStatus.OK.value())
                        .data(bankAccountResponse)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBankAccount(@PathVariable String id){
        bankAccountService.deleteBankAccount(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Bank account deleted")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
