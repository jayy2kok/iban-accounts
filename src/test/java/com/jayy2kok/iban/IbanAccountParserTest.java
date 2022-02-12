package com.jayy2kok.iban;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayy2kok.iban.model.IbanAccount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IbanAccountParserTest {
    @Test
    @DisplayName("formatted account input")
    void TestParseFormattedIban() {
        IbanAccount acct1 = IbanAccountParser.parseAccount("PL61 1090 1014 0000 0712 1981 2874");
        assertEquals("PL", acct1.countryCode());
        assertEquals("61", acct1.ibanCheckDigits());
        assertEquals("109", acct1.nationalBankCode());
        assertEquals("0 101", acct1.branchCode());
        assertEquals("4", acct1.checkDigitOrCharacter());
        assertEquals("0000 0712 1981 2874", acct1.formattedAccountNumber());
        assertEquals("0000071219812874", acct1.accountNumber());
    }

    @Test
    @DisplayName("unformatted account input")
    void TestParseUnFormattedIban() {
        IbanAccount acct1 = IbanAccountParser.parseAccount("PL61109010140000071219812874");
        assertEquals("PL", acct1.countryCode());
        assertEquals("61", acct1.ibanCheckDigits());
        assertEquals("109", acct1.nationalBankCode());
        assertEquals("0101", acct1.branchCode());
        assertEquals("4", acct1.checkDigitOrCharacter());
        assertEquals("0000 0712 1981 2874", acct1.formattedAccountNumber());
        assertEquals("0000071219812874", acct1.accountNumber());
    }
}
