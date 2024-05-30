package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.service.BankAccountService;
import com.enigma.dolen.service.TravelService;
import com.enigma.dolen.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/travels")
public class TravelController {

    private final TravelService travelService;
    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<?> createTravel(@ModelAttribute TravelRequest travelRequest){

        TravelCreateResponse travelCreateResponse = travelService.createTravel(travelRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Travel created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(travelCreateResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelById(@PathVariable String id){
        TravelCreateResponse travelResponse = travelService.getTravelById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel found")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllTravel(){
        List<TravelCreateResponse> travelResponses = travelService.getAllTravel();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Successfully get all travel")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponses)
                        .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable String id, @RequestBody TravelDTO travelDTO){
        TravelResponse travelResponse = travelService.updateTravel(id, travelDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel updated")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponse)
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable String id){
        String data = travelService.deleteTravel(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel deleted")
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .build());
    }

    @PostMapping("/{id}/bank_account")
    public ResponseEntity<?> createBankAccount(@PathVariable String id, @RequestBody AddBankAccountRequest request){
        List<BankAccountResponse> bankAccountResponses = bankAccountService.addBankAccount(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Bank account successfully added")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(bankAccountResponses)
                        .build());
    }

    @PutMapping("/{id}/bank_account/{account_id}")
    public ResponseEntity<?> updateBankAccount(@PathVariable String id, @PathVariable String account_id, @RequestBody BankAccountDTO bankAccountDTO){
        BankAccountResponse bankAccountResponse = bankAccountService.updateBankAccount(id, account_id, bankAccountDTO);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Bank account updated")
                       .statusCode(HttpStatus.OK.value())
                       .data(bankAccountResponse)
                       .build());
    }

    @GetMapping("/{id}/bank_account")
    public ResponseEntity<?> getBankAccountByTravelId(@PathVariable String id){
        List<BankAccountResponse> bankAccountResponses = bankAccountService.getAllBankAccountByTravelId(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Bank account found")
                       .statusCode(HttpStatus.OK.value())
                       .data(bankAccountResponses)
                       .build());
    }

    @DeleteMapping("/{id}/bank_account/{account_id}")
    public ResponseEntity<?> deleteBankAccount(@PathVariable String id, @PathVariable String account_id){
        BankAccountResponse bankAccountResponse = bankAccountService.deleteBankAccount(id, account_id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Bank account deleted")
                       .statusCode(HttpStatus.OK.value())
                       .data(bankAccountResponse)
                       .build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getTravelByUserId(@PathVariable String id){
        TravelResponse travelResponse = travelService.getTravelByUserId(id);


        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Bank account found")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponse)
                        .build());
    }
}
