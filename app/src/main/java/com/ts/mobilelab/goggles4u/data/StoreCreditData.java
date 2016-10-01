package com.ts.mobilelab.goggles4u.data;

/**
 * Created by PC2 on 19-04-2016.
 */
public class StoreCreditData {

    private String action;

    public String getBalancecharge() {
        return balancecharge;
    }

    public void setBalancecharge(String balancecharge) {
        this.balancecharge = balancecharge;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBalanceremain() {
        return balanceremain;
    }

    public void setBalanceremain(String balanceremain) {
        this.balanceremain = balanceremain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String balancecharge;
    private String balanceremain;
    private String date;
}
