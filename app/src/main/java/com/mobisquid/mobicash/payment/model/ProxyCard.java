package com.mobisquid.mobicash.payment.model;

/**
 * Created by manis on 12/29/2017.
 */

public class ProxyCard {
    private String cardNumber;
    private String expiryDate;
    private String pin;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
