package com.enigma.dolen.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class MidtransResponse {
    private String token;
    private String redirect_url;
}
