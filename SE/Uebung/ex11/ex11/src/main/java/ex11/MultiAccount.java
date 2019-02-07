package ex11;

import java.util.Collections;
import java.util.LinkedList;

public class MultiAccount extends BankAccount {
    LinkedList<BankAccount> subAccounts = new LinkedList<>();
    private static int idctr = 0;

    public MultiAccount(BankAccount... subAccounts) {
        this.ID = getNextID();
        Collections.addAll(this.subAccounts, subAccounts);
    }

    private static String getNextID() {
        return String.valueOf(idctr++);
    }

    @Override
    void addListener(AccountUpdatedListener listener) {
        subAccounts.forEach(a -> a.addListener(listener));
    }

    public void addBankAccount(BankAccount ba) {
    	subAccounts.add(ba);
    }
    
    @Override
    public int getAmount() {
    	int entireAmount = 0;
    	for(BankAccount a: subAccounts)
    		entireAmount += a.getAmount();
    	return entireAmount;
    }

    @Override
    void applyInterestRate(float interestRate) {
        subAccounts.forEach(a -> a.applyInterestRate(interestRate));
    }

    public void removeBankAccount(BankAccount ba) {
    	subAccounts.remove(ba);
    }

    public void getBankAccountByPosition(int position) {
    	subAccounts.get(position);
    }

}
