package ex11;

import java.util.*;

public class CustomerInterface implements AccountUpdatedListener {
    private HashMap<String, BankAccount> accounts = new HashMap<>();
    private Map<String, Date> updateHistory = new HashMap<>();

    CustomerInterface(BankAccount... accounts) {
        for (BankAccount bankAccount: accounts) {
            this.accounts.put(bankAccount.getID(), bankAccount);
        }
    }

    @Override
    public void accountUpdated(BankAccount bankAccount) {
        this.accounts.get(bankAccount.getID()); // do some updates
        updateHistory.put(bankAccount.getID(), new Date());
    }

    public Map<String, Date> getUpdateHistory() {
        return updateHistory;
    }
}
