package com.jayy2kok.iban.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class IbanAccount {
    private String countryCode;
    private String nationalBankCode;
    private String branchCode;
    private String accountNumber;
    private String formattedAccountNumber;
    private String balanceAccountNumber;
    private String accountType;
    private String ownerAccountNumber;
    private String currencyCode;
    private String holderNationalId;
    private String ibanCheckDigits;
    private String checkDigitOrCharacter;
    private String ibanFormat;
}
