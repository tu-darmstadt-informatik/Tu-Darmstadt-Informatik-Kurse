package ex11;

import java.util.LinkedList;

public abstract class BankAccount {
    String ID;
    LinkedList<AccountUpdatedListener> listeners = new LinkedList<AccountUpdatedListener>();

    abstract void applyInterestRate(float interestRate);
    abstract int getAmount();

    void addListener(AccountUpdatedListener listener) {
        listeners.add(listener);
    }

    void removeListener(AccountUpdatedListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners() {
        listeners.forEach(l -> l.accountUpdated(this));
    }

    String getID() {
        return this.ID;
    }
}
