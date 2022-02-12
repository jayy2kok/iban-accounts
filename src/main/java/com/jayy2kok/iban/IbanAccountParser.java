package com.jayy2kok.iban;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import com.jayy2kok.iban.model.IbanAccount;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IbanAccountParser {
    private static final String SYMBOL_ACCOUNT = "c";
    private static final String SYMBOL_BANK_CODE = "b";
    private static final String SYMBOL_BRANCH_CODE = "s";
    private static final String SYMBOL_CHECK_DIGIT_CHARACTER = "x";
    private static final String SYMBOL_IBAN_CHECK_DIGIT = "k";
    private static final String SYMBOL_CURRENCY_CODE = "m";
    private static final String SYMBOL_ACCOUNT_TYPE = "t";
    private static final String SYMBOL_NATIONAL_ID = "i";
    private static final String SYMBOL_OWNER_ACCT = "n";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";

    private static Properties ibanFormats = null;

    public static IbanAccount parseAccount(String iban) {
        if (Objects.isNull(iban)) {
            return null;
        }
        loadProperties();
        IbanAccount account = new IbanAccount().countryCode(StringUtils.substring(iban, 0, 2));
        String format = ibanFormats.getProperty(account.countryCode());
        boolean withSpaces = iban.contains(SPACE);
        account.ibanFormat(format);
        getFieldFromSymbol(iban, format, SYMBOL_ACCOUNT, withSpaces).ifPresent(x -> account.accountNumber(x));
        getFieldFromSymbol(iban, format, SYMBOL_ACCOUNT_TYPE, withSpaces).ifPresent(x -> account.accountType(x));
        getFieldFromSymbol(iban, format, SYMBOL_BANK_CODE, withSpaces).ifPresent(x -> account.nationalBankCode(x));
        getFieldFromSymbol(iban, format, SYMBOL_BRANCH_CODE, withSpaces).ifPresent(x -> account.branchCode(x));
        getFieldFromSymbol(iban, format, SYMBOL_CHECK_DIGIT_CHARACTER, withSpaces)
                .ifPresent(x -> account.checkDigitOrCharacter(x));
        getFieldFromSymbol(iban, format, SYMBOL_IBAN_CHECK_DIGIT, withSpaces)
                .ifPresent(x -> account.ibanCheckDigits(x));
        getFieldFromSymbol(iban, format, SYMBOL_CURRENCY_CODE, withSpaces).ifPresent(x -> account.currencyCode(x));
        getFieldFromSymbol(iban, format, SYMBOL_NATIONAL_ID, withSpaces).ifPresent(x -> account.holderNationalId(x));
        getFieldFromSymbol(iban, format, SYMBOL_OWNER_ACCT, withSpaces).ifPresent(x -> account.ownerAccountNumber(x));
        setFormattedAccount(account);
        return account;
    }

    private static Optional<String> getFieldFromSymbol(String iban, String ibanFormat,
            String symbol, boolean withSpaces) {
        if (!withSpaces) {
            iban = StringUtils.replace(iban, SPACE, EMPTY_STRING);
            ibanFormat = StringUtils.replace(ibanFormat, SPACE, EMPTY_STRING);
        }
        int startIndex = StringUtils.indexOf(ibanFormat, symbol);
        int endIndex = StringUtils.lastIndexOf(ibanFormat, symbol) + 1;
        return Optional.ofNullable(StringUtils.substring(iban, startIndex, endIndex));
    }

    private static void setFormattedAccount(IbanAccount account) {
        int startIndex = StringUtils.indexOf(account.ibanFormat(), SYMBOL_ACCOUNT);
        int endIndex = StringUtils.lastIndexOf(account.ibanFormat(), SYMBOL_ACCOUNT) + 1;
        account.accountNumber(account.accountNumber().replace(SPACE, EMPTY_STRING));
        StringBuilder accountBuilder = new StringBuilder(account.accountNumber());
        String accountFormat = StringUtils.substring(account.ibanFormat(), startIndex, endIndex);
        for (int i = 0; i < accountFormat.length(); i++) {
            if (String.valueOf(accountFormat.charAt(i)).equals(" ")) {
                accountBuilder.insert(i, SPACE);
            }
        }
        account.formattedAccountNumber(accountBuilder.toString());
    }

    private static void loadProperties() {
        if (Objects.isNull(ibanFormats)) {
            ibanFormats = new Properties();
            try (InputStream input = IbanAccountParser.class.getClassLoader()
                    .getResourceAsStream("iban-format.properties")) {
                // load a properties file
                ibanFormats.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
