package com.example.bankingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/banking")
public class MyBankingApp {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Ping
    @GetMapping("/ping")
    public Map<String, String> ping() {
        HashMap<String, String> response = new HashMap<>();
        response.put("response", "ping");
        return response;
    }

    // Create Account
    @PostMapping("/accounts")
    public Map<String, String> create_acc(@RequestBody Map<String, String> body){
        // Data Collection from Request
        String acc_holder = body.get("acc_holder");
        long acc_id = Long.parseLong(body.get("acc_id"));
        float acc_bal = Float.parseFloat(body.get("acc_bal"));
        float acc_loan = Float.parseFloat(body.get("acc_loan"));
        // Creating an account object
        Account acc = new Account(acc_holder, acc_id, acc_bal, acc_loan);
        // Creating account record in db
        accountRepository.save(acc);
        // Returning response
        return acc.create_account();
    }

    // List All Accounts
    @GetMapping("/accounts")
    public List<Account> get_accounts(){
        return accountRepository.findAll();
    }

    // Get Account Snapshot
    @GetMapping("/account")
    public Optional<Account> get_acc(@RequestBody Map<String, String> body) {
        long acc_id = Long.parseLong(body.get("acc_id"));
        return accountRepository.findById(acc_id);
    }

    @PostMapping("/account/transaction")
    public Map<String, String > post_trans(@RequestBody Map<String, String> body){

        float pre_trans_acc_bal;
        // Collecting Data from Request body
        long trans_id = Long.parseLong(body.get("trans_id"));
        long acct_id = Long.parseLong(body.get("acct_id"));
        String trans_type = body.get("trans_type");
        float trans_amount = Float.parseFloat(body.get("trans_amount"));
        String trans_vendor = body.get("trans_vendor");
        String base_type = body.get("base_type");


        Optional<Account> account_held = accountRepository.findById(acct_id);
        try{
            if (account_held.isPresent()){
                pre_trans_acc_bal = account_held.get().accBal;
                float loan = account_held.get().accLoan;
                Transaction trns = new Transaction(trans_id, acct_id,trans_type, trans_amount, trans_vendor, base_type, pre_trans_acc_bal);
                Map<String, String> transaction = trns.process_transaction();
                System.out.println(transaction);
                float post_trans_acc_bal = Float.parseFloat(transaction.get("postTransAccBal"));
                account_held.stream().forEach(account -> account.setAccBal(post_trans_acc_bal));
                account_held.stream().forEach(account -> account.setNetBal(post_trans_acc_bal-loan));
                transactionRepository.save(trns);
                return transaction;
            }else{
                HashMap<String,String> message = new HashMap<>();
                message.put("Message", "Account not found");
                return message;
            }

        }catch (NullPointerException e){
            HashMap<String,String> message = new HashMap<>();
            message.put("Message", "Account not found");
            return message;
        }
    }

    // List Funds
    @PutMapping("/account/loan")
    public Map<String, String> process_loan(@RequestBody Map<String, String> body) {
        long acc_id = Long.parseLong(body.get("acc_id"));
        float loan_amount = Float.parseFloat(body.get("loan_amount"));
        Optional<Account> account_held = accountRepository.findById(acc_id);
        try{
            if(account_held.isPresent()){
                float acc_loan = account_held.get().accLoan;
                float acc_bal = account_held.get().accBal;
                float updated_loan = acc_loan + loan_amount;
                float updated_net_bal = acc_bal - updated_loan;
                account_held.stream().forEach(account -> account.setAccBal(updated_net_bal));
                account_held.stream().forEach(account -> account.setAccLoan(updated_loan));
                HashMap<String, String> response = new HashMap<>();
                response.put("acc_id", String.valueOf(acc_id));
                response.put("acc_loan", String.valueOf(acc_loan));
                response.put("pre_loan_acc_bal", String.valueOf(updated_loan));
                response.put("post_loan_acc_net_bal", String.valueOf(updated_net_bal));
                return response;
            }else{
                HashMap<String,String> message = new HashMap<>();
                message.put("Message", "Account not found");
                return message;
            }

        } catch(NullPointerException e){
            HashMap<String,String> message = new HashMap<>();
            message.put("Message", "Account not found");
            return message;
        }
    }
}
