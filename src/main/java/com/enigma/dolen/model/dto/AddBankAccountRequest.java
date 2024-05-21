package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBankAccountRequest {
    private String travelId;
    private List<BankAccount> bankAccounts;
}
