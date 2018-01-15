package com.mobisquid.mobicash.payment.model;

/**
 * Created by manis on 12/29/2017.
 */

public class ProxyBank {
    private String bankName;
    private String bankCode;
    private String bankAccountNumber;
    private String internationalBank;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getInternationalBank() {
        return internationalBank;
    }

    public void setInternationalBank(String internationalBank) {
        this.internationalBank = internationalBank;
    }
}
