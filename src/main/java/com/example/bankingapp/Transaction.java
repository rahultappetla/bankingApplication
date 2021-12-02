package com.example.bankingapp;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @Column(unique = true)
    @GeneratedValue
    public long transId;

    @Column
    public long acctId;
    @Column
    public String transType; // PURCHASE, DEPOSIT, WITHDRAWAL
    @Column
    public String baseType; // CREDIT, DEBIT
    @Column
    public float transAmount;
    @Column
    public String transVendor;
    @Column
    public Float preTransAccBal;
    @Column
    public Float postTransAccBal;

    public Transaction(){
    }

    public Transaction(long transId, long acctId, String transType, float transAmount, String transVendor, String baseType,
                       float preTransAccBal) {
        this.transId = transId;
        this.acctId = acctId;
        this.transType = transType;
        this.transAmount = transAmount;
        this.baseType = baseType;
        this.transVendor = transVendor;
        this.preTransAccBal = preTransAccBal;
        this.postTransAccBal = preTransAccBal + transAmount;
    }


    public HashMap<String, String> process_transaction(){
        HashMap<String, String> transaction = new HashMap<>();
        transaction.put("transId", String.valueOf(transId));
        transaction.put("acctId", String.valueOf(acctId));
        transaction.put("transAmount", String.valueOf(transAmount));
        transaction.put("postTransAccBal", String.valueOf(postTransAccBal));
        return transaction;
    }
}


