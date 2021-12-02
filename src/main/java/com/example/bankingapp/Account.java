package com.example.bankingapp;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Table(name = "account")
public class Account{
    @Id
    @Column(unique = true)
    @GeneratedValue
    public long accId;

    @Column
    public String accHolder;
    @Column
    public float accBal;
    @Column
    public float accLoan;
    @Column
    public Float netBal;

    public Account(){
    }


    public Account(String accHolder, long accId, float accBal, float accLoan){
        this.accHolder = accHolder;
        this.accId = accId;
        this.accBal = accBal;
        this.accLoan = accLoan;
        this.netBal = accBal - accLoan;
    }

    public void setAccId(long accId){this.accId = accId;}
    public long getAccId() {return accId;}

    public void setAccHolder(String accHolder){this.accHolder = accHolder;}
    public String getAccHolder() {return accHolder;}

    public void setAccBal(float accBal){this.accBal = accBal;}
    public float getAccBal() {return accBal;}

    public void setAccLoan(float accLoan){this.accLoan = accLoan;}
    public float getAccLoan() {return accLoan;}

    public void setNetBal(float netBal){this.netBal = netBal;}
    public float getNetBal() {return netBal;}

    public HashMap<String, String> create_account(){
        HashMap<String, String> created_account = new HashMap<>();
        created_account.put("accHolder", accHolder);
        created_account.put("accId", String.valueOf(accId));
        created_account.put("accBal", String.valueOf(accBal));
        created_account.put("accLoan", String.valueOf(accLoan));
        created_account.put("netBal", String.valueOf(netBal));
        return created_account;
    }
}


